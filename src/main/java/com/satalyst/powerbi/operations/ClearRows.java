package com.satalyst.powerbi.operations;

import com.satalyst.powerbi.*;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Aidan Morgan
 */
public class ClearRows implements PowerBiOperation<Void> {
    private UUID datasetId;
    private String tableName;

    public ClearRows(UUID datasetId, String tableName) {
        this.datasetId = checkNotNull(datasetId);
        this.tableName = checkNotNull(tableName);
    }

    @Override
    public Void get() {
        return null;
    }

    @Override
    public void buildUri(UriBuilder uri) {
        uri.path("myorg").path("datasets").path(datasetId.toString()).path("tables").path(tableName).path("rows");
    }

    @Override
    public void execute(PowerBiRequest request) throws PowerBiOperationExecutionException, RateLimitExceededException, RequestAuthenticationException {
       PowerBiResponse response = request.delete();

        if(response.getStatus() != 200) {
            throw new PowerBiOperationExecutionException("Expected 200 response.", response.getStatus(), response.getBody());
        }
    }
}
