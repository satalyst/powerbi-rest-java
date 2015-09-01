package com.satalyst.powerbi.impl;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
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
    public static final long DEFAULT_MAXIMUM_WAIT_TIME = TimeUnit.MINUTES.toMillis(5);
    public static final int DEFAULT_RETRIES = 10;

    private Authenticator authenticator;
    private ExecutorService executor;
    private ClientBuilder clientBuilder;

    private String baseUrl;

    private long maximumWaitTime = DEFAULT_MAXIMUM_WAIT_TIME;
    private int maximumRetries = DEFAULT_RETRIES;

    /**
     * Constructor.
     *
     * @param authenticator the {@see Authenticator} instance to use for authentication.
     * @param executor the {@see ExecutorService} to use for background processing.
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
        final Retryer<T> retryer = RetryerBuilder.<T>newBuilder()
                .retryIfExceptionOfType(RateLimitExceededException.class)
                .retryIfExceptionOfType(RequestAuthenticationException.class)
                .withWaitStrategy(WaitStrategies.exponentialWait(maximumWaitTime, TimeUnit.MILLISECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(maximumRetries))
                .build();

        return executor.submit(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return retryer.call(new PowerBiCallable<>(val, clientBuilder));
            }
        });
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
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
            } finally {
                if (client != null) {
                    client.close();
                }
            }

            return command.get();
        }
    }
}
