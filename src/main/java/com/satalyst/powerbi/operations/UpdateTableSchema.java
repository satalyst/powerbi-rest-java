package com.satalyst.powerbi.operations;

import com.google.gson.Gson;
import com.satalyst.powerbi.PowerBiOperation;
import com.satalyst.powerbi.PowerBiOperationExecutionException;
import com.satalyst.powerbi.model.Table;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Aidan Morgan
 */
public class UpdateTableSchema implements PowerBiOperation<Void> {
    private UUID datasetId;
    private String tableName;
    private Table schema;
    private Gson parser;

    public UpdateTableSchema(UUID datasetId, String tableName, Table newSchema) {
        this.datasetId = checkNotNull(datasetId);
        this.tableName = checkNotNull(tableName);
        this.schema = checkNotNull(newSchema);
        this.parser = new Gson();
    }

    @Override
    public Void get() {
        return null;
    }

    @Override
    public void buildUri(UriBuilder uri) {
        uri.path("myorg").path("datasets").path(datasetId.toString()).path("tables").path(tableName);

    }

    // 200 for no change, 201 for a change made - need to handle both scenarios?
    private static List<Integer> SUCCESS_RESPONSES = Arrays.asList(200, 201);

    @Override
    public void execute(Invocation.Builder request) throws PowerBiOperationExecutionException {
        Entity out = Entity.json(parser.toJson(CreateDataset.createTableJson(schema)));
        Response response = request.put(out);

        if(!SUCCESS_RESPONSES.contains(response.getStatus())) {
            throw new PowerBiOperationExecutionException("Expected response of 201.", response.getStatus(), response.readEntity(String.class));
        }
    }
}
