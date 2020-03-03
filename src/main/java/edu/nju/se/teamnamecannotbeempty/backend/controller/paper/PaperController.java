package edu.nju.se.teamnamecannotbeempty.backend.controller.paper;

import edu.nju.se.teamnamecannotbeempty.backend.service.paper.PaperService;
import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.paper.PaperServiceImpl;
import edu.nju.se.teamnamecannotbeempty.backend.vo.PaperVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.ResponseVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.SimplePaperVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * 论文controller层
 * @author ava_xu
 *
 */

@RestController
@Service
public class PaperController {
    @Autowired
    PaperService paperService;

    public void setPaperService(PaperService paperService) {
        this.paperService = paperService;
    }

    /**
     * 获得符合用户搜索条件的论文列表
     * @param text 用户输入在搜索框中的文本
     * @param mode 搜索模式（是否是模糊搜索）
     * @param pageNumber 当前展示到第几页（没有默认为1）
     * @param sortMode 排序模式（默认降序）
     * @param perPage 每页显示多少条目
     * @return （应该是SimplePaperVO的List??）
     */
    @RequestMapping(value = "/search/{text}/{mode}",method = RequestMethod.GET)
    public List<SimplePaperVO> search(@PathVariable String text, @PathVariable String mode, @RequestParam(required = false) int pageNumber, @RequestParam String sortMode, @RequestParam int perPage){
        return paperService.search(text,mode,pageNumber,sortMode,perPage);
    }


    /**
     * 获得某一篇论文的详细信息
     * @param id 论文id
     * @return 论文详细信息PaperVO??
     */
    @RequestMapping(value = "/paperDetail/{id}",method = RequestMethod.GET)
    public ResponseVO getPaper(@PathVariable long id){
        return paperService.getPaper(id);
    }

}
