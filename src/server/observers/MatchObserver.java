package server.observers;

import model.Player;

/**
 * Implemented by the MatchHandler class. The only one that knows the state of the game
 * and is able to determine the player to send model updates to based on the mapping between
 * clientHandler and player.
 */
public interface MatchObserver {
    public void notifyPlayerStatusChange(Player player);
}
