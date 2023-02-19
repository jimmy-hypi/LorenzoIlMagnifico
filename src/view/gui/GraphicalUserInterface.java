package view.gui;

import client.ClientController;
import model.Period;
import model.PersonalBonusTile;
import model.Player;
import model.area.Board;
import model.card.CardType;
import model.card.DevelopmentCard;
import model.card.LeaderCard;
import model.resource.ResourceChest;
import view.UserInterface;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The GUI, implementations of the User Interface using Swing and AWT components
 */
public class GraphicalUserInterface implements UserInterface, ActionListener {
    private MyFrame frame;
    private PersonalBoard personalBoard;
    private Image icon;
    private ClientController gameController;
    private boolean excommunicationCubeNeeded;
    private boolean isSatan;


    /**
     * Instantiates a new graphical user interface.
     *
     * @param clientController the client controller
     */
    public GraphicalUserInterface(ClientController clientController) {
        this.gameController = clientController;
        frame = new MyFrame();
        personalBoard = new PersonalBoard();
        frame.validate();
    }
    public void addListeners() {
        frame.getGamePanel().getSendChat().addActionListener(this);
        frame.getGamePanel().getShowPersonalBoard().addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == frame.getGamePanel().getSendChat()) {
            String message;
            message = frame.getGamePanel().getAndDeleteChatInput();

            gameController.notifyChatMessage(message);

        } else if (e.getSource() == frame.getGamePanel().getShowPersonalBoard()) {
            personalBoard.setVisible(!personalBoard.isVisible());
        }
    }

    @Override
    public void startDraft(ArrayList<LeaderCard> leaderCards) {
        if(leaderCards.size()==4)
            writeGameMessage("The Leader Draft phase has started!");

        writeGameMessage("Choose the leader card you want and pass the other 3 to"
                + "the player at your right");
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    frame.getGamePanel().showChooseLeaderDraft(leaderCards);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void initializeMatch(int numPlayers) {
        frame.removeInitialImage();
        frame.initializeGameFrame(numPlayers);

        if(!isSatan){
            this.addListeners();
        }

        frame.getGamePanel().setObserver(this);
        frame.pack();
        frame.repaint();
        writeGameMessage("The game has started");
    }

    private void writeGameMessage(String string) {
        writeMessage("\n<-GAME-> " + string + "\n");
    }

    private void writeMessage(String message) {
        frame.getGamePanel().addMessageToConsole(message);
    }


    @Override
    public void initializeTurn(Period period, int turn) {
        writeGameMessage("A new turn is starting, Period:" + period.toString() + " Turn:" + turn);
        FamilyMemberPawn.councilCounter = 0;
        frame.getGamePanel().removeCards();
        frame.getGamePanel().resetFamiliars();
    }

    @Override
    public void startTurn() {}

    @Override
    public void commandNotValid() {
        writeGameMessage("Your command is invalid");
    }

    @Override
    public void playerStatusChange(Player p) {
        if(excommunicationCubeNeeded){
            frame.getGamePanel().addExcommunicationCubes(p);
            excommunicationCubeNeeded=false;
        }
        frame.refreshPlayerStatus(p);
        addCardsToPersonalBoard(p);
        frame.refreshLeaderCards(p.getLeaderCards());
        frame.getGamePanel().setPointsMarkers(p);
        frame.getGamePanel().repaintResources();
    }

    private void addCardsToPersonalBoard(Player p) {
        ArrayList<DevelopmentCard> deck;

        for (int i = 0; i < CardType.values().length; i++) {
            if (CardType.values()[i] != CardType.ANY) {
                deck=p.getDeckOfType(CardType.values()[i]);
                if(deck.size()!=personalBoard.getRightNum(CardType.values()[i])){
                    DevelopmentCard cardToAdd=deck.get(deck.size()-1);
                    JDevelopmentCard jCard=new JDevelopmentCard
                            (cardToAdd.getCardType(), cardToAdd.getId(), personalBoard.getRightNum(CardType.values()[i]));
                    personalBoard.getPersonalBoardPanel().add(jCard);
                    personalBoard.repaint();
                    personalBoard.incrementRightNum(cardToAdd.getCardType());

                }
            }
        }
    }

    @Override
    public void playerMove() {}

    @Override
    public void playerTurn() {}

    @Override
    public void win() {
        writeGameMessage("CONGRATULATIONS! You won the game!\nPress any key to exit the game");
    }

    @Override
    public void lose() {
        writeGameMessage("OOPS, You lost. Try again, next time you will be luckier!\nPress any key to exit the game");
    }

    @Override
    public void askPrivilegeChoice(int numberOfPrivilege, List<ResourceChest> privilegeResources) {
        writeGameMessage("You have " + numberOfPrivilege + " council privileges to "
                + "choose, click on the resource you would like to get");
        frame.showPrivilegeChoice();
        frame.getGamePanel().setCurrentNumberOfPrivilege(numberOfPrivilege);
    }

    @Override
    public void askMove() {
        writeGameMessage("It's your turn, decide an action to perform");
        frame.showChooseAction();
        frame.getGamePanel().setLeaderState("none");
    }

    @Override
    public void invalidInput() {
        writeMessage("--INVALID INPUT--");
    }

    @Override
    public void askPersonalBonusTile(List<PersonalBonusTile> personalBonusTiles) {}

    @Override
    public void assignColor(String color) {
        if(color.equals("black"))
            isSatan=true;

        gameController.setPlayerColor(color);
        frame.setPlayerColor(color);
    }

    @Override
    public void refreshBoard(Board board) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    frame.refreshBoard(board);
                    OrderMarkerDisk.Ordercounter = 0;
                    frame.getGamePanel().setExcommTiles(board);
                    frame.getGamePanel().populateFamiliars(board);
                    frame.getGamePanel().createMarkers(board);
                    frame.getGamePanel().removeDicesAndMarkers();
                    frame.getGamePanel().updateOrder(board);
                    frame.getGamePanel().PlaceFamiliars(board);
                    frame.getGamePanel().setDices(board);

                    frame.getGamePanel().repaintBoard();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void notifyExcommunication() {
        excommunicationCubeNeeded=true;
        writeGameMessage("God seems very offended by your behaviour, he established your excommunication.");
    }

    @Override
    public void newChatMessage(String message) {
        writeMessage(message);
    }

    @Override
    public void askNameAndPassword() {
        if(!isSatan){
            writeGameMessage("Insert Name and Password to Login or Signup");
            frame.getGamePanel().showAskAuthentication();
        }
    }

    @Override
    public void askForProductionExchangedEffect(List<String[]> choices) {
        ArrayList<Integer> cardsIds=new ArrayList<>();

        writeGameMessage("Choose the production effects you want to activate:");
        for(String[] stringArray: choices){
            cardsIds.add(Integer.parseInt(stringArray[0]));
            writeGameMessage("Normal effect: "+stringArray[2]+" or alternative effect"+stringArray[3]);
        }

        frame.getGamePanel().showChooseProductionEffects(cardsIds);
    }

    @Override
    public void notifyRoundTimerExpired() {
        writeGameMessage("Your time's up, you've been disconnected");
        frame.getGamePanel().playerDisconnected();
    }

    @Override
    public void askForExcommunicationPayment(String excommunicationEffect) {
        writeGameMessage("The excommunication phase has started, choose if you want "
                + "to be excommunicated or not, the excommunication effect is: "+
                excommunicationEffect);
        frame.showExcommunicationPanel();
    }

    @Override
    public void opponentStatusChanged(Player maskedPlayer) {
        frame.getGamePanel().addExcommunicationCubes(maskedPlayer);
        frame.getGamePanel().setPointsMarkers(maskedPlayer);
        frame.getGamePanel().repaintBoard();
    }

    @Override
    public void askFinishRoundOrDiscard() {
        writeGameMessage("Choose what you want to do next, Discard a leader card or end the turn");
        frame.showEndOrDiscard();
    }

    @Override
    public void actionCommandNotValid(String reason) {
        writeGameMessage("Your action is invalid: " + reason);
    }

    public void notifyTakeCard(ArrayList<String> actionConstructor) {
        gameController.notifyTakeCardAction(actionConstructor);
    }

    public void notifyEndRound() {
        gameController.notifyFinishRound();
    }

    public void notifyCloseGame() {
        gameController.notifyRequestClosureCommand();
        personalBoard.dispose();
        frame.dispose();
    }
    public void notifyChosenPrivilege(String chosenP) {
        gameController.notifyChosenPrivileges(chosenP);
    }
    public void notifyMarketAction(ArrayList<String> actionConstructor) {
        gameController.notifyMarket(actionConstructor);
    }
    public void notifyCouncilAction(ArrayList<String> actionConstructor) {
        gameController.notifyCouncilPalace(actionConstructor);
    }

    public void notifyHarvest(ArrayList<String> actionConstructor) {
        gameController.notifyHarvest(actionConstructor);
    }

    public void notifyProduction(ArrayList<String> actionConstructor) {
        gameController.notifyProduction(actionConstructor);
    }

    public void notifyExcommunicationChoice(boolean showSupportDecision) {
        gameController.notifyExcommunicationEffectChoice(showSupportDecision);
    }

    public void notifyChosenLeaderInDraft(String leaderName) {
        gameController.notifyChosenLeaderCard(leaderName);
    }

    public void notifyActivateLeader(String leaderName) {
        gameController.notifyLeaderEffectActivation(leaderName);
    }

    public void notifyDiscardLeader(String leaderName) {
        gameController.notifyDiscardedLeaderCard(leaderName);
    }

    public void notifyActivateProduction(ArrayList<Integer> choices) {
        gameController.notifyProductionChoices(choices);
    }
    public void notifyAuthenticationRequest(String username, String password) {
        gameController.notifyAuthenticationRequest(username,password);
    }
    public void notifySatanChoice(String playerColor) {
        gameController.notifySatanChoice(playerColor);
    }

    @Override
    public void notifyServerClosed() {
        writeGameMessage("The server has closed the game and the connections"
                + " because no one was connected anymore");
    }

    @Override
    public void authenticatedCorrectly(String username) {
        writeGameMessage(username+" your authentication was successful");
        frame.getGamePanel().setUsername(username);
        frame.getGamePanel().removeActionPanel();
    }

    @Override
    public void displayWrongPasswordMessage(String username) {
        writeGameMessage("The password you inserted did not correspond to the one of the player: "+username+" , please try again");
        frame.getGamePanel().showAskAuthentication();
    }

    @Override
    public void displayPlayerDisconnected(String color) {
        writeGameMessage("The "+color+" player has disconnected from the game!");
    }

    @Override
    public void askSatanMove() {
        writeGameMessage("Satan: decide the player you want to punish "
                + "subtracting him some pseudo-random victory points, I'll remind you"
                + " that you should choose the player who's winning in order not to"
                + " make him prevail over you");
        frame.getGamePanel().showSatanPanel();
    }

    @Override
    public void displaySatanAction(String color) {
        writeGameMessage("Satan has punished the " + color + " player!");
    }

    @Override
    public void requestReconnection() {
        Scanner i=new Scanner(System.in);
        System.out.println("Would you join an existing Match? (y/n)\n");

        String connChoice = i.next();

        if(connChoice.equals("y")){
            System.out.println("Please insert your name: \n");
            String name = i.next();
            System.out.println("your Password: \n");
            String pword = i.next();
            gameController.notifyReconnectionRequest(connChoice,name,pword);
        } else
            gameController.notifyReconnectionRequest(connChoice,null,null);
    }
}
