package com.satalyst.powerbi.impl;

import com.github.rholder.retry.AttemptTimeLimiters;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.satalyst.powerbi.*;
import org.jboss.resteasy.specimpl.ResteasyUriBuilder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Aidan Morgan
 */
public class DefaultPowerBiConnection implements PowerBiConnection {

    private Authenticator authenticator;
    private ExecutorService executor;
    private ClientBuilder clientBuilder;

    private String baseUrl;

    private long maximumWaitTime;
    private int maximumRetries;

    /**
     * Constructor.
     *
     * @param authenticator the {@see Authenticator} instance to use for authentication.
     * @param executor      the {@see ExecutorService} to use for background processing.
     */
    public DefaultPowerBiConnection(Authenticator authenticator, ExecutorService executor) {
        this.authenticator = authenticator;
        this.executor = executor;
        this.clientBuilder = ClientBuilder.newBuilder();
    }

    public DefaultPowerBiConnection setMaximumWaitTime(long val, TimeUnit timeUnit) {
        this.maximumWaitTime = timeUnit.toMillis(val);
        return this;
    }

    public DefaultPowerBiConnection setMaximumRetries(int val) {
        this.maximumRetries = val;
        return this;
    }

    @Override
    public <T> Future<T> execute(final PowerBiOperation<T> val) {
        // use a retryer to keep attempting to send data to powerBI if we receive a rate limit exception.
        // use exponential backoff to create a window of time for the request to come through.

        // TODO : the time to wait is actually in the response header, come back and add that value.
        final Retryer<T> retryer = RetryerBuilder.<T>newBuilder()
                // we are retrying on these exceptions because they are able to be handled by just retrying this callable
                // again (assuming that the maximum retries value is greater than 1 and this isn't the last retry attempt
                // before giving up.
                .retryIfExceptionOfType(RateLimitExceededException.class)
                .retryIfExceptionOfType(RequestAuthenticationException.class)
                .withAttemptTimeLimiter(AttemptTimeLimiters.<T>fixedTimeLimit(maximumWaitTime, TimeUnit.MILLISECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(maximumRetries))
                .build();

        return executor.submit(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return retryer.call(new PowerBiCallable<>(val, clientBuilder));
            }
        });
    }

    public DefaultPowerBiConnection setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }


    private class PowerBiCallable<T> implements Callable<T> {
        private PowerBiOperation<T> command;
        private ClientBuilder clientBuilder;

        public PowerBiCallable(PowerBiOperation<T> val, ClientBuilder clientBuilder) {
            this.command = val;
            this.clientBuilder = clientBuilder;
        }

        @Override
        public T call() throws Exception {
            UriBuilder uri = new ResteasyUriBuilder().path(baseUrl);
            command.buildUri(uri);

            Client client = null;
            try {
                client = clientBuilder.build();
                WebTarget target = client.target(uri.build());

                Invocation.Builder request = target.request();
                request.header("Authorization", "Bearer " + authenticator.authenticate());
                request.accept(MediaType.APPLICATION_JSON_TYPE);

                PowerBiRequest r = new PowerBiRequestImpl(request);
                // delegate to the command to perform the processing now.
                command.execute(r);
            } catch (RequestAuthenticationException e) {
                // we are intentionally resetting the authenticator here as the previous token has expired, so the next time
                // through this call the authenticator will not used the cached token and will retrieve a new one.
                if (e.isTokenExpired()) {
                    authenticator.reset();
                }
                throw e;
            } finally {
                if (client != null) {
                    client.close();
                }
            }

            return command.get();
        }
    }
}
