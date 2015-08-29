package com.satalyst.powerbi.model.parsers;

import com.satalyst.powerbi.model.ColumnType;
import com.satalyst.powerbi.model.ColumnTypeParser;

/**
 * @author Aidan Morgan
 */
public class NumberColumnTypeParser implements ColumnTypeParser {
    @Override
    public boolean accept(ColumnType ct) {
        return ct == ColumnType.INT64;
    }

    @Override
    public boolean accept(Object o) {
        return o instanceof Number;
    }

    @Override
    public Object parse(Object o) {
        return ((Number) o).longValue();
    }
}
