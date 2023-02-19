package command.toclient;

import client.ClientCommandHandler;

/**
 * the command used to notify that the turn has started
 */
public class StartTurnCommand extends ServerToClientCommand {
	private static final long serialVersionUID = 6276314088628114292L;

	@Override
	public void processCommand(ClientCommandHandler clientCommandHandler) {
		clientCommandHandler.applyCommand(this);
	}
}
