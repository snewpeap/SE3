package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.Objects;

public class Link {
    private long sourceId;
    private long targetId;
    private double value;

    public Link(long sourceId, long targetId, double value) {
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.value = value;
    }

    public long getSourceId() {
        return sourceId;
    }

    public void setSourceId(long sourceId) {
        this.sourceId = sourceId;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link = (Link) o;
        return sourceId == link.sourceId &&
                targetId == link.targetId &&
                Double.compare(link.value, value) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(sourceId, targetId, value);
    }
}
