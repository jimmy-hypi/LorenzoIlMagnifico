package view;

import model.Period;
import model.PersonalBonusTile;
import model.Player;
import model.area.Board;
import model.card.LeaderCard;
import model.resource.ResourceChest;

import java.util.ArrayList;
import java.util.List;

/**
 * The Interface UserInterface to be implemented by the different type of views
 */
public interface UserInterface {
    public void startDraft(ArrayList<LeaderCard> leaderCards);
    public void initializeMatch(int numPlayers);
    public void initializeTurn(Period period, int turn);
    public void startTurn();
    public void commandNotValid();
    public void playerStatusChange(Player p);
    public void playerMove();
    public void playerTurn();
    public void win();
    public void lose();
    public void askPrivilegeChoice(int numberOfPrivilege, List<ResourceChest> privilegeResources);
    public void askMove();
    public void invalidInput();
    public void askPersonalBonusTile(List<PersonalBonusTile> personalBonusTiles);
    public void assignColor(String color);
    public void refreshBoard(Board board);
    public void notifyExcommunication();
    public void newChatMessage(String message);
    public void askNameAndPassword();
    public void askForProductionExchangedEffect(List<String[]> choices);
    public void notifyRoundTimerExpired();
    public void askForExcommunicationPayment(String excommunicationEffect);
    public void opponentStatusChanged(Player maskedPlayer);
    public void askFinishRoundOrDiscard();
    public void actionCommandNotValid(String reason);
    public void notifyServerClosed();
    public void authenticatedCorrectly(String username);
    public void displayWrongPasswordMessage(String username);
    public void displayPlayerDisconnected(String color);
    public void askSatanMove();
    public void displaySatanAction(String color);
    public void requestReconnection();
}
