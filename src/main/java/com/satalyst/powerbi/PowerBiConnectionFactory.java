package com.satalyst.powerbi;

import com.satalyst.powerbi.impl.DefaultPowerBiConnectionFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Used to create new {@see PowerBiConnection} instances.
 *
 * @author Aidan Morgan
 */
public interface PowerBiConnectionFactory {
    DefaultPowerBiConnectionFactory setPowerBiBaseUrl(String powerBiBaseUrl);

    DefaultPowerBiConnectionFactory setExecutor(ExecutorService executor);

    DefaultPowerBiConnectionFactory setMaximumWaitTime(long val, TimeUnit units);

    DefaultPowerBiConnectionFactory setMaximumRetries(int maximumRetries);

    /**
     * Create a new {@see PowerBiConnection} for performing operations.
     * @return a new {@see PowerBiConnection} instance.
     */
    public PowerBiConnection create();
}
