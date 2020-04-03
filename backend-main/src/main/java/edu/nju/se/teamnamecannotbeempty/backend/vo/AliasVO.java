package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.List;
import java.util.Objects;

public class AliasVO {
    private String origin;
    private long originId;
    /*
    1=作者
    2=机构
     */
    private int type;
    private List<AliasItem> alias;

    public AliasVO(String origin, long originId, int type, List<AliasItem> alias) {
        this.origin = origin;
        this.originId = originId;
        this.type = type;
        this.alias = alias;
    }

    public AliasVO() {
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<AliasItem> getAlias() {
        return alias;
    }

    public void setAlias(List<AliasItem> alias) {
        this.alias = alias;
    }

    public long getOriginId() {
        return originId;
    }

    public void setOriginId(long originId) {
        this.originId = originId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AliasVO aliasVO = (AliasVO) o;
        return originId == aliasVO.originId &&
                type == aliasVO.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(originId, type);
    }
}
