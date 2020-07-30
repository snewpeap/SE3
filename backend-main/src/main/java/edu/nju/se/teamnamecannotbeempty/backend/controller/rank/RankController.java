package edu.nju.se.teamnamecannotbeempty.backend.controller.rank;

import edu.nju.se.teamnamecannotbeempty.backend.service.rank.RankService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.PopRankItem;
import edu.nju.se.teamnamecannotbeempty.backend.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 排行
 *
 * @author ava_xu
 */
@RestController()
public class RankController {
    @Autowired
    RankService rankService;

    public void setRankService(RankService rankService) {
        this.rankService = rankService;
    }

    /**
     * 获得用户要求的排行榜
     *
     * @param mode       排行模式
     * @param pageNumber 当前加载到第几页
     * @param descend    是否降序
     * @param startYear  开始年份
     * @param endYear    结束年份
     * @return 排行榜信息 RankVO
     */
    @RequestMapping(value = "/rank/{mode}")
    public ResponseVO getRank(@PathVariable String mode, @RequestParam(required = false) Integer pageNumber, @RequestParam boolean descend, @RequestParam int startYear, @RequestParam int endYear) {
        return rankService.getRank(mode, pageNumber, descend, startYear, endYear);
    }

    /**
     * 获取实体热度排行榜（作者，机构，研究方向）
     *
     * @param type 实体类型
     * @return 排行榜（前20）
     */
    @RequestMapping(value = "/hot", method = RequestMethod.GET)
    public List<PopRankItem> getPopRank(@RequestParam int type) {
        return rankService.getPopRank(type);
    }
}
