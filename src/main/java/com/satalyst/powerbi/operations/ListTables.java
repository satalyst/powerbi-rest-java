package com.satalyst.powerbi.operations;

import com.google.gson.Gson;

import javax.ws.rs.core.UriBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.satalyst.powerbi.operations.MapUtils.getList;
import static com.satalyst.powerbi.operations.MapUtils.getString;

/**
 * @author Aidan Morgan
 */
public class ListTables extends AbstractGetOperation<List<String>> {
    private UUID datasetId;

    public ListTables(UUID datasetId) {
        this.datasetId = checkNotNull(datasetId);
    }

    @Override
    public void buildUri(UriBuilder uri) {
        uri.path("myorg").path("datasets").path(datasetId.toString()).path("tables");

    }

    protected List<String> parseJson(Gson parser, String s) {
        List<String> result = new ArrayList<>();

        Map parsed = parser.fromJson(s, Map.class);

        List<Map> tables = getList(parsed, "value");

        for (Map table : tables) {
            result.add(getString(table, "name"));
        }

        return result;
    }
}
