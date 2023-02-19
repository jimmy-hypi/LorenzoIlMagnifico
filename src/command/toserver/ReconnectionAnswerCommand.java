package command.toserver;

import server.ServerCommandHandler;

/**
 * Reconnection answer from the client:
 * provides answer, username, password
 */
public class ReconnectionAnswerCommand extends ClientToServerCommand{

	private static final long serialVersionUID = 7040327041116530057L;
	
	private String answer;
	private String username;
	private String password;

	@Override
	public void processCommand(ServerCommandHandler serverHandlerCommand) {
		serverHandlerCommand.applyCommand(this);
		
	}
	
	public ReconnectionAnswerCommand(String answer, String username, String password){
		this.answer = answer;
		this.username = username;
		this.password = password;
	}

	public String getAnswer() {
		return answer;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
