package command.toclient;

import client.ClientCommandHandler;

public class NotifySatanActionCommand extends ServerToClientCommand{
	private static final long serialVersionUID = -1233194644751446601L;
	private String color;

	@Override
	public void processCommand(ClientCommandHandler clientCommandHandler) {
		clientCommandHandler.applyCommand(this);
		
	}
	
	public NotifySatanActionCommand(String color){
		this.color = color;
	}
	
	public String getColor(){
		return this.color;
	}
	

}
