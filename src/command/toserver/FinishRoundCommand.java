package command.toserver;

import server.ServerCommandHandler;

/**
 * This class alerts the server about the end of a turn
 */
public class FinishRoundCommand extends ClientToServerCommand {
	private static final long serialVersionUID = -8641006857842951634L;

	@Override
	public void processCommand(ServerCommandHandler serverHandlerCommand) {
			serverHandlerCommand.applyCommand(this);
	}
}
