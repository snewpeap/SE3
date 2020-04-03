package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.Objects;

public class Node {

    private String id;
    private long entityId;
    private String entityName;
    private int entityType;

    public Node(long entityId, String entityName, int entityType) {
        StringBuilder preId = new StringBuilder(String.valueOf(entityId));
        while (preId.length()<10){
            preId.insert(0, "0");
        }
        preId.insert(0,String.valueOf(entityType));
        this.id = preId.toString();
        this.entityId = entityId;
        this.entityName = entityName;
        this.entityType = entityType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return entityId == node.entityId &&
                entityType == node.entityType &&
                Objects.equals(id, node.id) &&
                Objects.equals(entityName, node.entityName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, entityId, entityName, entityType);
    }
}
