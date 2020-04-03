package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.Objects;

public class AliasItem {
    private long recordId;
    private long targetId;
    private String aliasName;

    public AliasItem(long recordId, long targetId, String aliasName) {
        this.recordId = recordId;
        this.targetId = targetId;
        this.aliasName = aliasName;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AliasItem aliasItem = (AliasItem) o;
        return recordId == aliasItem.recordId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordId);
    }
}
