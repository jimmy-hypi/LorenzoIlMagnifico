package command.toserver;

import server.ServerCommandHandler;

/**
 * Command that notifies the Satan's choice
 */
public class SatanChoiceCommand extends ClientToServerCommand{
	private static final long serialVersionUID = 2239627322643830186L;
	private String color;

	@Override
	public void processCommand(ServerCommandHandler serverHandlerCommand) {
		serverHandlerCommand.applyCommand(this);
		
	}
	
	public SatanChoiceCommand(String color){
		this.color = color;
	}
	
	public String getColor(){
		return color;
	}
}
