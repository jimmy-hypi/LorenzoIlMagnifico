package network;

import client.ClientCommandHandler;
import client.ClientSocketListener;
import command.toclient.ServerToClientCommand;
import command.toserver.ClientToServerCommand;
import constant.NetworkConstants;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * The NetworkInterface implementors that uses Socket technology.
 */
public class ClientSocketInterface implements NetworkInterface {
    private ObjectInputStream inSocket;
    private ObjectOutputStream outSocket;
    private Socket socket;
    private ClientSocketListener listener;
    private Thread hear; // the thread that listens on the socket channel

    @Override
    public void connect() throws Exception {
        try {
            socket = new Socket(NetworkConstants.SERVER_IP_ADDRESS, NetworkConstants.PORT);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            outSocket = new ObjectOutputStream(socket.getOutputStream());
            outSocket.flush();
            inSocket = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        listener = new ClientSocketListener(inSocket);
        hear = new Thread(listener);
        hear.start();
    }

    @Override
    public void sendCommand(ClientToServerCommand command) throws Exception {
        try{
            outSocket.writeObject(command);
            outSocket.flush();
            outSocket.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyClient(ServerToClientCommand command) {

    }

    @Override
    public void closeConnection() {
        try{
            socket.close();
            inSocket.close();
            outSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void addCommandObserver(ClientCommandHandler handler) {
        listener.addCommandObserver(handler);
    }
}
