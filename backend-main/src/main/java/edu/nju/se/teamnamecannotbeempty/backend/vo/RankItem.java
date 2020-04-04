package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.Objects;

public class RankItem {
    private String name;

    private double value;

    public RankItem(String name, double value) {
        this.name = name;
        this.value = value;
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
        RankItem rankItem = (RankItem) o;
        return value == rankItem.value &&
                Objects.equals(name, rankItem.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
