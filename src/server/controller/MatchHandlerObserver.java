package server.controller;

import model.Player;
import server.ClientHandler;

/**
 * This interface is used to implement the MVC pattern between the two ClientHandler concrete
 * classes and the MatchHandler
 */
public interface MatchHandlerObserver {
    /**
     * This method will check and allow the application of a command sent by a specific player.
     */
    boolean isAllowed(Player player);

    void removeClient(ClientHandler clientHandler);
}
