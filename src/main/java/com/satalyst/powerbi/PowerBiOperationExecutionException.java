package com.satalyst.powerbi;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Thrown if an error executing a {@see com.satalyst.powerbi.PowerBiOperation} occurs.
 *
 * @author Aidan Morgan
 */
public class PowerBiOperationExecutionException extends Exception {
    private Integer statusCode;
    private String errorMessage;

    public PowerBiOperationExecutionException(String message, Integer statusCode, String errorMessage) {
        super(message);
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("statusCode", statusCode)
                .append("errorMessage", errorMessage)
                .toString();
    }
}
