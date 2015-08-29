package com.satalyst.powerbi;

/**
 * @author Aidan Morgan
 */
public class FakePowerBiResponse implements PowerBiResponse{

    private int status;
    private String body;

    public FakePowerBiResponse(int status, String body) {
        this.status = status;
        this.body = body;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getBody() {
        return body;
    }
}
