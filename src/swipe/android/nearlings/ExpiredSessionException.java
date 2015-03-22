package swipe.android.nearlings;

public class ExpiredSessionException extends Exception {
 
    private String message = "Expired Token";
 
    public ExpiredSessionException() {
        super();
    }
 
    public ExpiredSessionException(String message) {
        super(message);
        this.message = message;
    }
 
    public ExpiredSessionException(Throwable cause) {
        super(cause);
    }
 
    @Override
    public String toString() {
        return message;
    }
 
    @Override
    public String getMessage() {
        return message;
    }
}