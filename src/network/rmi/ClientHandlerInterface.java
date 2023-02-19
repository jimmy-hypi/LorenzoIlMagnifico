package network.rmi;

import command.toserver.ClientToServerCommand;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * RMI interface extension for Client Handler
 */
public interface ClientHandlerInterface extends Remote {
    void addClient(int port) throws RemoteException;
    void notifyServer(ClientToServerCommand command) throws RemoteException;
    void closedByServer() throws RemoteException;
}
