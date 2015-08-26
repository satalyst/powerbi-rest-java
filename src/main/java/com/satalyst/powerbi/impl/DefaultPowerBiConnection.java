package com.satalyst.powerbi.impl;

import com.satalyst.powerbi.Authenticator;
import com.satalyst.powerbi.PowerBiOperation;
import com.satalyst.powerbi.PowerBiConnection;
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

/**
 * @author Aidan Morgan
 */
public class DefaultPowerBiConnection implements PowerBiConnection {
    private Authenticator authenticator;
    private ExecutorService executor;
    private ClientBuilder clientBuilder;

    private String baseUrl;

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

    @Override
    public <T> Future<T> execute(PowerBiOperation<T> val) {
        Callable<T> call = new PowerBiCallable<>(val, clientBuilder);
        return executor.submit(call);
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

                // delegate to the command to perform the processing now.
                command.execute(request);
            } finally {
                if (client != null) {
                    client.close();
                }
            }

            return command.get();
        }
    }
}
