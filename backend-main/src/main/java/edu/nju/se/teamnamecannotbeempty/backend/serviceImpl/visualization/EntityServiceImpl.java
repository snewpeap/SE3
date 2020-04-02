package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.visualization;

import edu.nju.se.teamnamecannotbeempty.backend.service.visualization.EntityService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.AcademicEntityVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.GraphVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntityServiceImpl implements EntityService {

    private final AcademicEntityFecth academicEntityFecth;
    private final BasicGraphFecth basicGraphFecth;
    private final CompleteGraphFecth completeGraphFecth;

    @Autowired
    public EntityServiceImpl(AcademicEntityFecth academicEntityFecth, BasicGraphFecth basicGraphFecth,
                             CompleteGraphFecth completeGraphFecth) {
        this.academicEntityFecth = academicEntityFecth;
        this.basicGraphFecth = basicGraphFecth;
        this.completeGraphFecth = completeGraphFecth;
    }

    @Override
    public AcademicEntityVO getAcedemicEntity(long id, int type) {
        return academicEntityFecth.getAcedemicEntity(id, type);
    }

    @Override
    public GraphVO getBasicGraph(long id, int type) {
        return basicGraphFecth.getBasicGraph(id, type);
    }

    @Override
    public GraphVO getCompleteGraph(long id, int type) {
        return completeGraphFecth.getCompleteGraph(id, type);
    }
}
