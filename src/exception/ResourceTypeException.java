package exception;

/**
 * The unchecked exception thrown when an invalid resource type is passed to the Resource Factory
 */
public class ResourceTypeException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final String error = "Resource id is not valid";
    public String getErrorCode() {
        return error;
    }
}
