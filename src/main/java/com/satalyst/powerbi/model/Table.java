package com.satalyst.powerbi.model;

import java.util.List;

/**
 * Represents a table in the PowerBI data model.
 *
 * @author Aidan Morgan
 */
public interface Table {
    /**
     * Returns the name of the table.
     * @return the name of the table.
     */
    public String getName();

    /**
     * Returns a {@see List} of {@see Column} that make up this table.
     * @return a {@see List} of {@see Column} that make up this table.
     */
    public List<Column> getColumns();
}
