package network.rmi;

import client.ClientCommandHandler;
import client.ServerToClientCommandObserver;
import command.toclient.ServerToClientCommand;
import command.toserver.ClientToServerCommand;
import constant.NetworkConstants;
import network.NetworkInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class connects the client and the server using RMI. Implementing a direct ClientInterfaceImpl
 * class would have made the close() method a little difficult so I (Jimmy) merged the clientImplementation
 * with the network interface
 */
public class ClientRMIInterface implements ClientInterface, NetworkInterface {

    private String clientHandlerName;
    private String clientName;
    private Registry clientHandlerRegistry;
    private Registry clientRegistry;
    private ClientHandlerInterface clientHandler;
    private ClientInterface client;
    private ServerToClientCommandObserver observer;

    public ClientRMIInterface() {
        this.clientHandlerName = "ClientHandler";
        this.clientName = "Client";
        this.client = this;
    }
    @Override
    public void connect() throws Exception {
        clientHandlerRegistry = LocateRegistry.getRegistry(NetworkConstants.RMICLIENTHANDLERPORT);
        clientHandler = (ClientHandlerInterface) clientHandlerRegistry.lookup(clientHandlerName);
        ClientInterface clientStub = (ClientInterface) UnicastRemoteObject.exportObject(client, 0);

        try {
            clientRegistry = LocateRegistry.getRegistry(NetworkConstants.RMICLIENTPORT);
            clientRegistry.rebind(clientName, clientStub);
        } catch (Exception e) {
            clientRegistry = LocateRegistry.createRegistry(NetworkConstants.RMICLIENTPORT);
            clientRegistry.bind(clientName, clientStub);
        }
        clientHandler.addClient(NetworkConstants.RMICLIENTPORT);
    }

    @Override
    public void sendCommand(ClientToServerCommand command) throws Exception {
        clientHandler.notifyServer(command);
    }

    @Override
    public void addCommandObserver(ClientCommandHandler handler) {
        this.observer = handler;
    }

    @Override
    public void notifyClient(ServerToClientCommand command) {
        this.observer.notifyNewCommand(command);
    }

    @Override
    public void closeConnection() {
        try {
            // We remove the client from the registry to close the connection
            clientRegistry.unbind(clientName);
            clientRegistry = null;
        } catch (NullPointerException | RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}
