package command.toserver;

import server.ServerCommandHandler;

/**
 * This class represents the message with client credentials received by server
 */
public class SendCredentialsCommand extends ClientToServerCommand {
	private static final long serialVersionUID = -643151000952056736L;
	private String username;
	private String password;
	private String playerColor;

	public SendCredentialsCommand(String username, String password, String playerColor) {
		this.username = username;
		this.password = password;
		this.playerColor = playerColor;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getPlayerColor() {
		return playerColor;
	}

	@Override
	public void processCommand(ServerCommandHandler serverHandlerCommand) {
		serverHandlerCommand.applyCommand(this);
	}
}
