package server.socket;

import command.toclient.CloseClientCommand;
import command.toclient.InvalidCommand;
import command.toclient.ServerToClientCommand;
import command.toserver.*;
import server.ClientHandler;
import server.Server;
import server.ServerCommandHandler;
import server.ServerInterface;
import server.controller.MatchHandlerObserver;
import server.observers.CommandObserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

/**
 * Wrapper for server side socket. handles communication between server and client.
 */
public class ClientHandlerSocket extends ClientHandler {

    private Socket socket;
    private ObjectInputStream inSocket;
    private ObjectOutputStream outSocket;
    private CommandObserver commandHandler;
    private ServerInterface creator;
    private MatchHandlerObserver matchObserver;
    private Server serverListener;

    public ClientHandlerSocket(Socket socket, int number, ServerInterface serverStarter) {
        this.socket = socket;
        creator = serverStarter;
        closed = false;
        try {
            inSocket = new ObjectInputStream(this.socket.getInputStream());
            outSocket = new ObjectOutputStream(this.socket.getOutputStream());
            outSocket.flush();
        } catch (IOException e) {
            closedByServer();
        }
    }
    @Override
    public void sendCommand(ServerToClientCommand command) throws IOException {
        outSocket.writeUnshared(command);
        outSocket.flush();
        outSocket.reset();
    }

    @Override
    public void closedByServer() {
        try {
            outSocket.writeUnshared(new CloseClientCommand());
        } catch (IOException e) {
        }
        close();
    }

    @Override
    public void closedByClient() {
        if (matchObserver != null)
            matchObserver.removeClient(this);
        else
            creator.removeClient(this);
        close();
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    public void close() {
        if(!closed) {
            try {
                closed = true;
                socket.close();
                inSocket.close();
                outSocket.close();
            } catch (Exception e) {
            }
        }
    }
    @Override
    public void addObserver(MatchHandlerObserver matchObserver) {
        this.matchObserver = matchObserver;
    }

    @Override
    public void addCommandObserver(ServerCommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void addCommandObserver(Server server) {
        this.serverListener = server;
    }

    @Override
    public void run() {
        ClientToServerCommand command;

        while (true) {
            command = null;
            try {
                command = (ClientToServerCommand) inSocket.readObject();
                if (command instanceof ReconnectionAnswerCommand) {
                    serverListener.notifyReconnectionAnswer((ReconnectionAnswerCommand) command, this);
                }
            } catch (ClassNotFoundException | IOException e) {
                close();
                break;
            }
            if (command instanceof RequestClosureCommand)
                closedByClient();
            // Commands that can be sent in an asynchronous way from the clients and are always valid
            // and managed by the ServerCommandHandler
            else if (command instanceof SendCredentialsCommand || command instanceof ChosenLeaderCardCommand
                    || command instanceof ChatMessageClientCommand || command instanceof ChurchSupportCommand
                    || command instanceof SatanChoiceCommand) {
                commandHandler.notifyNewCommand(command);
            }
            // Commands that need a check, if they are from the current player they are allowed
            else if (matchObserver != null && matchObserver.isAllowed(player)) {
                commandHandler.notifyNewCommand(command);
            } else if (!matchObserver.isAllowed(player) | matchObserver == null) {
                try {
                    sendCommand(new InvalidCommand());
                } catch (IOException e) {
                    closedByClient();
                }
            }
        }
    }


}
