package network.rmi;

import command.toclient.CloseClientCommand;
import command.toclient.ServerToClientCommand;
import command.toserver.*;
import server.ClientHandler;
import server.Server;
import server.ServerCommandHandler;
import server.controller.MatchHandlerObserver;
import server.rmi.ServerRMIListener;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Implementation of the ClientHandler RMI interface
 */
public class ClientHandlerInterfaceImpl extends ClientHandler implements ClientHandlerInterface {
    private ServerRMIListener server;
    private ClientInterface client;
    private MatchHandlerObserver matchHandlerObserver;
    private ServerCommandHandler serverCommandHandler;
    private Server serverListener;

    public ClientHandlerInterfaceImpl(ServerRMIListener server, int id) {
        this.server = server;
        this.code = id;
        this.code = hashCode();
        this.closed = false;
        this.matchHandlerObserver = null;
    }

    @Override
    public void addClient(int port) throws RemoteException {
        // System.out.println!("In clientHandlerInterfaceImpl addClient function");
        try {
            Registry registry = LocateRegistry.getRegistry(port);
            client = (ClientInterface) registry.lookup("Client");
            server.addClient(this);
        } catch (NotBoundException e) {
            System.err.println("Failed to join a game");
        }
    }

    @Override
    public void notifyServer(ClientToServerCommand command) throws RemoteException {
        if (command instanceof RequestClosureCommand)
            closedByClient();
        else if (command instanceof ReconnectionAnswerCommand) {
            serverListener.notifyReconnectionAnswer((ReconnectionAnswerCommand) command, this);
        }

        else if (command instanceof SendCredentialsCommand || command instanceof ChosenLeaderCardCommand
                || command instanceof ChatMessageClientCommand || command instanceof ChurchSupportCommand)
            serverCommandHandler.notifyNewCommand(command);
        else if (matchHandlerObserver != null && matchHandlerObserver.isAllowed(player)) {

            serverCommandHandler.notifyNewCommand(command);
        }
    }

    @Override
    public void sendCommand(ServerToClientCommand command) throws IOException {
        client.notifyClient(command);
    }

    @Override
    public void closedByServer() throws RemoteException {
        if(!closed) {
            try {
                client.notifyClient(new CloseClientCommand());
            } catch (RemoteException e) {
                closedByClient();
            }
        }
        closed = true;
    }

    @Override
    public void closedByClient() {
        if(!closed) {
            if (this.matchHandlerObserver != null)
                this.matchHandlerObserver.removeClient(this);
            else
                server.removeWaitingClient(this);
        }
        closed = true;
    }

    @Override
    public void addObserver(MatchHandlerObserver matchObserver) {
        this.matchHandlerObserver = matchObserver;
    }

    @Override
    public void addCommandObserver(ServerCommandHandler commandHandler) {
        this.serverCommandHandler = commandHandler;
    }

    @Override
    public void addCommandObserver(Server server) {
        this.serverListener = server;
    }

    @Override
    public void run() {
        // System.out.println("ClientHandleInterfaceImpl is running");
    }
}
