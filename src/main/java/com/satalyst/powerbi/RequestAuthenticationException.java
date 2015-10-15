package com.satalyst.powerbi;

/**
 * @author Aidan Morgan
 */
public class RequestAuthenticationException extends Exception {
    private final boolean tokenExpired;

    public RequestAuthenticationException(boolean tokenExpired) {
        this.tokenExpired = tokenExpired;
    }

    public RequestAuthenticationException(String message, boolean tokenExpired) {
        super(message);
        this.tokenExpired = tokenExpired;
    }

    public RequestAuthenticationException(String message, Throwable cause, boolean tokenExpired) {
        super(message, cause);
        this.tokenExpired = tokenExpired;

    }

    public RequestAuthenticationException(Throwable cause, boolean tokenExpired) {
        super(cause);
        this.tokenExpired = tokenExpired;
    }

    protected RequestAuthenticationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, boolean tokenExpired) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.tokenExpired = tokenExpired;
    }

    public boolean isTokenExpired() {
        return tokenExpired;
    }
}
