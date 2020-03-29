package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.Objects;

public class TermItem {
    private long id;
    private String name;
    private double hot;

    public TermItem(long id, String name, double hot) {
        this.id = id;
        this.name = name;
        this.hot = hot;
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

    public double getHot() {
        return hot;
    }

    public void setHot(double hot) {
        this.hot = hot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TermItem termItem = (TermItem) o;
        return id == termItem.id &&
                Double.compare(termItem.hot, hot) == 0 &&
                Objects.equals(name, termItem.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, hot);
    }
}
