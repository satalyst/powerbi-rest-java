package com.satalyst.powerbi.operations;

import com.google.gson.Gson;
import com.satalyst.powerbi.PowerBiOperation;
import com.satalyst.powerbi.PowerBiOperationExecutionException;

import javax.ws.rs.client.Invocation;
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
    public final void execute(Invocation.Builder request) throws PowerBiOperationExecutionException {
        Response response = request.get();

        if(response.getStatus() != 200) {
            throw new PowerBiOperationExecutionException("Expected status code of 200.", response.getStatus(), response.readEntity(String.class));
        }

        result = parseJson(parser, response.readEntity(String.class));
    }


    protected abstract T parseJson(Gson parser, String value);
}
