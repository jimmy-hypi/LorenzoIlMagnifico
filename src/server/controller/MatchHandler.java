package server.controller;

import command.toclient.*;
import command.toserver.ProductionActivationCommand;
import command.toserver.ProductionCommand;
import constant.CardConstants;
import constant.FileConstants;
import constant.NetworkConstants;
import exception.*;
import model.*;
import model.action.Action;
import model.action.IndustrialAction;
import model.area.BoardInitializer;
import model.area.Church;
import model.card.CardType;
import model.card.LeaderCard;
import model.excommunicationtile.ExcommunicationTile;
import model.resource.ResourceChest;
import model.resource.ResourceType;
import server.ClientHandler;
import server.ServerCommandHandler;
import server.ServerInterface;
import server.observers.MatchObserver;
import utils.User;
import utils.UsersCreator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.Executors;

/**
 * Controller according to the MVC model. Authoritative server, therefore the only entity making decisions
 * about the match. Contains references to the model and performs changes according to the logic of the
 * game. An instance of MatchHandler exists for every Match created by the Server
 */
public class MatchHandler implements Runnable, MatchHandlerObserver, MatchObserver {

    private List<ClientHandler> clients;
    private List<ClientHandler> closedClients;
    private transient ServerCommandHandler commandHandler;
    private ServerInterface serverInterface;
    private Match match;
    private Thread roundTimerThread;
    private int leaderResponseCounter = 0;
    private ArrayList<ArrayList<LeaderCard>> leaderSets;
    private int cycle = 1;
    private int roundNumber = 0;
    private ServerToClientCommand lastCommandSent;
    private int numPlayersAnsweredExcomm;
    private String prodFamilyMember;
    private int prodActionSpace;
    private int prodPaidServant;
    private boolean alreadyDoneAction = false;
    private ArrayList<User> users;
    private HashMap<String, User> userFromColor;
    private int authenticatedCorrectly;
    private long startTime;
    private ArrayList<User> disconnectedUsers;
    // 5th player option
    private boolean satanIsPresent;
    private ClientHandler fifthPlayerClient;

    public MatchHandler(List<ClientHandler> clients, ServerInterface serverInterface) {
        if (clients.size() == NetworkConstants.MAXPLAYERS) {
            satanIsPresent = true;
            this.fifthPlayerClient = clients.remove(clients.size() - 1);
        }
        this.clients = clients;
        this.serverInterface = serverInterface;
        closedClients = new ArrayList<ClientHandler>();
        userFromColor = new HashMap<>();

        disconnectedUsers = new ArrayList<User>();
        try {
            users = UsersCreator.getUsersFromFile();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        initMatch();
        startMatch();
    }

    private void initMatch() {
        match = new Match(clients.size(), this);

        if (satanIsPresent)
            match.createSatan();

        commandHandler = new ServerCommandHandler(this, match);

        setPlayers();

        communicateColors();

        match.setInitialPlayer();
    }
    private void initExistingMatch(int id) throws FileNotFoundException, ClassNotFoundException, IOException {
        match = MatchSaver.readMatch(id);

        commandHandler = new ServerCommandHandler(this, match);    }

    private void communicateColors(){
        if (satanIsPresent)
            try {
                fifthPlayerClient.sendCommand(new AssignColorCommand(match.getSatan().getColor()));
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        for (ClientHandler c : clients) {
            String color = null;
            try {
                color = this.getRightPlayer(c).getColor();
            } catch (WrongClientHandlerException e) {
                e.printStackTrace();
            }
            sendToClientHandler(new AssignColorCommand(color), c);
        }
    }
    private void startLeaderDiscardPhase() {
        leaderSets = match.getLeaderCards().getStartingLeaderSets(match.getPlayers().length);

        for (int i = 0; i < clients.size(); i++) {
            sendToClientHandler(new ChooseLeaderCardCommand(leaderSets.get(i)), clients.get(i));
        }
    }
    private void startMatch() {
        if (satanIsPresent)
            sendToAllPlayers(new InitializeMatchCommand(match.getPlayers().length + 1));
        else
            sendToAllPlayers(new InitializeMatchCommand(match.getPlayers().length));
        sendToAllPlayers(new RefreshBoardCommand(match.getBoard()));

        sendToAllPlayers(new AskAuthenticationCommand());

        this.startTime = System.currentTimeMillis();
    }
    public Player getCurrentPlayer() {
        return match.getCurrentPlayer();
    }
    private void setPlayers() {
        Collections.shuffle(clients);
        int i = 1;
        for (ClientHandler c : clients) {
            c.addPlayer(match.createAndReturnPlayer(i));
            c.addObserver(this);
            c.addCommandObserver(commandHandler);
            i++;
        }

        if (satanIsPresent) {
            fifthPlayerClient.addCommandObserver(commandHandler);
            fifthPlayerClient.addObserver(this);
        }

        match.setPlayerOrder();
    }
    /**
     * method invoked at the end of a turn that checks if the game is end or set
     * the next player.
     */
    public void setNext() {

        deactivateLeaderCards();

        try {
            match.setNextPlayer();
        } catch (EveryPlayerDisconnectedException e) {
            closeMatch();
            e.printStackTrace();
        }
    }
    private void startTurn(){
        updateGamePlayTimeForEveryone();

        match.handlePeriodsAndTurns();
        if (match.getTurn() == 7) {
            handleEndGame();
        } else {

            initTurn();

            sendToAllPlayers(new InitializeTurnCommand(match.getPeriod(), match.getTurn()));

            sendToAllPlayers(new RefreshBoardCommand(match.getBoard()));

            match.distributeTurnResources(); // this needs to be here and not in
            // initTurn,
            // otherwise the GUI wouldn't have a playerResources Panel to add
            // resources into

            startRound();
        }
    }
    private void initTurn(){
        refreshPlayerOrder();
        roundNumber = 0;

        match.clearBoard();

        match.getBoard().rollDices();
        match.refreshDicesValueForPlayers();
        match.addFamilyMembersToPlayers();

        this.match.getBoard().changeCardInTowers();
    }
    private void startRound() {
        alreadyDoneAction = false;
        stopTimerIfAlive();

        roundNumber++;
        sendToCurrentPlayer(new AskMoveCommand());
        startRoundTimer();
    }
    public void sendToAllPlayers(ServerToClientCommand command) {
        if (satanIsPresent)
            try {
                fifthPlayerClient.sendCommand(command);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        for (ClientHandler client : clients) {
            try {
                client.sendCommand(command);
            } catch (Exception e) {
                this.removeClient(client);
            }
        }
    }
    public void sendToAllPlayersExceptCurrent(ServerToClientCommand command) {

        if (satanIsPresent)
            try {
                fifthPlayerClient.sendCommand(command);
            } catch (IOException e2) {
                e2.printStackTrace();
            }

        ClientHandler dontSendClient;
        try {
            dontSendClient = this.getRightClientHandler(match.getCurrentPlayer());
        } catch (WrongPlayerException e1) {
            e1.printStackTrace();
            return;
        }
        for (ClientHandler client : clients) {
            if (client != dontSendClient) {
                try {
                    client.sendCommand(command);
                } catch (Exception e) {
                    this.removeClient(client);
                }
            }
        }
    }
    public void sendToPlayer(ServerToClientCommand command, Player player) {
        ClientHandler client;
        try {
            client = getRightClientHandler(player);
        } catch (WrongPlayerException e1) {
            return;
        }

        try {
            client.sendCommand(command);
            lastCommandSent = command;
        } catch (Exception e) {

            this.removeClient(client);
            e.printStackTrace();
        }
    }
    public void sendToCurrentPlayer(ServerToClientCommand command) {
        sendToPlayer(command, match.getCurrentPlayer());
    }
    public void startRoundTimer() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(FileConstants.ROUND_TIME));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int timeMillis = 0;
        try {
            timeMillis = Integer.parseInt(reader.readLine());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        roundTimerThread = new Thread(new RoundTimer(this, timeMillis));
        roundTimerThread.start();
    }
    public synchronized void closeMatch() {
        if (!clients.isEmpty()) {
            for (ClientHandler clientHandler : clients) {
                try {
                    clientHandler.closedByServer();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        serverInterface.closeMatch(this);
    }
    @Override
    public boolean isAllowed(Player player) {
        if (getCurrentPlayer() != null) {
            if (getCurrentPlayer() == player)
                return true;
            else
                return false;
        }
        return true;
    }

    @Override
    public void removeClient(ClientHandler clientHandler) {
        if (!closedClients.contains(clientHandler)){
            updateGamePlayTime(clientHandler.getPlayer().getColor());
            closedClients.add(clientHandler);
            try {
                addDisconnectedUser(getRightPlayer(clientHandler).getName());
            } catch (WrongClientHandlerException e2) {
                e2.printStackTrace();
            }

            try {
                sendToAllPlayers(new PlayerDisconnectedCommand(getRightPlayer(clientHandler).getColor()));
            } catch (WrongClientHandlerException e1) {
                e1.printStackTrace();
            }
            try{
                this.match.addDisconnectedPlayer(getRightPlayer(clientHandler));
            } catch (MatchFullException e){
            } catch (WrongClientHandlerException e){
            }
        }
    }
    private void addDisconnectedUser(String userName) {
        User u = getUserFromName(userName);
        if (u != null)
            disconnectedUsers.add(u);
    }
    private User getUserFromName(String name) {
        for (User u: users) {
            if (u.getUsername().equals(name))
                return u;
        }
        return null;
    }
    public void reconnectClient(ClientHandler clientHandler, User user) {
        if (isUserInTheGameAndDisconnected(user)) {
            Player p = match.getPlayerFromName(user.getUsername());

            if (p != null) {
                disconnectedUsers.remove(user);
                closedClients.remove(clientHandler);
                clientHandler.addPlayer(p);
                clientHandler.addCommandObserver(commandHandler);
                clientHandler.addObserver(this);


                try {
                    boolean removed=clients.remove(getRightClientHandler(p));
                } catch (WrongPlayerException e) {
                    e.printStackTrace();
                }

                clients.add(clientHandler);

                this.match.reconnectPlayer(p);

                String color = null;
                color=p.getColor();

                sendToClientHandler(new AssignColorCommand(color), clientHandler);
                sendToClientHandler(new InitializeMatchCommand(match.getPlayers().length), clientHandler);
                sendToClientHandler(new RefreshBoardCommand(this.match.getBoard()), clientHandler);
                sendToClientHandler(new AuthenticatedCorrectlyCommand(user.getUsername()), clientHandler);

            } else {
                System.out.println("getplayerfromname returned NULL");
            }
        }
    }
    private boolean isUserInTheGameAndDisconnected(User user) {
        if (!users.contains(user))
            return false;
        if (!disconnectedUsers.contains(user))
            return false;

        return true;
    }
    private void updateGamePlayTimeForEveryone(){
        for (int i = 0; i < match.getPlayers().length; i++) {
            updateGamePlayTime(match.getPlayers()[i].getColor());
        }
    }
    /**
     * Updates the gameplay time for the disconnected player
     *
     * @param color
     */
    private void updateGamePlayTime(String color) {
        long currentTime = System.currentTimeMillis();
        int elapsedTime = (int) ((currentTime - startTime) / 1000);
        userFromColor.get(color).incrementSecondsPlayed(elapsedTime);
        this.runUpdateFileThread();
    }

    private void runUpdateFileThread(){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                UsersCreator.updateFile(users);
            }
        });
    }
    private ClientHandler getRightClientHandler(Player player) throws WrongPlayerException {
        for (ClientHandler client : clients)
            if (client.getPlayer().equals(player))
                return client;
        throw new WrongPlayerException();
    }

    public void applyAction(Action action) throws NotApplicableException {
        action.apply();
        sendToAllPlayers(new RefreshBoardCommand(match.getBoard()));
        alreadyDoneAction = true;
        if (match.getCurrentPlayer().getCouncilPrivilege() != 0) {
            sendPrivilegeToCurrentPlayer(match.getCurrentPlayer().getCouncilPrivilege());
            match.getCurrentPlayer().resetPrivileges();
        } else {
            sendToCurrentPlayer(new AskFinishRoundOrDiscardCommand());
        }
    }
    private void applyAction(List<Integer> choices, IndustrialAction industrialAction) throws NotApplicableException {
        industrialAction.apply(choices);
    }

    @Override
    public void notifyPlayerStatusChange(Player player) {
        try {
            this.sendToClientHandler(new PlayerStatusChangeCommand(player), this.getRightClientHandler(player));
        } catch (WrongPlayerException e) {
            e.printStackTrace();
        }
        this.sendToAllPlayers(new OpponentStatusChangeCommand(player.maskedClone()));
    }
    public void roundTimerExpired() {
        sendToCurrentPlayer(new RoundTimerExpiredCommand());
        try {
            this.removeClient(getRightClientHandler(getCurrentPlayer()));
        } catch (WrongPlayerException e1) {
            e1.printStackTrace();
        }

        if (match.isAnyoneStillPlaying()) {
            setNext();
            nextStep();
        } else {
            this.match = null;
        }
    }
    private void nextStep() {
        if (satanIsPresent && (roundNumber % 4 == 0))
            sendToClientHandler(new AskSatanMoveCommand(), fifthPlayerClient);

        if ((roundNumber == match.getPlayers().length * 4) || currentPlayerWithoutFamilyMembers()) {
            if (match.getTurn() % 2 == 1) {
                startTurn();
            } else {
                startExcommunicationPhase();
            }
        } else
            startRound();
    }
    private boolean currentPlayerWithoutFamilyMembers() {
        if (this.match.getCurrentPlayer().getFamilyMembers().isEmpty()) {
            return true;
        }
        return false;
    }
    private void startExcommunicationPhase(){
        stopTimerIfAlive();

        ExcommunicationTile excommTile = getCurrentExcommTile();

        if (satanIsPresent)
            sendToAllPlayersExceptSatan(new AskForExcommunicationPaymentCommand(excommTile.getEffect().toString()));
        else
            sendToAllPlayers(new AskForExcommunicationPaymentCommand(excommTile.getEffect().toString()));
    }
    private void sendToAllPlayersExceptSatan(ServerToClientCommand command) {
        for (ClientHandler client : clients) {
            try {
                client.sendCommand(command);
            } catch (Exception e) {
                this.removeClient(client);
            }
        }
    }
    private void stopTimerIfAlive() {
        if (roundTimerThread != null)
            if (roundTimerThread.isAlive())
                roundTimerThread.interrupt();
    }
    public void handleCredentials(String username, String password, ClientHandler clientHandler) {
        // For now it's just setting the name of the user
        try {
            getRightPlayer(clientHandler).setName(username);
        } catch (WrongClientHandlerException e) {
            sendToClientHandler(new InvalidCommand(), clientHandler);
        }
    }
    private void sendToClientHandler(ServerToClientCommand command, ClientHandler clientHandler) {
        try {
            clientHandler.sendCommand(command);
        } catch (IOException e){
            e.printStackTrace();
            this.removeClient(clientHandler);
        }
    }
    private Player getRightPlayer(ClientHandler clientHandler) throws WrongClientHandlerException {
        for (ClientHandler c: clients) {
            if (c == clientHandler){
                return c.getPlayer();
            }
        }
        throw new WrongClientHandlerException();
    }
    private Player getPlayerFromColor(String playerColor) {
        Player[] players = this.match.getPlayers();
        for (int i = 0; i < players.length; i++) {
            if (players[i].getColor().equals(playerColor))
                return players[i];
        }
        return null;
    }
    public void handleLeaderChoice(String name, String playerColor) {
        leaderResponseCounter++;
        try {
            this.getPlayerFromColor(playerColor).addLeaderCards(match.getLeaderCards().getCard(name));
            removeLeaderFromSets(match.getLeaderCards().getCard(name));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (leaderResponseCounter == match.getPlayers().length) {

            leaderResponseCounter = 0;
            for (int i = 0; i < clients.size(); i++) {
                if (cycle == 3) {
                    try {
                        this.getRightPlayer(clients.get((i + cycle) % (match.getPlayers().length)))
                                .addLeaderCards(leaderSets.get(i).get(0));
                    } catch (WrongClientHandlerException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (i >= match.getPlayers().length - cycle) {
                        sendToClientHandler(new ChooseLeaderCardCommand(leaderSets.get(i)),
                                clients.get((i + cycle) % (match.getPlayers().length)));
                    } else {
                        sendToClientHandler(new ChooseLeaderCardCommand(leaderSets.get(i)), clients.get(i + cycle));
                    }
                }
            }
            cycle++;
            if (cycle == 4)
                startTurn();
        }
    }
    private void removeLeaderFromSets(LeaderCard leaderCard) {
        for (ArrayList<LeaderCard> set : leaderSets) {
            for (LeaderCard card : set) {
                if (leaderCard == card) {
                    set.remove(card);
                    return;
                }
            }
        }
    }
    public void finishRound(){
        setNext();
        nextStep();
    }
    public void handleEndGame(){
        Player[] rank = new Player[match.getPlayers().length];
        Player prevPlayer;
        for (int i = 0; i < match.getPlayers().length; i++) {
            int val = calculatePlayerPoints(match.getPlayers()[i]);
            rank[i] = match.getPlayers()[i];
            if (i > 0 && val > calculatePlayerPoints(match.getPlayers()[i - 1])) {
                prevPlayer = rank[i - 1];
                rank[i - 1] = match.getPlayers()[i];
                rank[i] = prevPlayer;
            }

        }
        if (this.satanIsPresent) {
            int satanVictoryPoints = match.getSatan().getResourceChest().getResourceInChest(ResourceType.VICTORYPOINT)
                    .getAmount();
            if (calculatePlayerPoints(rank[0]) < satanVictoryPoints) {
                sendToPlayer(new WinCommand(), match.getSatan());
                for (Player p : rank) {
                    sendToPlayer(new LoseCommand(), p);
                    getUserFromName(p.getName()).incrementLostMathces();

                }
            } else {
                sendToPlayer(new WinCommand(), rank[0]);
                sendToPlayer(new LoseCommand(), match.getSatan());
                for (Player p : rank) {
                    if (p != rank[0]) {

                        sendToPlayer(new LoseCommand(), p);
                        getUserFromName(p.getName()).incrementWonMathces();

                    }
                }
            }
        }
        sendToPlayer(new WinCommand(), rank[0]);
        for (Player p : rank) {
            if (p != rank[0]) {
                sendToPlayer(new LoseCommand(), p);
            }
        }

        runUpdateFileThread();
    }
    private int calculatePlayerPoints(Player p) {
        int points = 0;
        for (Player player : match.getPlayers()) {
            if (player == p) {
                points = points + p.getResourceChest().getResourceInChest(ResourceType.VICTORYPOINT).getAmount();
                points = points + calculatePointsFromResources(player);
                points = points + calculatePointsForTerritories(player);
                points = points + calculatePointsForCharacterCards(player);
                try {
                    points = points + calculatePointsForMilitaryPoints(player);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

        return points;
    }
    private int calculatePointsFromResources(Player p) {
        int ResourceSum = 0;
        for (ResourceType r : ResourceType.values()) {
            if (r != ResourceType.VICTORYPOINT && r != ResourceType.FAITHPOINT && r != ResourceType.MILITARYPOINT) {
                ResourceSum = ResourceSum + p.getResourceChest().getResourceInChest(r).getAmount();
            }
        }
        return ResourceSum / 5;
    }
    private int calculatePointsForTerritories(Player p) {
        int points = 0;
        ArrayList<Integer> territoryBonuses = new ArrayList<Integer>();
        try {
            territoryBonuses = BoardInitializer.playerBoardBonusesForTerritory();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (int i = 0; i < p.getDeckOfType(CardType.TERRITORY).size(); i++) {
            points = points + territoryBonuses.get(i);
        }

        return points;
    }
    private int calculatePointsForCharacterCards(Player p) {
        int points = 0;
        ArrayList<Integer> characterBonuses = new ArrayList<Integer>();
        try {
            characterBonuses = BoardInitializer.playerBoardBonusesForCharacter();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (int i = 0; i < p.getDeckOfType(CardType.CHARACTER).size(); i++) {
            points = points + characterBonuses.get(i);
        }

        return points;
    }
    private int calculatePointsForMilitaryPoints(Player p) throws IOException {
        Player[] rank = new Player[match.getPlayers().length];
        Player prevPlayer;
        int points = 0;
        for (int i = 0; i < match.getPlayers().length; i++) {
            int val = match.getPlayers()[i].getResourceChest().getResourceInChest(ResourceType.MILITARYPOINT)
                    .getAmount();
            rank[i] = match.getPlayers()[i];
            if (i > 0 && val > match.getPlayers()[i - 1].getResourceChest()
                    .getResourceInChest(ResourceType.MILITARYPOINT).getAmount()) {
                prevPlayer = rank[i - 1];
                rank[i - 1] = match.getPlayers()[i];
                rank[i] = prevPlayer;
            }
        }

        BufferedReader reader;

        reader = new BufferedReader(new FileReader(FileConstants.VICTORYFORMILITARY));

        int[] pointsFromFile = new int[match.getPlayers().length];

        for (int i = 0; i < pointsFromFile.length; i++) {
            pointsFromFile[i] = Integer.parseInt(reader.readLine());
            if (rank[i] == p) {
                points = pointsFromFile[i];
            }
        }

        reader.close();
        return points;
    }
    public void handleInvalidCommand(){
        sendToCurrentPlayer(lastCommandSent);
    }
    public void sendPrivilegeToCurrentPlayer(int numberOfPrivilege) {
        ResourceChest[] rc = null;
        try {
            rc = BoardInitializer.createPrivilegeResources(CardConstants.PRIVILEGE_RESOURCES);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<ResourceChest> arrayListPrivilege = getArrayListPrivilegeFromArray(rc);

        sendToCurrentPlayer(new AskPrivilegeChoiceCommand(numberOfPrivilege, arrayListPrivilege));

    }
    public void discardLeaderCard(String leaderName) {
        match.getCurrentPlayer().removeLeaderCard(leaderName);
        sendPrivilegeToCurrentPlayer(1);
    }
    private ArrayList<ResourceChest> getArrayListPrivilegeFromArray(ResourceChest[] rc) {
        ArrayList<ResourceChest> arr = new ArrayList<ResourceChest>();
        for(int i = 0; i < rc.length; i++){
            arr.add(rc[i]);
        }
        return arr;
    }
    public void addPrivilegeResources(ArrayList<Integer> choice) {
        if (isPrivilegeCorrect(choice)) {
            ResourceChest[] rc = null;
            try {
                rc = BoardInitializer.createPrivilegeResources(CardConstants.PRIVILEGE_RESOURCES);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ResourceChest resourcesToGive = new ResourceChest();

            for (int i = 0; i < choice.size(); i++) {
                resourcesToGive.addChest(rc[choice.get(i)]);
            }
            this.getCurrentPlayer().addResources(resourcesToGive);

            if (alreadyDoneAction)
                sendToCurrentPlayer(new AskFinishRoundOrDiscardCommand());
            else
                sendToCurrentPlayer(new AskMoveCommand());

        } else {
            sendToCurrentPlayer(new InvalidActionCommand("You modified the code and "
                    + "tried to get resources from a council privilege which are not valid in the game"));
            sendToCurrentPlayer(new AskFinishRoundOrDiscardCommand());
        }
    }
    private boolean isPrivilegeCorrect(ArrayList<Integer> choice) {
        if (privilegeChoiceHasDuplicates(choice))
            return false;
        else
            return true;
    }
    private boolean privilegeChoiceHasDuplicates(ArrayList<Integer> choice) {
        for (int i = 0; i < choice.size(); i++) {
            for (int j = 0; j < choice.size(); j++) {
                if (i != j && choice.get(i) == choice.get(j)) {
                    return true;
                }
            }
        }
        return false;
    }
    private void refreshPlayerOrder(){
        Player[] oldList = match.getPlayers();

        ArrayList<FamilyMember> councilMemberList = match.getBoard().getCouncilPalace().getMembers();

        ArrayList<Player> councilPlayers = new ArrayList<Player>();

        if (!councilMemberList.isEmpty()) {
            for (int i = 0; i < councilMemberList.size(); i++) {
                councilPlayers.add(councilMemberList.get(i).getPlayer());
            }
            for (int i = 0; i < councilPlayers.size(); i++) {
                for (int j = 0; j < councilPlayers.size(); j++) {
                    if (i != j && councilPlayers.get(i) == councilPlayers.get(j)) {
                        councilPlayers.remove(j);
                    }
                }
            }
            for (int i = 0; i < oldList.length; i++) {
                if (!councilPlayers.contains(oldList[i])) {
                    councilPlayers.add(oldList[i]);
                }
            }
            Player[] newList = new Player[oldList.length];
            for (int i = 0; i < councilPlayers.size(); i++) {
                newList[i] = councilPlayers.get(i);
            }

            match.setPlayers(newList);
        }
    }
    public int getRoundNumber() {
        return roundNumber;
    }
    public void handleChurchSupportDecision(String playerColor, boolean decision) {
        numPlayersAnsweredExcomm++;
        if (decision) {

            if (match.getPeriod().ordinal() + 2 <= getPlayerFromColor(playerColor).getResourceChest()
                    .getResourceInChest(ResourceType.FAITHPOINT).getAmount()) {

                this.getPlayerFromColor(playerColor).payFaithPoint();
                ResourceChest rc = new ResourceChest();
                rc.addResource(match.getChurchSupportPrizeInPeriod());
                this.getPlayerFromColor(playerColor).addResources(rc);
            } else {
                excommunicatePlayer(playerColor);
            }

        } else {
            excommunicatePlayer(playerColor);
        }

        if (numPlayersAnsweredExcomm == this.match.getPlayers().length - this.match.getDisconnectedPlayersNum()) {
            numPlayersAnsweredExcomm = 0;
            startTurn();
        }
    }
    private void excommunicatePlayer(String playerColor) {
        ExcommunicationTile tile;
        tile = this.getCurrentExcommTile();
        tile.getEffect().applyEffect(getPlayerFromColor(playerColor));

        if (match.getPeriod() == Period.FIRST)
            this.getPlayerFromColor(playerColor).setExcommunicatedFirst(true);
        else if (match.getPeriod() == Period.SECOND) {
            this.getPlayerFromColor(playerColor).setExcommunicatedSecond(true);
        } else if (match.getPeriod() == Period.SECOND) {
            this.getPlayerFromColor(playerColor).setExcommunicatedThird(true);
        }

        this.sendToPlayer(new NotifyExcommunicationCommand(), this.getPlayerFromColor(playerColor));
    }
    private ExcommunicationTile getCurrentExcommTile() {
        Church c = this.match.getBoard().getChurch();
        Period p = this.match.getPeriod();
        ExcommunicationTile excommTile = c.getExcommunicationTile(p);
        return excommTile;
    }
    public void saveProductionParams(ProductionCommand command) {
        this.prodActionSpace = command.getActionSpace();
        this.prodPaidServant = command.getPaidServants();
        this.prodFamilyMember = command.getFamilyMember();

    }
    public void handleProductionActivation(ProductionActivationCommand productionActivationCommand) {

        FamilyMember member = getCurrentPlayer().getFamilyMember(this.prodFamilyMember);

        if (this.prodActionSpace == 1) {
            try {
                this.applyAction(productionActivationCommand.getChoices(),
                        new IndustrialAction(member, match.getBoard().getProductionArea(),
                                match.getBoard().getProductionArea().getSingleActionSpace(), this.prodPaidServant));
            } catch (NotApplicableException e) {
                e.printStackTrace();
            }
        } else {
            try {
                this.applyAction(productionActivationCommand.getChoices(),
                        new IndustrialAction(member, match.getBoard().getProductionArea(),
                                match.getBoard().getProductionArea().getMultipleActionSpace(), this.prodPaidServant));
            } catch (NotApplicableException e) {
                sendToCurrentPlayer(new InvalidActionCommand(e.getNotApplicableCode()));
                sendToCurrentPlayer(new AskMoveCommand());
            }
        }

        sendToAllPlayers(new RefreshBoardCommand(match.getBoard()));
        if (match.getCurrentPlayer().getCouncilPrivilege() != 0) {
            sendPrivilegeToCurrentPlayer(match.getCurrentPlayer().getCouncilPrivilege());

            match.getCurrentPlayer().resetPrivileges();

        } else {
            alreadyDoneAction = true;
            sendToCurrentPlayer(new AskFinishRoundOrDiscardCommand());
        }

        this.prodActionSpace = 0;
        this.prodPaidServant = 0;
        this.prodFamilyMember = "";
    }
    public void deactivateLeaderCards() {
        if (!this.getCurrentPlayer().getLeaderCards().isEmpty())
            for (LeaderCard l : this.getCurrentPlayer().getLeaderCards().values()) {
                if (l.isActivationState())
                    l.getSpecialEffect().disapplyEffect(getCurrentPlayer());
                l.setActivationState(false);
            }
    }
    public void clientClosedTheGame(String playerColor) {
        try {
            this.removeClient(getRightClientHandler(getPlayerFromColor(playerColor)));
        } catch (WrongPlayerException e) {
            e.printStackTrace();
        }
    }
    public boolean isLeaderCardActivable(String leaderName) {
        LeaderCard leader = match.getLeaderCards().getCard(leaderName);
        ResourceChest resourcesRequired = leader.getRequirement().getResourcesRequired();
        int buildingCardRequired = leader.getRequirement().getBuildingCardRequired();
        int characterCardRequired = leader.getRequirement().getCharacterCardRequired();
        int ventureCardRequired = leader.getRequirement().getVentureCardRequired();
        int territoryCardRequired = leader.getRequirement().getTerritoryCardRequired();
        int anyCardRequired = leader.getRequirement().getAnyCardRequired();

        int totalCards = match.getCurrentPlayer().getDeckOfType(CardType.BUILDING).size()
                + match.getCurrentPlayer().getDeckOfType(CardType.CHARACTER).size()
                + match.getCurrentPlayer().getDeckOfType(CardType.VENTURE).size()
                + match.getCurrentPlayer().getDeckOfType(CardType.TERRITORY).size();

        ResourceChest cloned = match.getCurrentPlayer().getResourceChest().cloneChest();
        cloned.subChest(resourcesRequired);
        if (!cloned.isGreaterEqualThan(new ResourceChest(0, 0, 0, 0, 0, 0, 0))
                || match.getCurrentPlayer().getDeckOfType(CardType.BUILDING).size() < buildingCardRequired
                || match.getCurrentPlayer().getDeckOfType(CardType.CHARACTER).size() < characterCardRequired
                || match.getCurrentPlayer().getDeckOfType(CardType.VENTURE).size() < ventureCardRequired
                || match.getCurrentPlayer().getDeckOfType(CardType.TERRITORY).size() < territoryCardRequired
                || totalCards < anyCardRequired || leader.isActivationState())
            return false;
        else
            return true;

    }
    public Match getMatch() {
        return match;
    }
    public void handleAuthenticationRequest(String username, String password, String playerColor) {

        if(getUserOrCreateOne(username,password,playerColor)){
            this.authenticatedCorrectly++;
            sendToPlayer(new AuthenticatedCorrectlyCommand(username), getPlayerFromColor(playerColor));
        }
        else{
            sendToPlayer(new WrongPasswordCommand(username), getPlayerFromColor(playerColor));
            sendToPlayer(new AskAuthenticationCommand(), getPlayerFromColor(playerColor));
        }


        if(authenticatedCorrectly==this.match.getPlayers().length)
            startLeaderDiscardPhase();
    }

    /**
     * @param username
     *            of the player that requested the authentication
     * @param password
     *            of the player that requested the authentication
     * @return true if the password is correct or a new player has been created
     *         successfully (and there was no other player with the same
     *         username), false otherwise
     */
    private boolean getUserOrCreateOne(String username, String password, String playerColor) {

        final Optional<User> user = users.stream().filter(u -> u.getUsername().equals(username)).findFirst();

        if (user.isPresent()) {
            if (user.get().correctPassword(password)) {
                user.get().incrementMatches();

                this.runUpdateFileThread();

                setNameToPlayer(playerColor,username);

                userFromColor.put(playerColor, user.get());
                return true;
            } else
                return false;
        } else {
            User newUser = new User(username, password);
            this.users.add(newUser);
            userFromColor.put(playerColor, newUser);
            setNameToPlayer(playerColor,username);

            return true;
        }
    }
    private void setNameToPlayer(String playerColor, String username) {
        getPlayerFromColor(playerColor).setName(username);
    }
    public void handleSatanChoice(String color) {

        Random r = new Random();
        int amount = r.nextInt(3) + 3;
        int newRes = getPlayerFromColor(color).getResourceChest().getResourceInChest(ResourceType.VICTORYPOINT)
                .getAmount();

        newRes = newRes - amount;
        if (newRes < 0)
            newRes = 0;

        getPlayerFromColor(color).getResourceChest().getResourceInChest(ResourceType.VICTORYPOINT).setAmount(newRes);

        sendToAllPlayers(new NotifySatanActionCommand(color));

        notifyPlayerStatusChange(getPlayerFromColor(color));
    }
    public boolean hasDisconnectedPlayer() {

        return disconnectedUsers.size()!=0;
    }
    public boolean hasDisconnectedUser(User u) {
        for(User user: disconnectedUsers){
            if(user.getUsername().equals(u.getUsername()))
                return true;
        }
        return false;
    }
    public User getDisconnectedUser(User u) {
        for(User user: disconnectedUsers){
            if(user.getUsername().equals(u.getUsername()))
                return user;
        }
        return null;
    }
}
