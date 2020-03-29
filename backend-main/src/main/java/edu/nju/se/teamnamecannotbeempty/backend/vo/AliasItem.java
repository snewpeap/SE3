package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.Objects;

public class AliasItem {
    private long recordId;
    private String aliasName;

    public AliasItem(long recordId, String aliasName) {
        this.recordId = recordId;
        this.aliasName = aliasName;
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
        return recordId == aliasItem.recordId &&
                Objects.equals(aliasName, aliasItem.aliasName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordId, aliasName);
    }
}
