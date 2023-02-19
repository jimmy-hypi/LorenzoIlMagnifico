package server.socket;

import constant.NetworkConstants;
import server.ClientHandler;
import server.ServerInterface;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The listener for receiving Socket events, this is instantiated by the server
 * and listens for new clients that try to connect to the server via socket
 */
public class ServerSocketListener implements Runnable {
    private int port;
    private ServerSocket server;
    private boolean listening;
    private ServerInterface creator;

    public ServerSocketListener(ServerInterface serverStarter) {
        port = NetworkConstants.PORT;
        listening = false;
        creator = serverStarter;
    }

    public ServerSocketListener(ServerInterface serverStarter, int port) {
        this.port = port;
        listening = false;
        creator = serverStarter;
    }

    @Override
    public void run() {
        try {
            startListening();
        } catch(IOException e) {
            try{
                endListening();
            } catch(IOException e2) {}
        }
    }

    private void startListening() throws IOException {
        server = new ServerSocket(port);
        listening = true;
        int i = 0;
        while (listening) {
            Socket socket = server.accept();
            ClientHandler c = new ClientHandlerSocket(socket, i, creator);
            creator.addClient(c);
            i++;
        }
    }
    public void endListening() throws IOException {
        if (listening) {
            listening = false;
            server.close();
        }
    }
}
