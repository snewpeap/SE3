package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.List;

public class RankVO {
    private int totalPage;

    private List <RankItem> rankList;

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
}
