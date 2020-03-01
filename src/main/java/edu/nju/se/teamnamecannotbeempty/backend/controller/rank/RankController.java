package edu.nju.se.teamnamecannotbeempty.backend.controller.rank;

import edu.nju.se.teamnamecannotbeempty.backend.service.rank.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 排行
 * @author ava_xu
 *
 */
@RestController
public class RankController {
    @Autowired RankService rankService;

    /**
     * 获得用户要求的排行榜
     * @param mode 排行模式
     * @param pageNumber 当前加载到第几页
     * @param descend 是否降序
     * @param startYear 开始年份
     * @param endYear 结束年份
     * @return 排行榜信息 RankVO??
     */
    @RequestMapping(value = "/rank/{mode}")
    public Object getRank(@PathVariable String mode, @RequestParam(required = false) int pageNumber,@RequestParam boolean descend,@RequestParam int startYear,@RequestParam int endYear){
        return rankService.getRank(mode,pageNumber,descend,startYear,endYear);
    }
}
