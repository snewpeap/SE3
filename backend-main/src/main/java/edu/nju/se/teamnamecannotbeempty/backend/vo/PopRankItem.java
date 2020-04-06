package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.Objects;

public class PopRankItem {

    private long id;
    private String name;
    private double value;

    public PopRankItem(long id, String name, double value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        PopRankItem that = (PopRankItem) o;
        return id == that.id &&
                Double.compare(that.value, value) == 0 &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, value);
    }
}
