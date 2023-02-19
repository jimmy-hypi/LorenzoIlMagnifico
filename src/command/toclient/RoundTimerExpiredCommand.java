package command.toclient;

import client.ClientCommandHandler;

/**
 * This class notifies the client that his time to move has expired
 */
public class RoundTimerExpiredCommand extends ServerToClientCommand {

	private static final long serialVersionUID = 7488490467132469474L;

	@Override
	public void processCommand(ClientCommandHandler clientCommandHandler) {
		clientCommandHandler.applyCommand(this);
	}
}
