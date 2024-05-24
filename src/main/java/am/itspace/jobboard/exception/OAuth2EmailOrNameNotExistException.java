package am.itspace.jobboard.exception;

public class OAuth2EmailOrNameNotExistException extends RuntimeException {
    public OAuth2EmailOrNameNotExistException() {
    }

    public OAuth2EmailOrNameNotExistException(String message) {
        super(message);
    }

    public OAuth2EmailOrNameNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public OAuth2EmailOrNameNotExistException(Throwable cause) {
        super(cause);
    }

    public OAuth2EmailOrNameNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
