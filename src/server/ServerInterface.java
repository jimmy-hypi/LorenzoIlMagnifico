package server;

import server.controller.MatchHandler;


/**
 * Interface that abstract server operations:
 * Implementors will use different data structures & technologies and provide
 * these server operations.
 */
public interface ServerInterface {
    public void addClient(ClientHandler clientHandler);

    public void removeClient(ClientHandler clientHandler);

    public void closeMatch(MatchHandler matchHandler);
}
