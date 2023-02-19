package command.toclient;

import client.ClientCommandHandler;

/**
 * Signals the user that the password they inserted was not correct
 */
public class WrongPasswordCommand extends ServerToClientCommand {
	private static final long serialVersionUID = -4210904855925108874L;
	private String username;

	public WrongPasswordCommand(String username) {
		this.username=username;
	}

	@Override
	public void processCommand(ClientCommandHandler clientCommandHandler) {
		clientCommandHandler.applyCommand(this);
	}

	public String getUsername() {
		return username;
	}
}
