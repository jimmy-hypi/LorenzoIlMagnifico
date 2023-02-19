package client;

import command.toclient.ServerToClientCommand;

/**
 * An asynchronous update interface for receiving client notifications
 */
public interface ServerToClientCommandObserver {
    public void notifyNewCommand(ServerToClientCommand serverToClientCommand);
}
