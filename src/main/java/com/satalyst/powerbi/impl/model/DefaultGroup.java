package com.satalyst.powerbi.impl.model;

import com.satalyst.powerbi.model.Group;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Manoj Sharma
 */
public class DefaultGroup implements Group {
    private String name;
    private UUID id;

    public DefaultGroup(String name, UUID id) {
        this.name = checkNotNull(name);
        this.id = checkNotNull(id);
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DefaultGroup that = (DefaultGroup) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("id", id)
                .toString();
    }
}
