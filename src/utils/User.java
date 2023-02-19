package utils;

/**
 * This class represents a User logged into the game
 */
public class User {
    private String username;
    private int pswHash;
    private int wonMatches;
    private int lostMatches;
    private int totalMatches;
    private int secondsPlayed;

    public User(String username, int pswHash, int wonMatches, int lostMatches, int totalMatches, int secondsPlayed) {
        this.username = username;
        this.pswHash = pswHash;
        this.lostMatches = lostMatches;
        this.totalMatches = totalMatches;
        this.secondsPlayed = secondsPlayed;
    }

    public User(String username, String password) {
        this(username, password.hashCode(), 0, 0, 0, 0);
    }

    public boolean correctPassword(String psw){
        return this.pswHash==psw.hashCode();
    }

    public String getUsername() {
        return username;
    }

    public int getPswHash() {
        return pswHash;
    }

    public int getWonMatches() {
        return wonMatches;
    }

    public int getLostMatches() {
        return lostMatches;
    }

    public int getTotalMatches() {
        return totalMatches;
    }

    public int getSecondsPlayed() {
        return secondsPlayed;
    }

    public void incrementMatches() {
        this.totalMatches++;
    }

    public void incrementSecondsPlayed(int seconds){
        this.secondsPlayed+=seconds;
    }

    public void incrementLostMathces() {
        this.lostMatches++;
    }

    public void incrementWonMathces() {
        this.wonMatches++;
    }
}
