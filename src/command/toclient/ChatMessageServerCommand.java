package command.toclient;

import client.ClientCommandHandler;

/**
 * The Server sends the Client chat messages
 */
public class ChatMessageServerCommand extends ServerToClientCommand {
	
	private static final long serialVersionUID = -6099362758469332623L;
	private String text;

	public ChatMessageServerCommand(String text){
		this.text = text;
	}

	@Override
	public void processCommand(ClientCommandHandler clientCommandHandler) {
		clientCommandHandler.applyCommand(this);

	}
	public String getText() {
		return text;
	}

}
