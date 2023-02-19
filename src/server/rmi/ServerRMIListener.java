package server.rmi;

import constant.NetworkConstants;
import network.rmi.ClientHandlerInterface;
import network.rmi.ClientHandlerInterfaceImpl;
import server.ClientHandler;
import server.Server;
import server.ServerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Wrapper around the RMI connection layer:
 * Binds ClientHandlers to RMI connections to allow remote method invocation
 */
public class ServerRMIListener implements Runnable {

    private static int id = 0;
    private ClientHandlerInterface clientHandler;
    private Registry registry;
    private String name;
    private ServerInterface creator;

    public ServerRMIListener(Server server) {
        name = "ClientHandler";
        creator = server;
    }

    @Override
    public void run() {
        createClientHandler();
    }

    private void createClientHandler() {
        try {
            clientHandler = new ClientHandlerInterfaceImpl(this, id);
            ClientHandlerInterface stub = (ClientHandlerInterface) UnicastRemoteObject.exportObject(clientHandler, 0);
            registry = LocateRegistry.createRegistry(NetworkConstants.RMICLIENTHANDLERPORT);
            registry.bind(name, stub);
            id++;
        } catch (Exception e) {
            closeListener();
        }
    }
    public synchronized void closeListener() {
        try {
            registry.unbind(name);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        registry = null;
    }

    public void addClient(ClientHandlerInterfaceImpl clientHandler){
        creator.addClient(clientHandler);
        try {
            clientHandler = new ClientHandlerInterfaceImpl(this, id);
            ClientHandlerInterface stub = (ClientHandlerInterface) UnicastRemoteObject.exportObject(clientHandler, 0);
            registry.rebind(name, stub);
            id++;
        } catch(RemoteException e){
            e.printStackTrace();
        }
    }

    public void removeWaitingClient(ClientHandler clientHandler) {
        creator.removeClient(clientHandler);
    }

    public void endListening(){
        try {
            registry.unbind(name);
        } catch (RemoteException | NotBoundException e) {}
        registry = null;
    }
}
