package com.satalyst.powerbi;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.UriBuilder;

/**
 * Base interface for all operations performed against the PowerBI server.
 *
 * @author Aidan Morgan
 */
public interface PowerBiOperation<T> {
    /**
     * Returns the value that was created after the operation was invoked.
     *
     * May be null in the case of an operation that does not return a result.
     *
     * @return the value that was created after the operation was invoked.
     */
    T get();

    /**
     * Used to build the URI for the resource to be operated against.
     * @param uri the {@see UriBuilder} instance to use for building the {@see URI}.
     */
    void buildUri(UriBuilder uri);

    /**
     * Executes the operation using the pre-configured {@see Invocation.Builder} request object.
     * @param request the request to perform.
     * @throws PowerBiOperationExecutionException if a problem processing the request occurs.
     */
    void execute(PowerBiRequest request) throws PowerBiOperationExecutionException, RateLimitExceededException, RequestAuthenticationException;
}
