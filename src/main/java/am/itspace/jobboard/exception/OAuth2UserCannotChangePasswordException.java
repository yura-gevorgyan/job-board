package am.itspace.jobboard.exception;

public class OAuth2UserCannotChangePasswordException extends RuntimeException {
    public OAuth2UserCannotChangePasswordException() {
    }

    public OAuth2UserCannotChangePasswordException(String message) {
        super(message);
    }

    public OAuth2UserCannotChangePasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public OAuth2UserCannotChangePasswordException(Throwable cause) {
        super(cause);
    }

    public OAuth2UserCannotChangePasswordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
