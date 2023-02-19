package exception;

/**
 * A checked Exception to signal a wrong player to send a command to has been selected
 */
public class WrongPlayerException extends Exception {
    private static final long serialVersionUID = 1L;
    private final String error = "The clientHandler associated to the player the server" +
            "is trying to send a command to is not in the list of clientHandlers";
    public String getError(){
        return error;
    }
}
