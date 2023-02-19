package network;

import client.ClientCommandHandler;
import command.toclient.ServerToClientCommand;
import command.toserver.ClientToServerCommand;

/**
 * The Interface that abstracts common networking operations.
 * Implementors will use different technologies but offer the same API
 */
public interface NetworkInterface {
    void connect() throws Exception;
    void sendCommand(ClientToServerCommand command) throws Exception;
    void notifyClient(ServerToClientCommand command);
    void closeConnection();
    void addCommandObserver(ClientCommandHandler handler);
}
