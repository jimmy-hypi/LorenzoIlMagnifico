package command.toserver;

import server.ServerCommandHandler;

/**
 * This class is made to alert the server about an invalid action made by the client
 */
public class InvalidInputCommand extends ClientToServerCommand {
	private static final long serialVersionUID = -4017180267188280820L;

	@Override
	public void processCommand(ServerCommandHandler serverHandlerCommand) {
		serverHandlerCommand.applyCommand(this);
	}
}
