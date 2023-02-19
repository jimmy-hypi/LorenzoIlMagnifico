package view;

import java.util.ArrayList;

/**
 * An asynchronous update interface for receiving notifications
 * about Input information as the Input is constructed.
 */
public interface InputObserver {
    /*
     * These methods are called when information about an Input
     * which was previously requested using an asynchronous interface becomes available
     */
    public void notifyChosenLeaderCard(String LeaderCardName);

    public void notifyMove(String move);

    public void notifyChosenPrivileges(String choices);

    public void notifyInvalidInput();

    public void notifyCouncilPalace(ArrayList<String> actionConstructor);

    public void notifyTakeCardAction(ArrayList<String> actionConstructor);

    public void notifyDiscardedLeaderCard(String discardedLeaderCard);

    public void notifyMarket(ArrayList<String> actionConstructor);

    public void notifyHarvest(ArrayList<String> actionConstructor);

    public void notifyProduction(ArrayList<String> actionConstructor);

    public void notifyCredentials(ArrayList<String> actionConstructor);

    public void notifyFinishRound();

    public void notifyExcommunicationEffectChoice(Boolean string);

    public void notifyProductionChoices(String choices);

    public void notifyProductionChoices(ArrayList<Integer> choices);

    public void notifyChatMessage(String message);

    /**
     * This method is called when a client requests to be disconnected
     *
     * @param response
     * @param username
     * @param password
     */
    public void notifyReconnectionRequest(String response, String username, String password);

}
