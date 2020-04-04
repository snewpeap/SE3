package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.*;

public class AliasVO {
    private String name;
    private long sonId;
    /*
    1=作者
    2=机构
     */
    private int type;
    private List<AliasItem> fathers;

    public AliasVO(String name, long sonId, int type, List<AliasItem> fathers) {
        this.name = name;
        this.sonId = sonId;
        this.type = type;
        this.fathers = fathers;
    }

    public AliasVO() {
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

    public List<AliasItem> getFathers() {
        return fathers;
    }

    public void setFathers(List<AliasItem> fathers) {
        this.fathers = fathers;
    }

    public long getSonId() {
        return sonId;
    }

    public void setSonId(long sonId) {
        this.sonId = sonId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AliasVO aliasVO = (AliasVO) o;
        return sonId == aliasVO.sonId &&
                type == aliasVO.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sonId, type);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AliasVO.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("sonId=" + sonId)
                .add("type=" + type)
                .add("fathers.size=" + Optional.ofNullable(fathers).orElse(Collections.emptyList()).size())
                .toString();
    }
}
