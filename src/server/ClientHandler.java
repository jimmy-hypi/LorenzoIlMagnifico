package server;

import command.toclient.ServerToClientCommand;
import model.Player;
import server.controller.MatchHandlerObserver;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * A runnable abstract messages client-server handler to be implemented by classes
 * using different connection types
 */
public abstract class ClientHandler implements Runnable {
    protected Player player;
    protected int code;
    protected boolean closed;

    public void addPlayer(Player player) {
        this.player = player;
    }
    public Player getPlayer() {
        return player;
    }

    public abstract void sendCommand(ServerToClientCommand command) throws IOException;

    public abstract void closedByServer() throws RemoteException;

    public abstract void closedByClient();

    public boolean isClosed() {
        return closed;
    }
    public abstract void addObserver(MatchHandlerObserver matchObserver);

    public abstract void addCommandObserver(ServerCommandHandler commandHandler);

    public abstract void addCommandObserver(Server server);

}
