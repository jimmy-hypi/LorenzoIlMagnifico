package exception;

/**
 * Exception thrown when the card id is wrong
 */
public class CardTypeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String errorMessage = "Card id is not valid";
    public String getErrorMessage(){
        return errorMessage;
    }
}
