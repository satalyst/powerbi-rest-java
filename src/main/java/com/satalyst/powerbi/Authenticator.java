package com.satalyst.powerbi;

/**
 * Used to authenticate against the PowerBI service and retrieve a bearer token.
 *
 * @author Aidan Morgan
 */
public interface Authenticator {
    /**
     * Perform the authentication to the PowerBI server, returning the bearer token to use for authentication of {@see com.satalyst.powerbi.PowerBiOperation}.
     *
     * @return the bearer token to use for service authentication
     * @throws AuthenticationFailureException if a problem authenticating with the server occurs.
     */
    public String authenticate() throws AuthenticationFailureException;
}
