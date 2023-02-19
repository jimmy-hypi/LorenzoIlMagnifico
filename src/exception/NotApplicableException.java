package exception;

/**
 * This is a checked Exception, the caller of an action must deal with the fact that
 * the action should be not applicable.
 */
public class NotApplicableException extends Exception {
    private static final long serialVersionUID = 1L;
    private String notApplicableCode;

    public NotApplicableException(String notApplicableCode) {
        this.notApplicableCode = notApplicableCode;
    }
    public String getNotApplicableCode(){
        return notApplicableCode;
    }
}
