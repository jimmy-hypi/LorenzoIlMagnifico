package command.toserver;

import command.toserver.ClientToServerCommand;
import exception.NotApplicableException;
import server.ServerCommandHandler;

/**
 * Command to notify the server about the client's intention to activate a leader effect
 */
public class ActivateLeaderCardCommand extends ClientToServerCommand {
	private static final long serialVersionUID = -5734000768457689241L;
	private String leaderName;
	private String player;

	public ActivateLeaderCardCommand(String leaderName, String player){
		this.leaderName = leaderName;
		this.player = player;
	}

	@Override
	public void processCommand(ServerCommandHandler serverHandlerCommand) {
		try {
			serverHandlerCommand.applyCommand(this);
		} catch (NotApplicableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getLeaderName() {
		return leaderName;
	}

	public String getPlayer(){
		return player;
	}
	

}
