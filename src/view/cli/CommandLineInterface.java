package view.cli;

import client.ClientController;
import constant.ClientConstants;
import model.Period;
import model.PersonalBonusTile;
import model.Player;
import model.area.Board;
import model.card.LeaderCard;
import model.resource.ResourceChest;
import view.UserInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Command Line Interface play mode.
 */
public class CommandLineInterface implements UserInterface, InputListener {
    private final String welcomeMessage = "|------------------------------Welcome to Lorenzo il Magnifico, the board game!----------------------------|";
    private InputReader reader;
    private ClientController gameController;
    private Thread inputReaderThread;
    private int readerState;
    private int moveState;
    private int takeCardState;
    private ArrayList<String> actionConstructor;
    private ArrayList<LeaderCard> leaderCardsLocalCopy;
    private ArrayList<String> possibleMoves;
    private ArrayList<String> possiblePlayerColors;

    public CommandLineInterface(ClientController clientController) {
        this.gameController = clientController;
        actionConstructor = new ArrayList<String>();
        leaderCardsLocalCopy = new ArrayList<LeaderCard>();
        reader = new InputReader();
        inputReaderThread = new Thread(reader);
        inputReaderThread.start();
        reader.addListener(this);
        print(welcomeMessage);
        initializePossibleMoves();
        initializePossibleColors();
    }

    /**
     * This method add all the colors in order to allow a faster control at this level
     */
    private void initializePossibleColors() {
        possiblePlayerColors = new ArrayList<String>();
        possiblePlayerColors.add("yellow");
        possiblePlayerColors.add("green");
        possiblePlayerColors.add("blue");
        possiblePlayerColors.add("red");
    }
    /**
     * This method adds all the actions in order to allow a faster control at this level
     */
    private void initializePossibleMoves() {
        possibleMoves = new ArrayList<String>();
        possibleMoves.add("action");
        possibleMoves.add("discard");
        possibleMoves.add("activate");
    }
    @Override
    public void startDraft(ArrayList<LeaderCard> leaderCards) {
        leaderCardsLocalCopy.clear();
        leaderCardsLocalCopy = leaderCards;
        print("Select a leader card from the following: ");
        if (leaderCards.size() == 0)
            System.out.println("leader cards è 0");
        for (int i = 0; i < leaderCards.size(); i++) {
            print("Number " + i + ":\n" + leaderCards.get(i).toString());
        }
        readerState = ClientConstants.SEND_CHOSEN_LEADERCARD;
    }

    @Override
    public void initializeMatch(int numPlayers) {
        print("A new game with " + numPlayers + " players is about to start!");
    }

    @Override
    public void initializeTurn(Period period, int turn) {
        printImp("New turn");
        print("Period: " + period.toString() + "\tTurn: " + turn);
    }

    @Override
    public void startTurn() {
        print("-------------------------------------------------------------");
        print("\nIt is now your turn, what do you want to do next?");
    }

    @Override
    public void commandNotValid() {
        printImp("Invalid command!");
    }

    @Override
    public void playerStatusChange(Player p) {
        print("This is your status updated: \n");
        print(p.toString());
    }

    @Override
    public void playerMove() {

    }

    @Override
    public void playerTurn() {

    }

    @Override
    public void win() {
        print("CONGRATULATIONS! You won the game!\nPress any key to exit the game");
        readerState = ClientConstants.SEND_END_GAME;
    }

    @Override
    public void lose() {
        print("OOPS, You lost. Try again, next time you will be luckier!\nPress any key to exit the game");
        readerState = ClientConstants.SEND_END_GAME;
    }

    @Override
    public void askPrivilegeChoice(int numberOfPrivilege, List<ResourceChest> privilegeResources) {
        print("Choose " + numberOfPrivilege
                + " different resources from the following (Please enter the numbers associated with your choices separated by commas, e.g. x,y,z, if you have more than one privilege to choose):");
        for (int i = 0; i < privilegeResources.size(); i++) {
            print("Number " + i + ":\n" + privilegeResources.get(i).toString() + "\n");
        }

        readerState = ClientConstants.SEND_PRIVILEGE_CHOICES;
    }
    /**
     * this method checks if the leader card name written by the player is correct
     *
     * @param input
     * @param leaderCards
     * @return boolean
     */
    private boolean checkLeaderCardInput(String input, ArrayList<LeaderCard> leaderCards){
        for(LeaderCard card : leaderCards){
            if(input.equals(card.getName()))
                return true;
        }
        return false;
    }

    /**
     * This method checks if the input corresponds to one of the available color
     *
     * @param input
     * @param possibleColors
     * @return boolean
     */
    private boolean checkColorInput(String input, ArrayList<String> possibleColors) {
        for(String color : possibleColors){
            if(input.equals(color))
                return true;
        }
        return false;
    }

    @Override
    public void askMove() {
        moveState=0;
        takeCardState=0;
        actionConstructor.clear();
        print("Choose what you want to do:");
        print("To perform an action, type \"action\"");
        print("To discard a leader card and get a privilege, type \"discard\"");
        print("To activate a leader card effect, type \"activate\"");
        readerState = ClientConstants.SEND_MOVE;
    }

    private void takeCardParams(String input){
        switch(takeCardState){
            case 0:
                print("Select the tower:");
                print("1 - Territory");
                print("2 - Building");
                print("3 - Character");
                print("4 - Venture");
                takeCardState=ClientConstants.SEND_TAKE_CARD_TOWER;
                break;
            case ClientConstants.SEND_TAKE_CARD_TOWER:
                print("Select the floor:");
                print("0 - First floor");
                print("1 - Second floor");
                print("2 - Third floor");
                print("3 - Fourth floor");
                takeCardState = ClientConstants.SEND_TAKE_CARD_FLOOR;
                break;
            case ClientConstants.SEND_TAKE_CARD_FLOOR:
                gameController.notifyTakeCardAction(actionConstructor);
                break;
        }
    }

    private void distinguishAction(String string, String takecard){
        switch(string){
            case "1":
                takeCardParams(takecard);
                break;
            case "3":
                print("Select market slot:");
                print("1 - First marketplace slot");
                print("2 - Second marketplace slot ");
                print(".. And so on ..");
                readerState = ClientConstants.SEND_MARKET_SLOT;
                break;
            case "2":
                gameController.notifyCouncilPalace(actionConstructor);
                break;
            case "4":
                print("Select action space: ");
                print("1 - Single action space");
                print("2 - Multiple action space");
                readerState  = ClientConstants.SEND_HARVEST_ACTION_SPACE;
                break;
            case "5":
                print("Select action space: ");
                print("1 - Single action space");
                print("2 - Multiple action space");
                readerState = ClientConstants.SEND_PRODUCTION_ACTION_SPACE;
                break;
            default:
                gameController.notifyInvalidInput();
        }
    }
    /**
     * Move handler.
     *
     * @param string the string
     */
    private void moveHandler(String string) {
        switch (moveState) {
            case 0:  //First possible case
                print("Select your available family member: ");
                moveState=ClientConstants.SEND_FAMILY_MEMBER;
                break;
            case ClientConstants.SEND_FAMILY_MEMBER:
                actionConstructor= new ArrayList<String>();
                actionConstructor.add(string);
                print("How many servants do you want to pay to raise your selected family member action value?");
                moveState=ClientConstants.SEND_PAID_SERVANTS;
                break;
            case ClientConstants.SEND_PAID_SERVANTS:
                actionConstructor.add(string);
                print("What action do you want to perform?");
                print("1 - Take card");
                print("2 - Place into Council Palace");
                print("3 - Place into Marketplace slot");
                print("4 - Harvest");
                print("5 - Product");
                moveState=ClientConstants.SEND_CHOSEN_ACTION;
                break;
            case ClientConstants.SEND_CHOSEN_ACTION:
                actionConstructor.add(string);
                distinguishAction(actionConstructor.get(2),string);
                break;
        }
    }

    @Override
    public void invalidInput() {
        printImp("Invalid Input");
    }

    @Override
    public void askPersonalBonusTile(List<PersonalBonusTile> personalBonusTiles) {}

    @Override
    public void assignColor(String color) {
        print("You were assigned the color: "+color);
        gameController.setPlayerColor(color);
    }

    @Override
    public void refreshBoard(Board board) {
        print("Board status:");
        print(board.toString());
    }

    @Override
    public void notifyExcommunication() {
        print("God seems very offended by your behaviour, he established your excommunication.");
    }

    @Override
    public void newChatMessage(String message) {
        print(message);
    }

    @Override
    public void askNameAndPassword() {
        actionConstructor.clear();
        print("Insert your name: ");
        readerState = ClientConstants.SEND_NAME;
    }

    @Override
    public void askForProductionExchangedEffect(List<String[]> choices) {
        for(int i = 0; i < choices.size(); i++){
            print("You can choose one of these production effects from \"" + choices.get(i)[1] + "\" card (please enter the choices separated by a comma, e.g 1,2,1,1) :");
            print("1 - " + choices.get(i)[2]);
            print("2 - " + choices.get(i)[3]);
            readerState = ClientConstants.SEND_PRODUCTION_CHOICES;
        }
    }

    @Override
    public void notifyRoundTimerExpired() {
        print("Your time to move is elapsed, you have lost the turn.");
    }

    @Override
    public void askForExcommunicationPayment(String excommunicationEffect) {
        print("Do you accept the following excommunication effect?\n");
        print(excommunicationEffect);
        print("\n1 - No, I want to pay the faith points");
        print("2 - Yes, I accept the excommunication");
        readerState = ClientConstants.SEND_EXCOMMUNICATION_PAYMENT_CHOICE;
    }

    @Override
    public void opponentStatusChanged(Player maskedPlayer) {
        print("The ");
        print(maskedPlayer.getColor());
        print(" has changed his status :\nNew details: ");
        print(maskedPlayer.getResourceChest().toString());
    }

    @Override
    public void askFinishRoundOrDiscard() {
        print("What do you want to do next?");
        print("- type \"end\" to end the turn");
        print("- type \"discard\" to discard a leader card, get a privilege and end your turn");
        readerState = ClientConstants.SEND_MOVE;
    }

    @Override
    public void actionCommandNotValid(String reason) {
        print("Your action is invalid!");
        print(reason);
    }

    @Override
    public void notifyServerClosed() {
        print("The server has closed the game");
    }

    @Override
    public void authenticatedCorrectly(String username) {
        print("You have been authenticated correctly, get ready to play!");
    }

    public void displayWrongPasswordMessage(String username) {
        print("Wrong password!");
    }


    @Override
    public void displayPlayerDisconnected(String color) {
        printImp(color + "player has disconnected");
    }

    @Override
    public void askSatanMove() {
        print("Choose a player to punish by typing his color: ");
        readerState = ClientConstants.SEND_PUNISHED_PLAYER;
    }

    @Override
    public void displaySatanAction(String color) {
        printImp("Satan has chosen the " + color + "player to punish");
    }

    @Override
    public void requestReconnection() {
        print("Would you join an existing Match? (y/n)\n");
        readerState = ClientConstants.SEND_REQUEST_RECONNECTION;
    }

    @Override
    public void notify(String input) {
        switch (readerState) {
            case ClientConstants.SEND_NAME:
                actionConstructor.add(input);
                print("Insert your password: ");
                readerState = ClientConstants.SEND_PASSWORD;
                break;
            case ClientConstants.SEND_CHOSEN_LEADERCARD:
                if(checkLeaderCardInput(input, leaderCardsLocalCopy))
                    gameController.notifyChosenLeaderCard(input);
                else{
                    print("Wrong input!");
                    print("Please insert the name again: ");
                    readerState = ClientConstants.SEND_CHOSEN_LEADERCARD; //This may be redundant but to be sure i'm going to have it
                }
                break;
            case ClientConstants.SEND_MOVE:
                if(input.toLowerCase().equals("discard")){
                    print("Select a leader card to discard (insert its name): ");
                    readerState = ClientConstants.SEND_DISCARDED_LEADER_CARD;
                } else if(input.toLowerCase().equals("end")){
                    gameController.notifyFinishRound();
                } else if(input.toLowerCase().equals("activate")){
                    print("Select the leader card you own by typing its name");
                    readerState = ClientConstants.SEND_ACTIVATED_LEADER_CARD;
                }
                else
                    moveHandler(input);
                break;
            case ClientConstants.SEND_PASSWORD:
                actionConstructor.add(input);
                gameController.notifyCredentials(actionConstructor);
                break;
            case ClientConstants.SEND_PRIVILEGE_CHOICES:
                gameController.notifyChosenPrivileges(input);
                break;
            case ClientConstants.SEND_DISCARDED_LEADER_CARD:
                gameController.notifyDiscardedLeaderCard(input);
                break;
            case ClientConstants.SEND_MARKET_SLOT:
                actionConstructor.add(input);
                gameController.notifyMarket(actionConstructor);
                break;
            case ClientConstants.SEND_HARVEST_ACTION_SPACE:
                actionConstructor.add(input);
                gameController.notifyHarvest(actionConstructor);
                break;
            case ClientConstants.SEND_PRODUCTION_ACTION_SPACE:
                actionConstructor.add(input);
                gameController.notifyProduction(actionConstructor);
                break;
            case ClientConstants.SEND_EXCOMMUNICATION_PAYMENT_CHOICE:
                if(input.equals("1"))
                    gameController.notifyExcommunicationEffectChoice(true);
                else if (input.equals("2"))
                    gameController.notifyExcommunicationEffectChoice(false);
                else{
                    gameController.notifyExcommunicationEffectChoice(false);  //Default choice: false
                }
                break;
            case ClientConstants.SEND_PRODUCTION_CHOICES:
                gameController.notifyProductionChoices(input);
                break;
            case ClientConstants.SEND_ACTIVATED_LEADER_CARD:
                gameController.notifyLeaderEffectActivation(input);
            case ClientConstants.SEND_PUNISHED_PLAYER:
                if(checkColorInput(input, possiblePlayerColors)){
                    gameController.notifySatanChoice(input);
                }else{
                    print("Wrong input!");
                    print("Type it again: ");
                    readerState = ClientConstants.SEND_PUNISHED_PLAYER;
                }
                break;
            case ClientConstants.SEND_REQUEST_RECONNECTION:
                if(input.equals("y")){
                    actionConstructor.add(input);
                    print("Insert your name: ");
                    readerState = ClientConstants.SEND_RECONNECTION_NAME;
                }else if(input.equals("n")){
                    gameController.notifyReconnectionRequest(input, null, null);
                }else{
                    print("Wrong input!");
                    print("Would you join an existing Match? (y/n)\n");
                    readerState = ClientConstants.SEND_PUNISHED_PLAYER; //This may be useless, but to avoid any mistake i prefer to have it
                }
                break;
            case ClientConstants.SEND_RECONNECTION_NAME:
                actionConstructor.add(input);
                print("Insert your password: ");
                readerState = ClientConstants.SEND_RECONNECTION_PASSWORD;
                break;
            case ClientConstants.SEND_RECONNECTION_PASSWORD:
                actionConstructor.add(input);
                gameController.notifyReconnectionRequest(actionConstructor.get(0), actionConstructor.get(1), actionConstructor.get(2)); //Instead of "actionConstructor.get("2")"
                //I could have passed directly input
                break;
            default:
                print("Command not recognized");
                break;
        }
    }

    private void print(String s){
        System.out.println(s);
    }

    private void printImp(String s) {
        System.out.println("¤¤¤  " + s.toUpperCase() + "  ¤¤¤");
    }
}
