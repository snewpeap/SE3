package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.List;
import java.util.Objects;

public class RankVO {
    private int totalPage;

    private List<RankItem> rankList;

    public RankVO(int totalPage, List<RankItem> rankList) {
        this.totalPage = totalPage;
        this.rankList = rankList;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<RankItem> getRankList() {
        return rankList;
    }

    public void setRankList(List<RankItem> rankList) {
        this.rankList = rankList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RankVO rankVO = (RankVO) o;
        return totalPage == rankVO.totalPage &&
                Objects.equals(rankList, rankVO.rankList);
    }

    @Override
    public int hashCode() {

        return Objects.hash(totalPage, rankList);
    }
}
