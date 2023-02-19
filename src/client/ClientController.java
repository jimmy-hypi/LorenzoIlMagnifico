package client;

import command.toserver.*;
import model.card.CardType;
import network.NetworkInterface;
import view.InputObserver;
import view.UserInterface;

import java.util.ArrayList;

/**
 * This class represents the client controller that takes care of sending through the network all
 * the commands requested by the player
 */
public class ClientController implements InputObserver {

    private UserInterface userInterface;
    private NetworkInterface networkInterface;
    private ClientCommandHandler commandHandler;
    private String playerColor;

    public ClientController(NetworkInterface networkInterface) {
        this.networkInterface = networkInterface;
    }

    public void sendCommand(ClientToServerCommand command) {
        try {
            networkInterface.sendCommand(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void notifyChosenLeaderCard(String leaderCardName){
        sendCommand(new ChosenLeaderCardCommand(leaderCardName, playerColor));
    }

    @Override
    public void notifyMove(String move) {
        sendCommand(new PlayerMoveCommand(move));
    }
    @Override
    public void notifyChosenPrivileges(String choices){
        ArrayList<Integer> commandConstructor = parseString(choices);
        if (commandConstructor.size() != 0)
            sendCommand(new ChosenPrivilegeCommand(commandConstructor));
        else {
            userInterface.invalidInput();
            notifyInvalidInput();
        }
    }
    public void setCommandHandler(ClientCommandHandler handler) {
        this.commandHandler = handler;
    }

    public void setUserInterface(UserInterface userInterface){
        this.userInterface = userInterface;
    }
    public void notifyInvalidInput() {
        sendCommand(new InvalidInputCommand());
    }
    public void notifyCouncilPalace(ArrayList<String> actionConstructor) {
        sendCommand(new PlaceIntoCouncilPalaceCommand(actionConstructor.get(0), Integer.parseInt(actionConstructor.get(1))));
    }
    public void notifyTakeCardAction(ArrayList<String> actionConstructor){
        try {
            TakeCardCommand takeCardCommand = new TakeCardCommand(actionConstructor.get(0),
                    Integer.parseInt(actionConstructor.get(4)), Integer.parseInt(actionConstructor.get(1)),
                    CardType.values()[Integer.parseInt(actionConstructor.get(3)) - 1]);
            sendCommand(takeCardCommand);
        } catch (IndexOutOfBoundsException e) {}
    }

    @Override
    public void notifyMarket(ArrayList<String> actionConstructor) {
        try {
            PlaceIntoMarketCommand placeIntoMarketCommand = new PlaceIntoMarketCommand(actionConstructor.get(0),
                    actionConstructor.get(3), Integer.parseInt(actionConstructor.get(1)));
            sendCommand(placeIntoMarketCommand);
        } catch (IndexOutOfBoundsException e) {
        }
    }

    @Override
    public void notifyHarvest(ArrayList<String> actionConstructor) {
        try {
            HarvestCommand harvestCommand = new HarvestCommand(actionConstructor.get(0),
                    Integer.parseInt(actionConstructor.get(1)), Integer.parseInt(actionConstructor.get(3)));
            sendCommand(harvestCommand);
        } catch (IndexOutOfBoundsException e) {
        }
    }

    @Override
    public void notifyProduction(ArrayList<String> actionConstructor) {
        try {
            ProductionCommand productionCommand = new ProductionCommand(actionConstructor.get(0),
                    Integer.parseInt(actionConstructor.get(1)), Integer.parseInt(actionConstructor.get(3)));
            sendCommand(productionCommand);
        } catch (IndexOutOfBoundsException e) {
        }
    }

    @Override
    public void notifyDiscardedLeaderCard(String discardedLeaderCard) {
        sendCommand(new DiscardLeaderCardCommand(discardedLeaderCard));

    }

    public void setPlayerColor(String color) {
        this.playerColor = color;
    }

    @Override
    public void notifyCredentials(ArrayList<String> actionConstructor) {
        sendCommand(new SendCredentialsCommand(actionConstructor.get(0), actionConstructor.get(1), playerColor));
    }

    @Override
    public void notifyFinishRound() {
        sendCommand(new FinishRoundCommand());
    }

    @Override
    public void notifyExcommunicationEffectChoice(Boolean choice) {
        sendCommand(new ChurchSupportCommand(playerColor, choice));
    }

    @Override
    public void notifyProductionChoices(String choices) {
        ArrayList<Integer> commandConstructor = parseString(choices);
        if (commandConstructor.size() != 0)
            sendCommand(new ProductionActivationCommand(commandConstructor));
        else {
            userInterface.invalidInput();
            notifyInvalidInput();
        }
    }

    @Override
    public void notifyProductionChoices(ArrayList<Integer> choices) {
        sendCommand(new ProductionActivationCommand(choices));
    }

    private ArrayList<Integer> parseString(String choices) {
        char[] charArray = choices.toCharArray();
        for (int i = 0; i < choices.length(); i++) {
        }
        ArrayList<Integer> commandConstructor = new ArrayList<Integer>();
        for (int i = 0; i < charArray.length; i += 2) {
            if (Character.getNumericValue(charArray[i]) != -1)
                commandConstructor.add(Character.getNumericValue(charArray[i]));
            else {
                commandConstructor.clear();
                break;
            }
        }
        return commandConstructor;
    }
    public void notifyLeaderEffectActivation(String leaderCardName) {
        sendCommand(new ActivateLeaderCardCommand(leaderCardName, playerColor));

    }

    @Override
    public void notifyChatMessage(String message) {
        String m = "<--" + playerColor + "--> " + message;
        sendCommand(new ChatMessageClientCommand(m));
    }
    public void notifyRequestClosureCommand() {
        sendCommand(new RequestClosureCommand(playerColor));
    }

    public void notifyAuthenticationRequest(String username, String password) {
        sendCommand(new SendCredentialsCommand(username, password, playerColor));

    }

    public void notifySatanChoice(String playerColor) {
        sendCommand(new SatanChoiceCommand(playerColor));
    }

    @Override
    public void notifyReconnectionRequest(String response, String username, String password){
        sendCommand(new ReconnectionAnswerCommand(response, username, password));
    }

}
