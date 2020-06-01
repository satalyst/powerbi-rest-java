package com.satalyst.powerbi.operations;

import com.satalyst.powerbi.PowerBiOperation;
import com.satalyst.powerbi.PowerBiOperationExecutionException;
import com.satalyst.powerbi.PowerBiRequest;
import com.satalyst.powerbi.PowerBiResponse;
import com.satalyst.powerbi.RateLimitExceededException;
import com.satalyst.powerbi.RequestAuthenticationException;
import org.apache.http.HttpStatus;

import javax.ws.rs.core.UriBuilder;
import java.util.UUID;

import static com.satalyst.powerbi.impl.PowerBiConstants.GROUPS;
import static com.satalyst.powerbi.impl.PowerBiConstants.MY_ORG;

/**
 * @author Manoj Sharma
 */
public class DeleteGroup implements PowerBiOperation<Void> {
    private UUID groupId;

    public DeleteGroup(UUID groupId) {
        this.groupId = groupId;
    }

    @Override
    public Void get() {
        return null;
    }

    @Override
    public void buildUri(UriBuilder uri) {
        uri.path(MY_ORG).path(GROUPS).path(groupId.toString());
    }

    @Override
    public void execute(PowerBiRequest request) throws PowerBiOperationExecutionException, RateLimitExceededException, RequestAuthenticationException {
        PowerBiResponse response = request.delete();

        if (response.getStatus() != HttpStatus.SC_OK) {
            throw new PowerBiOperationExecutionException("Expected status code of 200.", response.getStatus(), response.getBody());
        }

    }
}