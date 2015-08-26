package com.satalyst.powerbi.impl;

import com.google.gson.Gson;
import com.satalyst.powerbi.TableSchemaLoader;
import com.satalyst.powerbi.impl.builder.TableBuilder;
import com.satalyst.powerbi.model.ColumnType;
import com.satalyst.powerbi.model.Table;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Simple class that will load a table definition (Table and Columns) from a JSON file.
 *
 * @author Aidan Morgan
 */
public class JsonTableSchemaLoader implements TableSchemaLoader {
    public static final String TABLES_KEY = "tables";
    public static final String NAME_KEY = "name";
    public static final String COLUMNS_KEY = "columns";
    public static final String TYPE_KEY = "type";
    private Map<String, Object> contents;

    public static JsonTableSchemaLoader loadFromString(String s) {
        Gson parser = new Gson();
        return new JsonTableSchemaLoader(parser.fromJson(s, Map.class));
    }

    public static JsonTableSchemaLoader loadFromFile(File f) throws IOException {
        Gson parser = new Gson();
        return loadFromString(FileUtils.readFileToString(f));
    }

    public static JsonTableSchemaLoader loadFromStream(InputStream stream) throws IOException {
        Gson parser = new Gson();
        return loadFromString(StringUtils.join(IOUtils.readLines(stream).toArray()));
    }

    private JsonTableSchemaLoader(Map<String, Object> val) {
        this.contents = val;
    }

    public List<Table> getTables() {
        List<Table> results = new ArrayList<>();
        List<Map<String,Object>> tables = getList(contents, TABLES_KEY);

        for(Map<String,Object> table : tables) {
            TableBuilder tableBuilder = new TableBuilder()
                    .setName(getString(table, NAME_KEY));

            List<Map<String, Object>> columns = getList(table, COLUMNS_KEY);

            for(Map<String, Object> column : columns) {
                tableBuilder.addColumn(getString(column, NAME_KEY),
                        ColumnType.fromString(getString(column, TYPE_KEY)));
            }

            results.add(tableBuilder.build());
        }

        return results;
    }

    @Override
    public Table getTable(String name) {
        List<Table> tables = getTables();

        for(Table t : tables) {
            if(StringUtils.equals(t.getName(), name)) {
                return t;
            }
        }

        return null;
    }

    private static List<Map<String, Object>> getList(Map<String, Object> obj, String name) {
        return (List<Map<String,Object>>) obj.get(name);
    }

    private static String getString(Map<String, Object> obj,String name) {
        return (String) obj.get(name);
    }


}
