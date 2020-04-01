package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.Objects;

public class Link {
    private long sourceId;
    private int sourceType;
    private long targetId;
    private int targetType;
    private double value;

    public Link(long sourceId, int sourceType, long targetId, int targetType, double value) {
        this.sourceId = sourceId;
        this.sourceType = sourceType;
        this.targetId = targetId;
        this.targetType = targetType;
        this.value = value;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public int getTargetType() {
        return targetType;
    }

    public void setTargetType(int targetType) {
        this.targetType = targetType;
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
                sourceType == link.sourceType &&
                targetId == link.targetId &&
                targetType == link.targetType &&
                Double.compare(link.value, value) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(sourceId, sourceType, targetId, targetType, value);
    }
}
