package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.List;
import java.util.Objects;

public class GraphVO {
    private String id;
    private long centerId;
    private int centerType;
    private String centerName;
    private List<Node> nodes;
    private List<Link> links;

    public GraphVO(long centerId, int centerType, String centerName, List<Node> nodes, List<Link> links) {
        StringBuilder preId = new StringBuilder(String.valueOf(centerId));
        while (preId.length()<10){
            preId.insert(0,"0");
        }
        preId.insert(0,String.valueOf(centerType));
        this.id = preId.toString();
        this.centerId = centerId;
        this.centerType = centerType;
        this.centerName = centerName;
        this.nodes = nodes;
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCenterType() {
        return centerType;
    }

    public void setCenterType(int centerType) {
        this.centerType = centerType;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public long getCenterId() {
        return centerId;
    }

    public void setCenterId(long centerId) {
        this.centerId = centerId;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphVO graphVO = (GraphVO) o;
        return centerId == graphVO.centerId &&
                centerType == graphVO.centerType &&
                Objects.equals(id, graphVO.id) &&
                Objects.equals(centerName, graphVO.centerName) &&
                Objects.equals(nodes, graphVO.nodes) &&
                Objects.equals(links, graphVO.links);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, centerId, centerType, centerName, nodes, links);
    }
}
