package com.satalyst.powerbi;

import java.util.concurrent.Future;

/**
 * @author Aidan Morgan
 */
public interface PowerBiConnection {
    /**
     * Executes the provide {@see PowerBiOperation} against this connection.
     * @param val the {@see PowerBiOperation} to execute.
     * @param <T> the type of the result to be returned after the operation has executed.
     * @return a {@see Future} that when completed will contain the result from performing the operation.
     */
    public <T> Future<T> execute(PowerBiOperation<T> val);
}
