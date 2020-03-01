package edu.nju.se.teamnamecannotbeempty.backend.controller.paper;

import edu.nju.se.teamnamecannotbeempty.backend.service.paper.PaperService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.PaperVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * 论文
 * @author ava_xu
 *
 */

@RestController
public class PaperController {
    @Autowired PaperService paperService;

    /**
     * 获得符合用户搜索条件的论文列表
     * @param text 用户输入在搜索框中的文本
     * @param mode 搜索模式（是否是模糊搜索）
     * @param pageNumber 当前展示到第几页（没有默认为1）
     * @param sortmode 排序模式（默认降序）
     * @param perpage 每页显示多少条目
     * @return （应该是SimplePaperVO的List??）
     */
    @RequestMapping(value = "/search/{text}/{mode}",method = RequestMethod.GET)
    public List<Object> search(@PathVariable String text,@PathVariable String mode,@RequestParam(required = false) int pageNumber,@RequestParam String sortmode,int perpage){
        return paperService.search(text,mode,pageNumber,sortmode,perpage);
    }


    /**
     * 获得某一篇论文的详细信息
     * @param id 论文id
     * @return 论文详细信息PaperVO??
     */
    @RequestMapping(value = "/paperDetail/{id}",method = RequestMethod.GET)
    public Object getPaper(@PathVariable long id){
        return paperService.getPaper(id);
    }

}
