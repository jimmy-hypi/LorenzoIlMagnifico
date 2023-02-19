package command.toserver;

import server.ServerCommandHandler;

/**
 * this command manages the possibility to discard a leader card during the game.
 */
public class DiscardLeaderCardCommand extends ClientToServerCommand{
	private static final long serialVersionUID = 59970063367278274L;
	private String leaderName;

	public DiscardLeaderCardCommand(String leaderName){
		this.leaderName = leaderName;
	}

	@Override
	public void processCommand(ServerCommandHandler serverHandlerCommand) {
		serverHandlerCommand.applyCommand(this);
	}

	public String getLeaderName() {
		return leaderName;
	}
}
