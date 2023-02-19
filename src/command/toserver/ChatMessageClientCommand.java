package command.toserver;

import server.ServerCommandHandler;

/**
 * This command is used to notify the Server of the Client's chat message
 */
public class ChatMessageClientCommand extends ClientToServerCommand {
	private static final long serialVersionUID = 2602059606073679275L;
	private String message;

	public ChatMessageClientCommand(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public void processCommand(ServerCommandHandler serverHandlerCommand) {
		serverHandlerCommand.applyCommand(this);
	}

}
