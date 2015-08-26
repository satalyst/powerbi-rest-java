package com.satalyst.powerbi.impl.model;

import com.satalyst.powerbi.model.Column;
import com.satalyst.powerbi.model.ColumnType;

/**
 * @author Aidan Morgan
 */
public class DefaultColumn implements Column {
    private String name;
    private ColumnType columnType;

    public DefaultColumn(String name, ColumnType columnType) {
        this.name = name;
        this.columnType = columnType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ColumnType getColumnType() {
        return columnType;
    }

}
