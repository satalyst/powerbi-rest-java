package com.satalyst.powerbi.impl;

import com.satalyst.powerbi.PowerBiResponse;

import javax.ws.rs.core.Response;

/**
 * @author Aidan Morgan
 */
public class PowerBiResponseImpl implements PowerBiResponse {
    private Response response;

    public PowerBiResponseImpl(Response response) {
        this.response = response;
    }

    @Override
    public int getStatus() {
        return response.getStatus();
    }

    @Override
    public String getBody() {
        return response.readEntity(String.class);
    }
}
