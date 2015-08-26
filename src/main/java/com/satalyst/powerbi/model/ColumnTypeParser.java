package com.satalyst.powerbi.model;

/**
 * Used to convert from java types to PowerBI column types.
 *
 * Must be registered with the {@see ColumnType} enum to be used.
 *
 * @author Aidan Morgan
 */
public interface ColumnTypeParser {
    /**
     * Returns {@code true} if this parser can produce an output for the provided {@see ColumnType}, {@code false} otherwise.
     * @param ct the {@see ColumnType} to be produced.
     */
    public boolean accept(ColumnType ct);

    /**
     * Returns {@see true} if this parser can parse the provided {@see Object}, {@code false} otherwise.
     * @param o the {@see Object} to parse
     */
    public boolean accept(Object o);

    /**
     * Performs the parsing of the provided {@see Object} to the type expected by the {@see ColumnType}
     * @param o the {@see Object} to parse.
     * @return the {@see Object} representation of the provided {@see Object}, using the correct type for the PowerBI API.
     */
    public Object parse(Object o);
}
