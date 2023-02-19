package command.toclient;

import client.ClientCommandHandler;

/**
 * Command to ask for player reconnection
 */
public class AskForReconnectionCommand extends ServerToClientCommand {
    private static final long serialVersionUID = 3161152521045309148L;

    @Override
    public void processCommand(ClientCommandHandler clientCommandHandler) {
        try {
            clientCommandHandler.applyCommand(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
