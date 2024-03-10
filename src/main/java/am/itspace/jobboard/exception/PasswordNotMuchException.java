package am.itspace.jobboard.exception;

public class PasswordNotMuchException extends RuntimeException {
    public PasswordNotMuchException() {
    }

    public PasswordNotMuchException(String message) {
        super(message);
    }

    public PasswordNotMuchException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordNotMuchException(Throwable cause) {
        super(cause);
    }

    public PasswordNotMuchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
