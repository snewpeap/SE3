package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.List;
import java.util.Objects;

public class AliasVO {
    private String origin;
    private int type;
    private List<AliasItem> alias;

    public AliasVO(String origin, int type, List<AliasItem> alias) {
        this.origin = origin;
        this.type = type;
        this.alias = alias;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AliasVO aliasVO = (AliasVO) o;
        return type == aliasVO.type &&
                Objects.equals(origin, aliasVO.origin) &&
                Objects.equals(alias, aliasVO.alias);
    }

    @Override
    public int hashCode() {

        return Objects.hash(origin, type, alias);
    }
}
