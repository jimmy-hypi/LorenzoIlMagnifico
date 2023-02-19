package client;

import command.toclient.ServerToClientCommand;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Socket listener for incoming server communication
 */
public class ClientSocketListener implements Runnable {
    private ObjectInputStream inSocket;
    private ServerToClientCommandObserver observer;
    public ClientSocketListener(ObjectInputStream inSocket){
        this.inSocket = inSocket;
    }
    @Override
    public void run() {
            ServerToClientCommand command;
            while (true){
                try{
                    command = (ServerToClientCommand)inSocket.readObject();
                    observer.notifyNewCommand(command);
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
    }
    public void addCommandObserver(ClientCommandHandler clientCommandHandler) {
        this.observer = clientCommandHandler;
    }
}
