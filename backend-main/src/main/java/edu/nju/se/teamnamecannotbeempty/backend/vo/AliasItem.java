package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.Objects;
import java.util.StringJoiner;

public class AliasItem {
    private long fatherId;
    private String aliasName;

    public AliasItem(long fatherId, String aliasName) {
        this.fatherId = fatherId;
        this.aliasName = aliasName;
    }

    public long getFatherId() {
        return fatherId;
    }

    public void setFatherId(long fatherId) {
        this.fatherId = fatherId;
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
        return fatherId == aliasItem.fatherId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fatherId);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AliasItem.class.getSimpleName() + "[", "]")
                .add("fatherId=" + fatherId)
                .add("aliasName='" + aliasName + "'")
                .toString();
    }
}
