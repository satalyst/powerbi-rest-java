package com.satalyst.powerbi.operations;

import com.google.gson.Gson;
import com.satalyst.powerbi.PowerBiOperation;
import com.satalyst.powerbi.PowerBiOperationExecutionException;
import com.satalyst.powerbi.PowerBiRequest;
import com.satalyst.powerbi.PowerBiResponse;
import com.satalyst.powerbi.RateLimitExceededException;
import com.satalyst.powerbi.RequestAuthenticationException;
import com.satalyst.powerbi.impl.model.DefaultGroup;
import com.satalyst.powerbi.model.Group;
import org.apache.http.HttpStatus;

import javax.ws.rs.core.UriBuilder;
import java.util.HashMap;
import java.util.Map;

import static com.satalyst.powerbi.impl.PowerBiConstants.GROUPS;
import static com.satalyst.powerbi.impl.PowerBiConstants.MY_ORG;
import static com.satalyst.powerbi.operations.MapUtils.getString;
import static com.satalyst.powerbi.operations.MapUtils.getUuid;


/**
 * @author Manoj Sharma
 */
public class CreateGroup implements PowerBiOperation<Group> {
    private String name;
    private Boolean workspaceV2;
    private Group result;
    private Gson parser;

    public CreateGroup(String name) {
        this(name, true);
    }

    public CreateGroup(String name, Boolean workspaceV2) {
        this.name = name;
        this.workspaceV2 = workspaceV2;
        this.parser = new Gson();
    }

    @Override
    public Group get() {
        return result;
    }

    @Override
    public void buildUri(UriBuilder uri) {
        uri.path(MY_ORG).path(GROUPS).queryParam("workspaceV2", String.valueOf(workspaceV2));
    }

    @Override
    public void execute(PowerBiRequest request) throws PowerBiOperationExecutionException, RateLimitExceededException, RequestAuthenticationException {
        PowerBiResponse response = request.post(createRequestJson());

        if (response.getStatus() != HttpStatus.SC_OK) {
            throw new PowerBiOperationExecutionException("Expected status code of 200.", response.getStatus(), response.getBody());
        }

        result = parseResponseJson(response.getBody());
    }

    private String createRequestJson() {
        Map<String, Object> out = new HashMap<>();
        out.put("name", name);
        return parser.toJson(out);
    }

    private Group parseResponseJson(String json) {
        Map result = parser.fromJson(json, Map.class);
        return new DefaultGroup(getString(result, "name"), getUuid(result, "id"));
    }
}
