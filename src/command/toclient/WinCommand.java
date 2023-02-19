package command.toclient;

import client.ClientCommandHandler;

/**
 * This command is used to notify the winner of the game
 */
public class WinCommand extends ServerToClientCommand {
	private static final long serialVersionUID = 2918136961631454765L;

	@Override
	public void processCommand(ClientCommandHandler clientCommandHandler) {
		clientCommandHandler.applyCommand(this);
	}
}
