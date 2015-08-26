package com.satalyst.powerbi;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Aidan Morgan
 */
public class AuthenticationFailureException extends Exception {
    public AuthenticationFailureException() {
        super();
    }

    public AuthenticationFailureException(String message) {
        super(message);
    }

    public AuthenticationFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationFailureException(Throwable cause) {
        super(cause);
    }

    protected AuthenticationFailureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
