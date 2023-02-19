package server.controller;

import server.Server;
/**
 * Timer thread that awaits the creation of a match if can't get to MAX_PLAYERS.
 */
public class InitialTimer implements Runnable {
    private Server creator;
    int millis;

    public InitialTimer(Server creator, int millis) {
        this.creator = creator;
        this.millis = millis;
    }
    @Override
    public void run() {
        try {
            Thread.sleep(this.millis);
        } catch (InterruptedException e) {
        }
        creator.timerExpired();
    }
}
