package com.satalyst.powerbi.model;

import com.satalyst.powerbi.model.parsers.JavaDateColumnTypeParser;
import com.satalyst.powerbi.model.parsers.JodaDateColumnTypeParser;
import com.satalyst.powerbi.model.parsers.NumberColumnTypeParser;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Simple enum that represents the column types supported by PowerBI, as well as providing a registration point
 * for parsers to convert from Java objects to PowerBI object types.
 *
 * @author Aidan Morgan
 */
public class ColumnType {
    private static List<ColumnType> columnTypes = new ArrayList<>();
    private static List<ColumnTypeParser> parsers = new ArrayList<>();

    public static void registerColumnTypeParser(ColumnTypeParser parser) {
        parsers.add(checkNotNull(parser));
    }

    static {
        registerColumnTypeParser(new NumberColumnTypeParser());
        registerColumnTypeParser(new JavaDateColumnTypeParser());
        registerColumnTypeParser(new JodaDateColumnTypeParser());
    }

    private static ColumnType create(String name) {
        ColumnType ct = new ColumnType(name);
        columnTypes.add(ct);

        return ct;
    }

    public static final ColumnType INT64 = create("Int64");
    public static final ColumnType STRING = create("string");
    public static final ColumnType BOOL = create("bool");
    public static final ColumnType DATETIME = create("DateTime");


    private String name;

    private ColumnType(String name) {
        this.name = checkNotNull(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ColumnType that = (ColumnType) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .toHashCode();
    }

    public Object parse(Object value) {
        if (value == null) {
            return null;
        }

        // TODO : For performance, introduce a parser cache here for commonly used ColumnType & Class values to save
        // TODO : on traversing the list all the time.
        for (ColumnTypeParser parser : parsers) {
            if (parser.accept(this) && parser.accept(value)) {
                return parser.parse(value);
            }
        }

        // Intentionally just returning the value if no parser can be found, saves on writing String/Integer/Long parsers.
        return value;
    }

    public static ColumnType fromString(String type) {
        for(ColumnType ct : columnTypes) {
            if(StringUtils.equalsIgnoreCase(ct.getName(), type)) {
                return ct;
            }
        }

        return null;
    }
}
