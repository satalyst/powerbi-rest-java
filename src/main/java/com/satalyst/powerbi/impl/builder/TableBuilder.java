package com.satalyst.powerbi.impl.builder;

import com.satalyst.powerbi.impl.model.DefaultTable;
import com.satalyst.powerbi.model.Column;
import com.satalyst.powerbi.model.ColumnType;
import com.satalyst.powerbi.model.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple fluent interface for building a {@see Table} instance.
 *
 * @author Aidan Morgan
 */
public class TableBuilder {
    private String name;
    private List<ColumnBuilder> columns;

    public TableBuilder() {
        this.columns = new ArrayList<>();
    }

    public TableBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public TableBuilder addColumn(String name, ColumnType type) {
        columns.add(new ColumnBuilder().setName(name).setType(type));
        return this;
    }

    public Table build() {
        DefaultTable table = new DefaultTable(name);
        for(ColumnBuilder cb: columns) {
            table.addColumn(cb.build());
        }

        return table;
    }
}
