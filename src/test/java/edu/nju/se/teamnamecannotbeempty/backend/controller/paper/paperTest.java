package edu.nju.se.teamnamecannotbeempty.backend.controller.paper;

import edu.nju.se.teamnamecannotbeempty.backend.controller.rank.RankController;
import edu.nju.se.teamnamecannotbeempty.backend.service.paper.PaperService;
import edu.nju.se.teamnamecannotbeempty.backend.service.rank.RankService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.PaperVO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class paperTest {
    private MockMvc mvc;

    @Mock
    private PaperService paperService;

    @InjectMocks
    private PaperController controller;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getPaper() throws Exception{
        Mockito.when(paperService.getPaper(1))
                .thenReturn(new PaperVO());
    }
}
