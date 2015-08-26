package com.satalyst.powerbi.operations;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Aidan Morgan
 */
public class MapUtils {

    public static String getString(Map map, String name) {
        return (String) map.get(name);
    }

    public static List<Map> getList(Map map, String name) {
        return (List<Map>) map.get(name);
    }

    public static UUID getUuid(Map map, String name) {
        return UUID.fromString(getString(map, name));
    }
}
