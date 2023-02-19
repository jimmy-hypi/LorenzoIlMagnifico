package command.toclient;

import client.ClientCommandHandler;

/**
 * the command used to notify the client of an invalid action
 */
public class InvalidActionCommand extends ServerToClientCommand {

	private static final long serialVersionUID = -6304179008470498378L;
	private String invalidCode;

	public InvalidActionCommand(){
		this.invalidCode="";
	
	}

	public InvalidActionCommand(String notApplicableCode) {
		this.invalidCode=notApplicableCode;
	}

	@Override
	public void processCommand(ClientCommandHandler clientCommandHandler) {
		clientCommandHandler.applyCommand(this);
		
	}

	public String getInvalidCode(){
		return invalidCode;
	}
	
}
