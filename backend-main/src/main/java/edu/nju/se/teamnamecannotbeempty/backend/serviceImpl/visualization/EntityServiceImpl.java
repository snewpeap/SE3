package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.visualization;

import edu.nju.se.teamnamecannotbeempty.backend.service.visualization.EntityService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.AcademicEntityVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.GraphVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.SimplePaperVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntityServiceImpl implements EntityService {

    private final AcademicEntityFetch academicEntityFetch;
    private final BasicGraphFetch basicGraphFetch;
    private final CompleteGraphFetch completeGraphFetch;

    @Autowired
    public EntityServiceImpl(AcademicEntityFetch academicEntityFetch, BasicGraphFetch basicGraphFetch,
                             CompleteGraphFetch completeGraphFetch) {
        this.academicEntityFetch = academicEntityFetch;
        this.basicGraphFetch = basicGraphFetch;
        this.completeGraphFetch = completeGraphFetch;
    }

    @Override
    public AcademicEntityVO getAcademicEntity(long id, int type) {
        return academicEntityFetch.getAcademicEntity(id, type);
    }

    @Override
    public GraphVO getBasicGraph(long id, int type) {
        return basicGraphFetch.getBasicGraph(id, type);
    }

    @Override
    public GraphVO getCompleteGraph(long id, int type) {
        return completeGraphFetch.getCompleteGraph(id, type);
    }

    @Override
    public List<SimplePaperVO> getSignificantPaper(long id, int type, int year, long termId) {
        return null;
    }
}
