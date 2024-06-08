package am.itspace.jobboard.exception;

public class PasswordToShortException extends RuntimeException {
    public PasswordToShortException() {
    }

    public PasswordToShortException(String message) {
        super(message);
    }

    public PasswordToShortException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordToShortException(Throwable cause) {
        super(cause);
    }

    public PasswordToShortException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
