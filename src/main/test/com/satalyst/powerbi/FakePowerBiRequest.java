package com.satalyst.powerbi;

/**
 * @author Aidan Morgan
 */
public class FakePowerBiRequest implements PowerBiRequest {
    private boolean isGet;
    private boolean isPost;
    private String posted;

    private boolean isPut;
    private String putted;

    private boolean isDelete;

    private PowerBiResponse response;

    public FakePowerBiRequest(PowerBiResponse response) {
        this.response = response;
    }

    @Override
    public PowerBiResponse get() throws RateLimitExceededException, RequestAuthenticationException {
        this.isGet = true;
        return response;
    }

    @Override
    public PowerBiResponse post(String json) throws RateLimitExceededException, RequestAuthenticationException {
        this.isPost = true;
        this.posted = json;

        return response;
    }

    @Override
    public PowerBiResponse put(String json) throws RateLimitExceededException, RequestAuthenticationException {
        this.isPut = true;
        this.putted = json;

        return response;
    }

    @Override
    public PowerBiResponse delete() throws RateLimitExceededException, RequestAuthenticationException {
        this.isDelete = true;
        return response;
    }

    public boolean isGet() {
        return isGet;
    }

    public boolean isPost() {
        return isPost;
    }

    public boolean isPut() {
        return isPut;
    }

    public boolean isDelete() {
        return isDelete;
    }
}
