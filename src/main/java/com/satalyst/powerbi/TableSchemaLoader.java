package com.satalyst.powerbi;

import com.satalyst.powerbi.model.Table;

import java.util.List;

/**
 * Provides the ability to load a {@see Table} definition from an external resource.
 * @author Aidan Morgan
 */
public interface TableSchemaLoader {
    public List<Table> getTables();

    public Table getTable(String name);
}
