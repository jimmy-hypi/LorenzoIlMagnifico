package command.toclient;

import client.ClientCommandHandler;

/**
 * the command used to notify the excommunication status to the client
 */
public class NotifyExcommunicationCommand extends ServerToClientCommand{
	private static final long serialVersionUID = 2543318254054111680L;

	@Override
	public void processCommand(ClientCommandHandler clientCommandHandler) {
		clientCommandHandler.applyCommand(this);
	}
}
