package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.rank;

import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.RankMsg;
import edu.nju.se.teamnamecannotbeempty.backend.service.rank.RankService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.RankItem;
import edu.nju.se.teamnamecannotbeempty.backend.vo.RankVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RankServiceImpl implements RankService {


    private final RankMsg rankMsg;
    private final RankFecth rankFecth;

    @Autowired
    public RankServiceImpl(RankMsg rankMsg, RankFecth rankFecth) {
        this.rankMsg = rankMsg;
        this.rankFecth = rankFecth;
    }


    @Override
    public ResponseVO getRank(String mode, Integer pageNumber, boolean descend, int startYear, int endYear) {
        if (pageNumber == null || pageNumber <= 0) pageNumber = 1;

        List<RankItem> rankItemList = rankFecth.getAllResult(mode, startYear, endYear);
        int len = rankItemList.size();
        ResponseVO responseVO;
        if (pageNumber * rankMsg.getEachNum() - len > rankMsg.getEachNum()) {
            responseVO = ResponseVO.fail(rankMsg.getMismatchPageNumber());
            return responseVO;
        }

        int totalPage = len % rankMsg.getEachNum() == 0 ? len / rankMsg.getEachNum() : len / rankMsg.getEachNum() + 1;
        int resultStart = (pageNumber - 1) * rankMsg.getEachNum();
        int resultLen = Math.min(len - resultStart, rankMsg.getEachNum());
        List<RankItem> result;
        if (descend) {
            List<RankItem> reverseItems = new ArrayList<>(rankItemList);
            Collections.reverse(reverseItems);
            result = reverseItems.subList(resultStart, resultStart + resultLen);
        } else {
            result = rankItemList.subList(resultStart, resultStart + resultLen);
        }
        responseVO = ResponseVO.success();
        responseVO.setContent(new RankVO(totalPage, result));
        return responseVO;
    }


}
