package command.toserver;

import server.ClientHandler;
import server.ServerCommandHandler;

/**
 * The class to notify the server about the client's move
 */
public class PlayerMoveCommand extends ClientToServerCommand {
	private static final long serialVersionUID = 667330673864025398L;
	private String move;

	public PlayerMoveCommand(String move){
		this.move = move;
	}

	public String getMove(){
		return move;
	}

	@Override
	public void processCommand(ServerCommandHandler serverHandlerCommand) {
		System.out.println("PlayerMoveCommand: you should not be here");
	}

	public void processCommand(ServerCommandHandler serverHandlerCommand, ClientHandler clientHandler) {
		serverHandlerCommand.applyCommand(this,clientHandler);
	}
}
