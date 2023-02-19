package command.toserver;

import server.ServerCommandHandler;

/**
 * this command represents the Client's answer about the possible decision to pay to prevent excommunication
 * true if you want to support, false otherwise
 */
public class ChurchSupportCommand extends ClientToServerCommand{

	private static final long serialVersionUID = 2379068517465682578L;
	private boolean decision;
	private String playerColor;

	public ChurchSupportCommand(String playerColor, boolean decision){
		this.playerColor=playerColor;
		this.decision = decision;
	}

	@Override
	public void processCommand(ServerCommandHandler serverHandlerCommand) {
		serverHandlerCommand.applyCommand(this);
	}

	public boolean getDecision() {
		return decision;
	}

	public String getPlayerColor() {
		return playerColor;
	}
}
