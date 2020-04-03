package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.Objects;

public class Link {

    private String source;
    private String target;
    private double value;

    public Link(long sourceId, int sourceType, long targetId, int targetType, double value) {
        StringBuilder preSource = new StringBuilder(String.valueOf(sourceId));
        while (preSource.length()<10){
            preSource.insert(0, "0");
        }
        preSource.insert(0,String.valueOf(sourceType));

        StringBuilder preTarget = new StringBuilder(String.valueOf(targetId));
        while (preTarget.length()<10){
            preTarget.insert(0,"0");
        }
        preTarget.insert(0,String.valueOf(targetType));
        this.source = preSource.toString();
        this.target = preTarget.toString();
        this.value = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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
        return Double.compare(link.value, value) == 0 &&
                Objects.equals(source, link.source) &&
                Objects.equals(target, link.target);
    }

    @Override
    public int hashCode() {

        return Objects.hash(source, target, value);
    }
}
