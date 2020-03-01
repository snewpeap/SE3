package edu.nju.se.teamnamecannotbeempty.backend.controller.paper;

import edu.nju.se.teamnamecannotbeempty.backend.controller.rank.RankController;
import edu.nju.se.teamnamecannotbeempty.backend.service.paper.PaperService;
import edu.nju.se.teamnamecannotbeempty.backend.service.rank.RankService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.PaperVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.SimplePaperVO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

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
                .thenReturn("hello?hello?hello?");
    }

    @Test
    public void search() throws Exception{
        List<SimplePaperVO> simplePaperVOList = new ArrayList<>();
        simplePaperVOList.add(mock(SimplePaperVO.class));
        Mockito.when(paperService.search("","All",0,"descend",20))
                .thenReturn(Collections.singletonList(simplePaperVOList));
    }
}
