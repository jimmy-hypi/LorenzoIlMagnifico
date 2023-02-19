package client;

import command.toclient.*;
import network.NetworkInterface;
import view.UserInterface;

/**
 * Concrete implementor of ServerToClientCommandObserver:
 * manages the messages from the server calling methods of the view and network interfaces
 * to update the user of the server message.
 */
public class ClientCommandHandler implements ServerToClientCommandObserver {
    private UserInterface userInterface;
    private NetworkInterface networkInterface;

    public ClientCommandHandler(UserInterface ui, NetworkInterface networkInterface) {
        this.userInterface = ui;
        this.networkInterface = networkInterface;
    }
    public void applyCommand(InvalidCommand invalidCommand) {
        userInterface.commandNotValid();
    }

    public void applyCommand(StartTurnCommand startTurnCommand) {
        userInterface.startTurn();

    }

    public void applyCommand(CloseClientCommand closeConnectionCommand) {
        userInterface.notifyServerClosed();
        networkInterface.closeConnection();

    }

    public void applyCommand(AskPrivilegeChoiceCommand askPrivilegeChoiceCommand) {
        userInterface.askPrivilegeChoice(askPrivilegeChoiceCommand.getNumberOfPrivilege(), askPrivilegeChoiceCommand.getPrivilegeResources());

    }

    public void applyCommand(InitializeMatchCommand initializeMatchCommand) {
        userInterface.initializeMatch(initializeMatchCommand.getNumPlayers());
    }

    public void applyCommand(WinCommand winCommand) {
        userInterface.win();

    }

    public void applyCommand(LoseCommand loseCommand) {
        userInterface.lose();

    }
    @Override
    public void notifyNewCommand(ServerToClientCommand serverToClientCommand) {
        serverToClientCommand.processCommand(this);
    }

    public void applyCommand(NotifyExcommunicationCommand notifyExcommunicationCommand) {
        userInterface.notifyExcommunication();

    }

    public void applyCommand(ChooseLeaderCardCommand chooseLeaderCardCommand) {
        userInterface.startDraft(chooseLeaderCardCommand.getPossibleChoices());
    }

    public void applyCommand(AskForExcommunicationPaymentCommand solveExcommunicationCommand) {
        userInterface.askForExcommunicationPayment(solveExcommunicationCommand.getExcommunicationEffect());
    }

    public void applyCommand(AskAuthenticationCommand askAuthenticationCommand) {
        userInterface.askNameAndPassword();
    }

    public void applyCommand(PlayerStatusChangeCommand playerStatusChangeCommand) {
        userInterface.playerStatusChange(playerStatusChangeCommand.getPlayer());
    }

    public void applyCommand(InitializeTurnCommand initializeTurnCommand){
        userInterface.initializeTurn( initializeTurnCommand.getPeriod(), initializeTurnCommand.getTurn());
    }

    public void applyCommand(OpponentStatusChangeCommand opponentStatusChangeCommand){
        userInterface.opponentStatusChanged(opponentStatusChangeCommand.getMaskedPlayer());
    }

    public void applyCommand(ChatMessageServerCommand chatMessageServerCommand) {
        userInterface.newChatMessage(chatMessageServerCommand.getText());

    }

    public void applyCommand(ChooseProductionExchangeEffectsCommand chooseProductionExchangeEffectsCommand) {
        userInterface.askForProductionExchangedEffect(chooseProductionExchangeEffectsCommand.getChoices());

    }

    public void applyCommand(RefreshBoardCommand refreshBoardCommand) {
        userInterface.refreshBoard(refreshBoardCommand.getBoard());

    }

    public void applyCommand(RoundTimerExpiredCommand roundTimerExpiredCommand) {
        userInterface.notifyRoundTimerExpired();

    }

    public void applyCommand(AssignColorCommand assignColorCommand) {
        userInterface.assignColor(assignColorCommand.getColor());
    }

    public void applyCommand(AskMoveCommand askMoveCommand) {
        userInterface.askMove();
    }

    public void applyCommand(AskFinishRoundOrDiscardCommand askFinishRoundOrDiscardCommand) {
        userInterface.askFinishRoundOrDiscard();
    }

    public void applyCommand(InvalidActionCommand command){
        userInterface.actionCommandNotValid(command.getInvalidCode());
    }

    public void applyCommand(AuthenticatedCorrectlyCommand authenticatedCorrectlyCommand) {
        userInterface.authenticatedCorrectly(authenticatedCorrectlyCommand.getUsername());
    }

    public void applyCommand(WrongPasswordCommand wrongPasswordCommand) {
        userInterface.displayWrongPasswordMessage(wrongPasswordCommand.getUsername());
    }

    public void applyCommand(PlayerDisconnectedCommand playerDisconnectedCommand) {
        userInterface.displayPlayerDisconnected(playerDisconnectedCommand.getColor());
    }

    public void applyCommand(NotifySatanActionCommand notifySatanAction) {
        userInterface.displaySatanAction(notifySatanAction.getColor());

    }

    public void applyCommand(AskSatanMoveCommand askSatanMove) {
        userInterface.askSatanMove();
    }

    public void applyCommand(AskForReconnectionCommand askForReconnectionCommand) throws Exception{
        userInterface.requestReconnection();

    }
}
