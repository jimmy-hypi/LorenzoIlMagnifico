package model;

import constant.BoardConstants;
import exception.EveryPlayerDisconnectedException;
import exception.MatchFullException;
import model.area.Board;
import model.area.Floor;
import model.card.BuildingCard;
import model.card.CardType;
import model.card.DevelopmentCard;
import model.deck.LeaderDeck;
import model.resource.ResourceChest;
import model.resource.VictoryPoint;
import server.controller.MatchHandler;
import server.observers.MatchObserver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that represents a match
 */
public class Match implements Serializable {
    private static final long serialVersionUID = -4611746373897593079L;
    private static final ResourceChest roundResourceSupply = new ResourceChest(0, 2, 2, 3, 0, 0, 0);
    private Board board;
    private Player[] players;
    private int addedPlayers;
    private Player satan;
    private ArrayList<Player> disconnectedPlayers;
    private int currentPlayer = 0;
    private boolean matchFinished;
    private transient MatchObserver observer;
    private String[] playercolors;
    private int playerscreated;
    private LeaderDeck leaderCards;
    private Period period;
    private int turn = 0;

    public Match(int numPlayers, MatchHandler matchObserver) {
        this.setMatchObserver(matchObserver);
        try {
            board = new Board(numPlayers);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        players = new Player[numPlayers];
        disconnectedPlayers=new ArrayList<Player>();

        playercolors = new String[numPlayers];

        shuffleColors();

        try {
            leaderCards = new LeaderDeck();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shuffleColors() {
        ArrayList<String> colors = new ArrayList<>();
        colors.add("Red");
        colors.add("Green");
        colors.add("Blue");
        colors.add("Yellow");
        Collections.shuffle(colors);

        for (int i = 0; i < playercolors.length; i++) {
            playercolors[i] = colors.get(i);
        }

    }

    /**
     * Adds the player to the match.
     *
     * @param p the p
     * @throws MatchFullException the match full exception
     */
    public void addPlayer(Player p) throws MatchFullException {
        if (addedPlayers == players.length)
            throw new MatchFullException();
        else {
            this.players[addedPlayers] = p;
            p.addObserver(this.observer);
            addedPlayers++;
        }
    }

    /**
     * Adds the disconnected player.
     *
     * @param p the p
     * @throws MatchFullException the match full exception
     */
    public void addDisconnectedPlayer(Player p) throws MatchFullException{
        if(!isDisconnected(p)){


            if (disconnectedPlayers.size() == players.length)
                throw new MatchFullException();
            else {
                this.disconnectedPlayers.add(p);
            }
        }

    }

    public void reconnectPlayer(Player p){
        disconnectedPlayers.remove(p);

    }

    /**
     * Checks if the player p is disconnected.
     *
     * @param p the p
     * @return true, if is disconnected
     */
    private boolean isDisconnected(Player p) {
        return this.disconnectedPlayers.contains(p);
    }
    public Board getBoard() {
        return board;
    }

    public synchronized Player[] getPlayers() {
        return players;
    }

    public int getAddedPlayers() {
        return addedPlayers;
    }

    /**
     * Creates the and returns player.
     *
     * @param id the id
     * @return the player
     */
    public Player createAndReturnPlayer(int id) {
        Player player = new Player("", playercolors[playerscreated]);
        playerscreated++;
        try {
            this.addPlayer(player);
        } catch (MatchFullException e) {
            e.printStackTrace();
        }
        return player;
    }

    public void setInitialPlayer() {
        currentPlayer = 0;
    }

    public Player getCurrentPlayer() {
        return this.players[this.currentPlayer];
    }

    public Floor getFloor(CardType cardType, int index) {
        return this.board.getFloor(cardType, index);
    }

    public void setMatchObserver(MatchObserver observer) {
        this.observer = observer;
    }

    /**
     * Gets the current player production choices.
     *
     * @param familyMember the family member
     * @param actionSpace the action space
     * @param paidServants the paid servants
     * @return the current player production choices
     */
    public List<String[]> getCurrentPlayerProductionChoices(String familyMember, int actionSpace, int paidServants) {
        List<String[]> choices = new ArrayList<>();
        List<DevelopmentCard> buildingCards = this.getCurrentPlayer().getDeckOfType(CardType.BUILDING);
        Player player = getCurrentPlayer();
        FamilyMember fm = player.getFamilyMember(familyMember);
        String[] cardFields;

        for (DevelopmentCard card : buildingCards) {
            if (isApplicable(card, fm, player, paidServants)) {
                if ((card instanceof BuildingCard) && ((BuildingCard) card).hasProductionChoice()) {
                    cardFields = new String[4];
                    cardFields[0] = "" + card.getId();
                    cardFields[1] = card.getName();
                    String[] tmp = ((BuildingCard) card).getProductionChoice();
                    cardFields[2] = tmp[0];
                    cardFields[3] = tmp[1];

                    choices.add(cardFields);

                }
            }
        }
        return choices;
    }

    /**
     * Checks if is applicable.
     *
     * @param card the card
     * @param fm the fm
     * @param player the player
     * @param paidServants the paid servants
     * @return true, if is applicable
     */
    public boolean isApplicable(DevelopmentCard card, FamilyMember fm, Player player, int paidServants) {
        return (fm.getActionValue() + player.getBonuses().getActivationVariation(card.getCardType())
                + paidServants >= card.getActivationCost());
    }

    public int getChurchSupportCostInPeriod() {
        if (this.period == Period.FIRST) {
            return BoardConstants.FIRSTPERIOD_CHURCHSUPPORTCOST;
        } else if (this.period == Period.SECOND) {
            return BoardConstants.SECONDPERIOD_CHURCHSUPPORTCOST;
        } else {
            return BoardConstants.THIRDPERIOD_CHURCHSUPPORTCOST;
        }
    }

    public VictoryPoint getChurchSupportPrizeInPeriod() {
        if (this.period == Period.FIRST) {
            return this.board.getChurch().getVictoryPoints()[BoardConstants.FIRSTPERIOD_CHURCHSUPPORTCOST];
        } else if (this.period == Period.SECOND) {
            return this.board.getChurch().getVictoryPoints()[BoardConstants.SECONDPERIOD_CHURCHSUPPORTCOST];
        } else {
            return this.board.getChurch().getVictoryPoints()[BoardConstants.THIRDPERIOD_CHURCHSUPPORTCOST];
        }
    }

    public int getDisconnectedPlayersNum(){
        return disconnectedPlayers.size();
    }

    public Period getPeriod() {
        return period;
    }

    public LeaderDeck getLeaderCards() {
        return leaderCards;
    }

    public void distributeTurnResources() {
        for (int i = 0; i < players.length; i++) {
            ResourceChest rs=new ResourceChest(
                    BoardConstants.ROUND_COIN_FIRST_PLAYER+i,0,0,0,0,0,0);
            rs.addChest(roundResourceSupply);
            players[i].addResources(rs);
        }
    }

    public void setPlayers(Player[] players) {
        this.players = players;
        this.refreshOrder();
    }

    private void refreshOrder() {
        ArrayList<String> playerOrder=new ArrayList<String>();
        for(int i=0;i<players.length;i++){
            playerOrder.add(players[i].getColor());
        }

        board.setPlayerOrder(playerOrder);

    }

    public void incrementTurn() {
        this.turn++;
    }

    public void handlePeriodsAndTurns() {
        incrementTurn();
        if (this.turn == 1)
            this.period = Period.FIRST;
        else if (this.turn == 3)
            period = Period.SECOND;
        else if (this.turn == 5)
            period = Period.THIRD;


    }

    public int getTurn() {
        return turn;
    }

    /**
     * Sets the next player.
     *
     * @throws EveryPlayerDisconnectedException the every player disconnected exception
     */
    public void setNextPlayer() throws EveryPlayerDisconnectedException {
        if(players.length==disconnectedPlayers.size()){
            matchFinished=true;
            throw new EveryPlayerDisconnectedException();
        }
        if (this.currentPlayer == players.length - 1)
            this.currentPlayer = 0;
        else
            this.currentPlayer++;
        if(isDisconnected(currentPlayer)){
            this.setNextPlayer();
        }
    }

    private boolean isDisconnected(int currentPlayer) {
        return this.disconnectedPlayers.contains(players[currentPlayer]);
    }

    public Player getSatan(){
        return this.satan;
    }

    public void setPlayerOrder() {
        ArrayList<String> colors = new ArrayList<String>();
        for (int i = 0; i < players.length; i++)
            colors.add(playercolors[i]);
        this.board.setPlayerOrder(colors);
    }

    public void addFamilyMembersToPlayers() {
        for (int i = 0; i < players.length; i++) {
            players[i].addFamilyMembers();
        }
    }

    public void refreshDicesValueForPlayers() {
        for (int i = 0; i < this.getPlayers().length; i++)
            this.getPlayers()[i].refreshFamilyMemberValues();
    }

    public void clearBoard() {
        // clearing the council palace
        this.getBoard().getCouncilPalace().getMembers().clear();

        // clearing harvest area
        this.getBoard().getHarvestArea().getMultipleActionSpace().getMembers().clear();
        this.getBoard().getHarvestArea().getSingleActionSpace().setFamilyMember(null);

        // clearing production area
        this.getBoard().getProductionArea().getMultipleActionSpace().getMembers().clear();
        this.getBoard().getProductionArea().getSingleActionSpace().setFamilyMember(null);

        // clearing the market
        this.getBoard().getMarket().getMarketActionSpace("1").setFamilyMember(null);
        this.getBoard().getMarket().getMarketActionSpace("2").setFamilyMember(null);
        if (this.players.length == 4) {
            this.getBoard().getMarket().getMarketActionSpace("3").setFamilyMember(null);
            this.getBoard().getMarket().getMarketActionSpace("4").setFamilyMember(null);
        }

        // clearing the towers
        for (CardType c : CardType.values()) {
            if (c != CardType.ANY) {
                for (int i = 0; i < this.getBoard().getTower(c).getFloors().size(); i++) {
                    this.getBoard().getTower(c).getFloor(i).getActionSpace().setFamilyMember(null);
                }
            }
        }
    }

    public boolean isAnyoneStillPlaying() {
        return !matchFinished;
    }

    public Player getPlayerFromName(String name) {
        for(int i=0;i<players.length;i++){
            if(players[i].getName().equals(name))
                return players[i];
        }
        return null;
    }

    public void createSatan() {
        this.satan=new Player("Satan","black");
        satan.getResourceChest().addResource(new VictoryPoint(99));
    }
}
