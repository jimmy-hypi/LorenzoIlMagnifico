package server.controller;

/**
 * Timer thread that counts down available time for a client to perform their round.
 */
public class RoundTimer implements Runnable {
    private MatchHandler handler;
    private int millis;

    public RoundTimer(MatchHandler handler, int millis) {
        this.handler = handler;
        this.millis = millis;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(this.millis);
        } catch (InterruptedException e) {
            return;
        }
        handler.roundTimerExpired();
    }
}
