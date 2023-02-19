package command.toclient;

import client.ClientCommandHandler;
import command.toclient.ServerToClientCommand;

/**
 * This command is used to notify that the server decides to remove a client from the match.
 */
public class CloseClientCommand extends ServerToClientCommand {

	private static final long serialVersionUID = 1L;

	@Override
	public void processCommand(ClientCommandHandler clientCommandHandler) {
		clientCommandHandler.applyCommand(this);
		
	}
		
}
