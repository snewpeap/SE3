package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.List;
import java.util.Objects;

public class GraphVO {
    private long centerId;
    private List<Node> nodes;
    private List<Link> links;

    public GraphVO(long centerId, List<Node> nodes, List<Link> links) {
        this.centerId = centerId;
        this.nodes = nodes;
        this.links = links;
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
                Objects.equals(nodes, graphVO.nodes) &&
                Objects.equals(links, graphVO.links);
    }

    @Override
    public int hashCode() {

        return Objects.hash(centerId, nodes, links);
    }
}
