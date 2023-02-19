package command.toclient;

import client.ClientCommandHandler;

/**
 * This command asks the user to send his credentials to log into the game before the match starts
 */
public class AskAuthenticationCommand extends ServerToClientCommand {
    private static final long serialVersionUID = -2642328618210688214L;

    @Override
    public void processCommand(ClientCommandHandler clientCommandHandler) {
        clientCommandHandler.applyCommand(this);
    }
}
