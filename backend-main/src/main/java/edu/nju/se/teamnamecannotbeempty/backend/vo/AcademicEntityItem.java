package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.Objects;

public class AcademicEntityItem {
    private int type;
    private long id;
    private String name;
    //以空格隔开的字符串，n个数字，第一个数字是年份
    private String popTrend;

    public AcademicEntityItem(int type, long id, String name, String popTrend) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.popTrend = popTrend;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getPopTrend() {
        return popTrend;
    }

    public void setPopTrend(String popTrend) {
        this.popTrend = popTrend;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AcademicEntityItem that = (AcademicEntityItem) o;
        return type == that.type &&
                id == that.id &&
                Objects.equals(name, that.name)&&
                Objects.equals(popTrend, that.popTrend);
    }

    @Override
    public int hashCode() {

        return Objects.hash(type, id, name, popTrend);
    }
}
