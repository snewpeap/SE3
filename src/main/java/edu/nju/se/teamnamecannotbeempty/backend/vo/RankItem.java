package edu.nju.se.teamnamecannotbeempty.backend.vo;

public class RankItem {
    private String name;

    private int value;

    public RankItem(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
