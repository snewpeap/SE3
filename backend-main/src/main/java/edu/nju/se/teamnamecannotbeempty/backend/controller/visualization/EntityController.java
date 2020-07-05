package edu.nju.se.teamnamecannotbeempty.backend.controller.visualization;

import edu.nju.se.teamnamecannotbeempty.backend.service.visualization.EntityService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.AcademicEntityVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.GraphVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.SimplePaperVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 实体和展示相关的controller层
 */
@RestController()
@Service
public class EntityController {
    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    @Autowired(required = false)
    EntityService entityService;

    @RequestMapping(value = "/academic/{id}", method = RequestMethod.GET)
    public AcademicEntityVO getAcademicEntity(@PathVariable long id, @RequestParam int type) {
        return entityService.getAcademicEntity(id,type);
    }

    @RequestMapping(value = "/graph/basic/{id}", method = RequestMethod.GET)
    public GraphVO getGraph(@PathVariable long id, @RequestParam int type) {
        return entityService.getBasicGraph(id,type);
    }

    @RequestMapping(value = "/graph/more/{id}", method = RequestMethod.GET)
    public GraphVO getMoreGraph(@PathVariable long id, @RequestParam int type) {
        return entityService.getCompleteGraph(id, type);
    }

    @RequestMapping(value = "/academic/significantPapers",method = RequestMethod.GET)
    public List<SimplePaperVO> getSignificantPaper(@RequestParam long id, @RequestParam int type,@RequestParam int year, @RequestParam long termId){
        return entityService.getSignificantPaper(id,type,year,termId);
    }
}
