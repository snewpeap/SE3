package edu.nju.se.teamnamecannotbeempty.backend.controller.visualization;

import edu.nju.se.teamnamecannotbeempty.backend.service.visualization.EntityService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.AcademicEntityVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.GraphVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.SimplePaperVO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    /**
     * 获得实体信息的测试
     *
     * @throws Exception
     */
    @Test
    public void getAcademicEntityTest1() throws Exception{
        entityController = new EntityController();
        AcademicEntityVO academicEntityVO = new AcademicEntityVO(1,0,"",-1,null,null, null,null,null, null,null);
        entityController.setEntityService(entityService);
        Mockito.when(entityService.getAcademicEntity(0,1)).thenReturn(academicEntityVO);
        assertEquals(academicEntityVO,entityController.getAcademicEntity(0,1));
    }

    /**
     * 获得图信息
     * @throws Exception
     */
    @Test
    public void getGraphTest1() throws Exception{
        entityController = new EntityController();
        GraphVO graphVO = new GraphVO(0,0,null,null,null,-1.0);
        entityController.setEntityService(entityService);
        Mockito.when(entityService.getBasicGraph(0,1)).thenReturn(graphVO);
        assertEquals(graphVO,entityController.getGraph(0,1));
    }

    /**
     * 获得更多的图信息
     * @throws Exception
     */
    @Test
    public void getMoreGraphTest1() throws Exception{
        entityController = new EntityController();
        GraphVO graphVO = new GraphVO(0,0,null,null,null,-1.0);
        entityController.setEntityService(entityService);
        Mockito.when(entityService.getCompleteGraph(0,1)).thenReturn(graphVO);
        assertEquals(graphVO,entityController.getMoreGraph(0,1));
    }

    /**
     * 新接口的测试
     * 通过年份和term获得代表作列表
     */
    @Test
    public void getSignificantpaper(){
        entityController = new EntityController();
        entityController.setEntityService(entityService);
        List<SimplePaperVO> list = new ArrayList<>();
        SimplePaperVO simplePaperVO = new SimplePaperVO(1,"s",null,"title",11,"2000",null);
        list.add(simplePaperVO);
        Mockito.when(entityService.getSignificantPaper(0,0,2020,11111)).thenReturn(list);
        assertEquals(list,entityController.getSignificantPaper(0,0,2020,11111));
    }

    @Test
    public void getSignificantpaper2() throws Exception {
        String url = "/academic/significantPapers";
        List<SimplePaperVO> list = new ArrayList<>();
        SimplePaperVO simplePaperVO = new SimplePaperVO(1,"s",null,"title",11,"2000",null);
        list.add(simplePaperVO);
        Mockito.when(entityService.getSignificantPaper(0,0,2020,11111)).thenReturn(list);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("id","1")
                        .param("type", "0")
                        .param("year", "2020")
                        .param("termId", "11111"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
}
