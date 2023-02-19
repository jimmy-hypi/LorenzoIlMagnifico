package command.toclient;

import client.ClientCommandHandler;

/**
 * This command asks the client if they want to end the turn or discard a leader card
 */
public class AskFinishRoundOrDiscardCommand extends ServerToClientCommand {
    private static final long serialVersionUID = 5372505115142988012L;

    @Override
    public void processCommand(ClientCommandHandler clientCommandHandler) {
        clientCommandHandler.applyCommand(this);
    }
}
