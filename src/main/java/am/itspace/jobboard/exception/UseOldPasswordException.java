package am.itspace.jobboard.exception;

public class UseOldPasswordException extends RuntimeException{
    public UseOldPasswordException() {
    }

    public UseOldPasswordException(String message) {
        super(message);
    }

    public UseOldPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public UseOldPasswordException(Throwable cause) {
        super(cause);
    }

    public UseOldPasswordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
