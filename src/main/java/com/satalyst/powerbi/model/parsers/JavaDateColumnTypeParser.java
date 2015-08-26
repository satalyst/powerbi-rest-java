package com.satalyst.powerbi.model.parsers;

import com.satalyst.powerbi.model.ColumnType;
import com.satalyst.powerbi.model.ColumnTypeParser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Aidan Morgan
 */
public class JavaDateColumnTypeParser implements ColumnTypeParser {
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private final SimpleDateFormat dateFormatter;

    public JavaDateColumnTypeParser() {
        dateFormatter = new SimpleDateFormat(DATE_FORMAT);
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public boolean accept(ColumnType ct) {
        return ct == ColumnType.DATETIME;
    }

    @Override
    public boolean accept(Object o) {
        return o instanceof Date;
    }

    @Override
    public Object parse(Object o) {
        Date d = (Date) o;
        return dateFormatter.format(d);
    }
}
