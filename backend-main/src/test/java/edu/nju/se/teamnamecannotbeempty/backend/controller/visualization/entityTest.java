package edu.nju.se.teamnamecannotbeempty.backend.controller.visualization;

import edu.nju.se.teamnamecannotbeempty.backend.service.visualization.EntityService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.AcademicEntityVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.GraphVO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class entityTest {

    private MockMvc mockMvc;
    @Mock
    private EntityService entityService;

    @InjectMocks
    private EntityController entityController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(entityController).build();
    }

    @Test
    public void getAcademicEntityTest1() throws Exception{
        entityController = new EntityController();
        AcademicEntityVO academicEntityVO = new AcademicEntityVO(1,0,"",null,null, null,null,null);
        entityController.setEntityService(entityService);
        Mockito.when(entityService.getAcedemicEntity(0,1)).thenReturn(academicEntityVO);
        assertEquals(academicEntityVO,entityController.getAcademicEntity(0,1));
    }

    @Test
    public void getGraphTest1() throws Exception{
        entityController = new EntityController();
        GraphVO graphVO = new GraphVO(0,null,null);
        entityController.setEntityService(entityService);
        Mockito.when(entityService.getBasicGraph(0,1)).thenReturn(graphVO);
        assertEquals(graphVO,entityController.getGraph(0,1));
    }

    @Test
    public void getMoreGraphTest1() throws Exception{
        entityController = new EntityController();
        GraphVO graphVO = new GraphVO(0,null,null);
        entityController.setEntityService(entityService);
        Mockito.when(entityService.getCompleteGraph(0,1)).thenReturn(graphVO);
        assertEquals(graphVO,entityController.getMoreGraph(0,1));
    }

}
