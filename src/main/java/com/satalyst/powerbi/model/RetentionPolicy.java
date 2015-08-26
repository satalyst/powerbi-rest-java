package com.satalyst.powerbi.model;

/**
 * @author Aidan Morgan
 */
public enum RetentionPolicy {
    FIFO("basicFIFO"),
    NONE("None");


    private final String name;

    RetentionPolicy(String val) {
        this.name = val;
    }

    public String getName() {
        return name;
    }
}
