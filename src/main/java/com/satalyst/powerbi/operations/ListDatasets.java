package com.satalyst.powerbi.operations;

import com.google.gson.Gson;
import com.satalyst.powerbi.impl.model.DefaultDataset;
import com.satalyst.powerbi.model.Dataset;

import static com.satalyst.powerbi.operations.MapUtils.*;

import javax.ws.rs.core.UriBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Aidan Morgan
 */
public class ListDatasets extends AbstractGetOperation<List<Dataset>> {

    @Override
    public void buildUri(UriBuilder uri) {
        uri.path("myorg").path("datasets");
    }

    protected List<Dataset> parseJson(Gson parser, String value) {
        List<Dataset> result = new ArrayList<>();

        Map parsed = parser.fromJson(value, Map.class);

        List<Map> datasets = getList(parsed, "value");

        for (Map dataset : datasets) {
            result.add(new DefaultDataset(getString(dataset, "name"), UUID.fromString(getString(dataset, "id"))));
        }

        return result;
    }
}
