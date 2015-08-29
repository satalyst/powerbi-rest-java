package com.satalyst.powerbi.operations;

import com.google.gson.Gson;
import com.satalyst.powerbi.*;

import javax.ws.rs.core.Response;

/**
 * Abstract class that performs basic get operations, delegating to subclasses to parse the response JSON from the server.
 * @author Aidan Morgan
 */
public abstract class AbstractGetOperation<T> implements PowerBiOperation<T> {
    private Gson parser;
    private T result;

    public AbstractGetOperation() {
        this.parser = new Gson();
    }

    @Override
    public final T get() {
        return result;
    }

    @Override
    public final void execute(PowerBiRequest request) throws PowerBiOperationExecutionException, RateLimitExceededException, RequestAuthenticationException {
        PowerBiResponse response = request.get();

        if(response.getStatus() != 200) {
            throw new PowerBiOperationExecutionException("Expected a 200 status code.", response.getStatus(), response.getBody());
        }

        result = parseJson(parser, response.getBody());
    }


    protected abstract T parseJson(Gson parser, String value);
}
