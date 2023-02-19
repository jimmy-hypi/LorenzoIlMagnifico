package command.toserver;

import java.io.Serializable;

import server.ServerCommandHandler;

/**
 * This abstract class represents a generic command that a client needs to submit to the server
 */
public abstract class ClientToServerCommand implements Serializable{
	private static final long serialVersionUID = -6514178874808077590L;

	public abstract void processCommand(ServerCommandHandler serverHandlerCommand);

}
