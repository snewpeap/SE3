package edu.nju.se.teamnamecannotbeempty.backend.controller.rank;

import edu.nju.se.teamnamecannotbeempty.backend.service.rank.RankService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.PaperVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.RankVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.ResponseVO;
import org.junit.Test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class rankTest {
    private MockMvc mvc;

    @Mock
    private RankService rankService;

    @InjectMocks
    private RankController rankController;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(rankController).build();
    }

    @Test
    public void getRank() throws Exception{

        ResponseVO responseVO = new ResponseVO();
        Mockito.when(rankService.getRank("All",0,true,2010,2010))
                .thenReturn(responseVO);
        rankController = new RankController();
        rankController.setRankService(rankService);
        assertEquals(rankController.getRank("All",0,true,2010,2010),responseVO);
    }
}
