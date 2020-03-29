package edu.nju.se.teamnamecannotbeempty.backend.controller.visualization;

import edu.nju.se.teamnamecannotbeempty.backend.service.visualization.EntityService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
        assertNull(entityController.getAcademicEntity(0,1));
    }

    @Test
    public void getGraphTest1() throws Exception{
        assertNull(entityController.getGraph(0,1));
    }

    @Test
    public void getMoreGraphTest1() throws Exception{
        assertNull(entityController.getMoreGraph(0,1));
    }

}
