package com.satalyst.powerbi;

/**
 * @author Aidan Morgan
 */
public interface PowerBiRequest {
    public PowerBiResponse get() throws RateLimitExceededException, RequestAuthenticationException;
    public PowerBiResponse post(String json) throws RateLimitExceededException, RequestAuthenticationException;
    public PowerBiResponse put(String json) throws RateLimitExceededException, RequestAuthenticationException;
    public PowerBiResponse delete() throws RateLimitExceededException, RequestAuthenticationException;

}
