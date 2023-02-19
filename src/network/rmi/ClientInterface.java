package network.rmi;

import command.toclient.ServerToClientCommand;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * RMI client interface.
 */
public interface ClientInterface extends Remote {
    public void notifyClient(ServerToClientCommand command) throws RemoteException;
    public void closeConnection() throws RemoteException;
}
