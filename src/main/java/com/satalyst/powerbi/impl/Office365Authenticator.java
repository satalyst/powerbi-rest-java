package com.satalyst.powerbi.impl;

import com.microsoft.aad.adal4j.AuthenticationContext;
import com.satalyst.powerbi.AuthenticationFailureException;
import com.satalyst.powerbi.Authenticator;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * https://msdn.microsoft.com/en-US/library/dn877545.aspx
 *
 * @author Aidan Morgan
 */
public class Office365Authenticator implements Authenticator {
    private static final String DEFAULT_AUTHORITY = "https://login.windows.net/common/oauth2/authorize";
    private static final String DEFAULT_POWER_BI_RESOURCE_ID = "https://analysis.windows.net/powerbi/api";
    private static final boolean DEFAULT_VALIDATE_AUTHORITY = false;

    private String authority = DEFAULT_AUTHORITY;
    private String powerBiResourceId = DEFAULT_POWER_BI_RESOURCE_ID;
    private boolean validateAuthority = DEFAULT_VALIDATE_AUTHORITY;

    private String nativeClientId;
    private String tenant;
    private String username;
    private String password;

    private ExecutorService executor;


    public Office365Authenticator(String nativeClientId, String tenant, String username, String password) {
        this(nativeClientId, tenant, username, password, Executors.newSingleThreadExecutor());
    }

    public Office365Authenticator(String nativeClientId, String tenant, String username, String password, ExecutorService executor) {
        this.nativeClientId = nativeClientId;
        this.tenant = tenant;
        this.username = username;
        this.password = password;
        this.executor = executor;
    }

    public Office365Authenticator setAuthority(String authority) {
        this.authority = checkNotNull(authority);
        return this;
    }

    public Office365Authenticator setPowerBiResourceId(String powerBiResourceId) {
        this.powerBiResourceId = checkNotNull(powerBiResourceId);
        return this;
    }

    public Office365Authenticator setValidateAuthority(boolean validateAuthority) {
        this.validateAuthority = validateAuthority;
        return this;
    }

    private ReadWriteLock tokenLock = new ReentrantReadWriteLock();
    private String cachedToken;

    /**
     * Performs the authentication.
     *
     * Thread-safe implementation that will cache the bearer token for multiple calls to ensure that we don't make
     * repeated, unnecessary calls to the authentication service.
     *
     * @return the bearer token to use for authenticating service requests.
     * @throws AuthenticationFailureException
     */
    public String authenticate() throws AuthenticationFailureException {
        try {
            tokenLock.readLock().lock();

            if (cachedToken == null) {
                try {
                    // release the read lock and acquire the write lock to call the implementation
                    tokenLock.readLock().unlock();
                    tokenLock.writeLock().lock();

                    // check again, it may have been set in the time it took us to acquire the write lock
                    if (cachedToken == null) {
                        cachedToken = _authenticate();
                    }

                    // Downgrade by acquiring read lock before releasing write lock
                    tokenLock.readLock().lock();
                } finally {
                    tokenLock.writeLock().unlock();
                }
            }
        } finally {
            // TODO: in theory, if there has been an exception in the authenticate method then this unlock method
            // TODO: should fail as the downgrade of the lock was never performed. Haven't seen this issue in practice yet
            // TODO: however it looks theoretically possible.
            try {
                tokenLock.readLock().unlock();
            } catch (IllegalMonitorStateException e) {
                // ignore - see TODO above for reasoning....
            }
        }


        return cachedToken;
    }

    @Override
    public void reset() {
        try {
            tokenLock.writeLock().lock();
            cachedToken = null;
        }
        finally {
            tokenLock.writeLock().unlock();
        }
    }

    private String _authenticate() throws AuthenticationFailureException {
        try {
            AuthenticationContext authenticationContext = new AuthenticationContext(
                    authority,
                    validateAuthority,
                    executor
            );

            String result = getAccessToken(
                    authenticationContext,
                    powerBiResourceId,
                    nativeClientId,
                    username + "@" + tenant,
                    password
            );

            if (StringUtils.isEmpty(result)) {
                throw new AuthenticationFailureException("Returned access token is null.");
            }

            return result;
        } catch (ExecutionException | InterruptedException | IOException e) {
            throw new AuthenticationFailureException(e);
        }
    }

    private String getAccessToken(AuthenticationContext authenticationContext, String resourceId, String clientId,
                                  String username, String password) throws ExecutionException, InterruptedException {
        return authenticationContext.acquireToken(
                resourceId,
                clientId,
                username,
                password,
                null
        ).get().getAccessToken();
    }
}
