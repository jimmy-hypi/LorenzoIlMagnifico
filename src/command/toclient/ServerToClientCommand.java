package command.toclient;

import client.ClientCommandHandler;

import java.io.Serializable;

/**
 * ServerToClientCommand abstract class:
 * It represents a generic command from server to client.
 */
public abstract class ServerToClientCommand implements Serializable {
    private static final long serialVersionUID = -6460847901998831472L;

    public abstract void processCommand(ClientCommandHandler clientCommandHandler);
}
