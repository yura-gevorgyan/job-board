package am.itspace.jobboard.exception;

public class EmailIsPresentException extends RuntimeException {
    public EmailIsPresentException() {
    }

    public EmailIsPresentException(String message) {
        super(message);
    }

    public EmailIsPresentException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailIsPresentException(Throwable cause) {
        super(cause);
    }

    public EmailIsPresentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
