package am.itspace.jobboard.exception;

public class IncorrectDateFormatException extends RuntimeException {

    public IncorrectDateFormatException() {
    }

    public IncorrectDateFormatException(String message) {
        super(message);
    }

    public IncorrectDateFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectDateFormatException(Throwable cause) {
        super(cause);
    }

    public IncorrectDateFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
