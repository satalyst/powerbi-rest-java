package com.satalyst.powerbi.operations;

import com.google.gson.Gson;
import com.satalyst.powerbi.impl.model.DefaultGroup;
import com.satalyst.powerbi.model.Group;

import javax.ws.rs.core.UriBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.satalyst.powerbi.impl.PowerBiConstants.GROUPS;
import static com.satalyst.powerbi.impl.PowerBiConstants.MY_ORG;
import static com.satalyst.powerbi.operations.MapUtils.getList;
import static com.satalyst.powerbi.operations.MapUtils.getString;

/**
 * @author Manoj Sharma
 */
public class ListGroups extends AbstractGetOperation<List<Group>> {

    @Override
    public void buildUri(UriBuilder uri) {
        uri.path(MY_ORG).path(GROUPS);
    }

    protected List<Group> parseJson(Gson parser, String value) {
        List<Group> result = new ArrayList<>();

        Map parsed = parser.fromJson(value, Map.class);

        List<Map> groups = getList(parsed, "value");

        for (Map group : groups) {
            DefaultGroup createdGroup = new DefaultGroup(
                    getString(group, "name"),
                    UUID.fromString(getString(group, "id"))
            );
            result.add(createdGroup);
        }

        return result;
    }
}
