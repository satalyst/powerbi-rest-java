package com.satalyst.powerbi.model;

import java.util.List;
import java.util.UUID;

/**
 * Represents a dataset in the PowerBI data model.
 * @author Aidan Morgan
 */
public interface Dataset {
    public UUID getId();
    public String getName();
}
