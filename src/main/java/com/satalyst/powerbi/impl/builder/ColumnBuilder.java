package com.satalyst.powerbi.impl.builder;

import com.satalyst.powerbi.impl.model.DefaultColumn;
import com.satalyst.powerbi.model.Column;
import com.satalyst.powerbi.model.ColumnType;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple fluent interface for building a {@see Column}.
 *
 * @author Aidan Morgan
 */
public class ColumnBuilder {
    private String name;
    private ColumnType type;

    public ColumnBuilder setName(String name) {
        this.name = checkNotNull(name);
        return this;
    }

    public ColumnBuilder setType(ColumnType type) {
        this.type = checkNotNull(type);
        return this;
    }

    public Column build() {
        return new DefaultColumn(name, type);
    }
}
