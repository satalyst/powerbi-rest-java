package com.satalyst.powerbi.impl;

import com.satalyst.powerbi.Authenticator;
import com.satalyst.powerbi.PowerBiConnection;
import com.satalyst.powerbi.PowerBiConnectionFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Aidan Morgan
 */
public class DefaultPowerBiConnectionFactory implements PowerBiConnectionFactory {
    private static final String DEFAULT_POWER_BI_BASE_URL = "https://api.powerbi.com/v1.0";

    private String powerBiBaseUrl = DEFAULT_POWER_BI_BASE_URL;

    public static final long DEFAULT_MAXIMUM_WAIT_TIME = TimeUnit.SECONDS.toMillis(30);
    public static final int DEFAULT_RETRIES = 10;


    private Authenticator authenticator;
    private ExecutorService executor;

    private long maximumWaitTime = DEFAULT_MAXIMUM_WAIT_TIME;
    private int maximumRetries = DEFAULT_RETRIES;

    public DefaultPowerBiConnectionFactory(Authenticator authenticator) {
       this(authenticator, Executors.newSingleThreadExecutor());
    }

    public DefaultPowerBiConnectionFactory(Authenticator authenticator, ExecutorService executor) {
        this.authenticator = authenticator;
        this.executor = executor;
    }


    public DefaultPowerBiConnectionFactory setPowerBiBaseUrl(String powerBiBaseUrl) {
        this.powerBiBaseUrl = checkNotNull(powerBiBaseUrl);
        return this;
    }

    public DefaultPowerBiConnectionFactory setExecutor(ExecutorService executor) {
        this.executor = executor;
        return this;
    }

    public DefaultPowerBiConnectionFactory setMaximumWaitTime(long val, TimeUnit units) {
        this.maximumWaitTime = units.toMillis(val);
        return this;
    }

    public DefaultPowerBiConnectionFactory setMaximumRetries(int maximumRetries) {
        this.maximumRetries = maximumRetries;
        return this;
    }

    @Override
    public PowerBiConnection create() {
        DefaultPowerBiConnection connection = new DefaultPowerBiConnection(authenticator, executor)
                .setBaseUrl(powerBiBaseUrl)
                .setMaximumRetries(maximumRetries)
                .setMaximumWaitTime(maximumWaitTime, TimeUnit.MILLISECONDS);

        return connection;
    }
}
