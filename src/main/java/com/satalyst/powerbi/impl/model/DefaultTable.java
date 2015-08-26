package com.satalyst.powerbi.impl.model;

import com.satalyst.powerbi.model.Column;
import com.satalyst.powerbi.model.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Aidan Morgan
 */
public class DefaultTable implements Table {
    private String name;
    private List<Column> columns;

    public DefaultTable(String name) {
        this.name = name;
        this.columns = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    public DefaultTable addColumn(Column col) {
        columns.add(checkNotNull(col));

        return this;
    }

    @Override
    public List<Column> getColumns() {
        return columns;
    }
}
