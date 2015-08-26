package com.satalyst.powerbi.model;

/**
 * Represents a Column in the PowerBI data model.
 * @author Aidan Morgan
 */
public interface Column {
    public String getName();
    public ColumnType getColumnType();
}
