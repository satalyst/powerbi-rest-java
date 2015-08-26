package com.satalyst.powerbi.model.parsers;

import com.satalyst.powerbi.model.ColumnType;
import com.satalyst.powerbi.model.ColumnTypeParser;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


/**
 * Implementation of the {@see ColumnTypeParser} interface for processing {@code Joda Time} instances.
 *
 * @author Aidan Morgan
 */
public class JodaDateColumnTypeParser implements ColumnTypeParser {
    private static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    // TODO : assuming that a date is always considered midnight for our purposes, may need to change..
    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'00:00:00.000'Z'");

    @Override
    public boolean accept(ColumnType ct) {
        return ct == ColumnType.DATETIME;
    }

    @Override
    public boolean accept(Object o) {
        return o instanceof LocalDate || o instanceof LocalDateTime;
    }

    @Override
    public Object parse(Object o) {
        if(o instanceof LocalDate) {
            LocalDate localDate = (LocalDate) o;

            return DATE_FORMATTER.print(localDate);
        }
        else if(o instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) o;
            return DATE_TIME_FORMATTER.print(localDateTime.toDateTime().withZone(DateTimeZone.UTC));
        }

        throw new IllegalArgumentException("Unrecognised object type. Expected " + LocalDate.class.getName() + " or " + LocalDateTime.class.getName() + ".");
    }
}
