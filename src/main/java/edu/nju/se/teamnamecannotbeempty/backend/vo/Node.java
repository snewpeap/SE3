package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.Objects;

public class Node {

    private long id;
    private String name;
    private int type;

    public Node(long id, String name, int type) {
        this.id = id;
        this.name = name;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id == node.id &&
                type == node.type &&
                Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, type);
    }
}
