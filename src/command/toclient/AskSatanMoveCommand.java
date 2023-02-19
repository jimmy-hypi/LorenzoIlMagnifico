package command.toclient;

import client.ClientCommandHandler;

/**
 * Fifth player extension:
 * Ask the player representing Satan for their choice
 */
public class AskSatanMoveCommand extends ServerToClientCommand {
    private static final long serialVersionUID = -1303484145043675961L;

    @Override
    public void processCommand(ClientCommandHandler clientCommandHandler) {
        clientCommandHandler.applyCommand(this);
    }
}
