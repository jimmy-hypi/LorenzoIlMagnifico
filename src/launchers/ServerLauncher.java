package launchers;

import server.Server;

/**
 * Server launcher:
 * Instantiates Server to listen to clients and spawn matches.
 */
public class ServerLauncher {
    private ServerLauncher(){}

    public static void main(String[] args) {
        Thread t = null;
        try {
            Server server = Server.getInstance();
            t = new Thread(server);
            t.start();
        } catch (Exception e) {
            t.interrupt();
        }
    }
}
