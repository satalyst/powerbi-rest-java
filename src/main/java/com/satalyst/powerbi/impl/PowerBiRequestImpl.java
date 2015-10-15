package com.satalyst.powerbi.impl;

import com.satalyst.powerbi.PowerBiRequest;
import com.satalyst.powerbi.PowerBiResponse;
import com.satalyst.powerbi.RateLimitExceededException;
import com.satalyst.powerbi.RequestAuthenticationException;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

/**
 * @author Aidan Morgan
 */
public class PowerBiRequestImpl implements PowerBiRequest {
    private Invocation.Builder request;

    public PowerBiRequestImpl(Invocation.Builder request) {
        this.request = request;
    }

    @Override
    public PowerBiResponse get() throws RateLimitExceededException, RequestAuthenticationException {
        Response r = request.get();
        checkResponseCode(r);

        return new PowerBiResponseImpl(r);
    }

    @Override
    public PowerBiResponse post(String json) throws RateLimitExceededException, RequestAuthenticationException {
        Entity<String> entity = Entity.json(json);
        Response r = request.post(entity);
        checkResponseCode(r);

        return new PowerBiResponseImpl(r);
    }

    @Override
    public PowerBiResponse put(String json) throws RateLimitExceededException, RequestAuthenticationException {
        Entity<String> entity = Entity.json(json);
        Response r = request.put(entity);
        checkResponseCode(r);

        return new PowerBiResponseImpl(r);
    }

    @Override
    public PowerBiResponse delete() throws RateLimitExceededException, RequestAuthenticationException {
        Response r = request.delete();
        checkResponseCode(r);

        return new PowerBiResponseImpl(r);
    }

    private static void checkResponseCode(Response response) throws RateLimitExceededException, RequestAuthenticationException {
        if (response.getStatus() == 429) {
            throw new RateLimitExceededException(response.readEntity(String.class));
        }

        if (response.getStatus() == 403) {
            String body = response.readEntity(String.class);

            // TODO : replace with proper JSON parsing to extract the token.
            throw new RequestAuthenticationException(response.readEntity(String.class), StringUtils.contains(body, "TokenExpired"));
        }
    }

}
