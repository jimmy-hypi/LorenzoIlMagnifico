package command.toclient;

import client.ClientCommandHandler;
import command.toclient.ServerToClientCommand;

/**
 * The command is used to notify the client that a match has been intialized
 */
public class InitializeMatchCommand extends ServerToClientCommand {

	int numPlayers;
	private static final long serialVersionUID = -5791396313072706548L;

	public InitializeMatchCommand(int numPlayers) {
		this.numPlayers=numPlayers;
	}

	@Override
	public void processCommand(ClientCommandHandler clientCommandHandler) {
		clientCommandHandler.applyCommand(this);
	}

	public int getNumPlayers() {
		return numPlayers;
	}

}
