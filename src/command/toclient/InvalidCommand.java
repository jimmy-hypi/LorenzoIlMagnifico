package command.toclient;

import client.ClientCommandHandler;

/**
 * the command used to notify the client that they sent am invalid command
 */
public class InvalidCommand extends ServerToClientCommand{

	private static final long serialVersionUID = -7556587540722322509L;

	@Override
	public void processCommand(ClientCommandHandler clientCommandHandler) {
		clientCommandHandler.applyCommand(this);
	}
}
