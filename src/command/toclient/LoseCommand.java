package command.toclient;

import client.ClientCommandHandler;

/**
 * the command to notify a client that they aren't the winner of the match
 */
public class LoseCommand extends ServerToClientCommand {
	private static final long serialVersionUID = -4757146999670162579L;

	@Override
	public void processCommand(ClientCommandHandler clientCommandHandler) {
		clientCommandHandler.applyCommand(this);
	}
}
