package edu.nju.se.teamnamecannotbeempty.backend.controller.visualization;

import edu.nju.se.teamnamecannotbeempty.backend.service.visualization.EntityService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.AcademicEntityVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.GraphVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 实体和展示相关的controller层
 */
@RestController()
@Service
public class EntityController {
    @Autowired(required = false)
    EntityService entityService;

    @RequestMapping(value = "/academic/{id}?type=", method = RequestMethod.GET)
    public AcademicEntityVO getAcademicEntity(@PathVariable long id, @PathVariable int type) {
        return null;
    }

    @RequestMapping(value = "/graph/basic/{id}?type=", method = RequestMethod.GET)
    public GraphVO getGraph(@PathVariable long id, @PathVariable int type) {
        return null;
    }

    @RequestMapping(value = "/graph/more/{id}?type=", method = RequestMethod.GET)
    public GraphVO getMoreGraph(@PathVariable long id, @PathVariable int type) {
        return null;
    }
}
