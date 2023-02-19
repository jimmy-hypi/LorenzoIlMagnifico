package command.toclient;

import client.ClientCommandHandler;

/**
 * The command to ask the player about the move they want to play.
 */
public class AskMoveCommand extends ServerToClientCommand {
    private static final long serialVersionUID = -6651993894417335857L;

    @Override
    public void processCommand(ClientCommandHandler clientCommandHandler) {
        clientCommandHandler.applyCommand(this);
    }

}
