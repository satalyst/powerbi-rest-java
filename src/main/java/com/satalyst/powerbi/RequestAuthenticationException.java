package com.satalyst.powerbi;

/**
 * @author Aidan Morgan
 */
public class RequestAuthenticationException extends Exception {
    public RequestAuthenticationException() {
        super();
    }

    public RequestAuthenticationException(String message) {
        super(message);
    }

    public RequestAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestAuthenticationException(Throwable cause) {
        super(cause);
    }

    protected RequestAuthenticationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
