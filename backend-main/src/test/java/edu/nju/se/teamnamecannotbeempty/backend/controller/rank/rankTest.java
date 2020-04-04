package edu.nju.se.teamnamecannotbeempty.backend.controller.rank;

import edu.nju.se.teamnamecannotbeempty.backend.service.rank.RankService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.RankItem;
import edu.nju.se.teamnamecannotbeempty.backend.vo.ResponseVO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class rankTest {
    private MockMvc mvc;

    @Mock
    private RankService rankService;

    @InjectMocks
    private RankController rankController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(rankController).build();
    }

    /**
     * 对RankController中 getRank的测试1
     * 通过RankController对象直接调用方法
     *
     * @throws Exception
     */
    @Test
    public void testGetRank1() throws Exception {
        ResponseVO responseVO = new ResponseVO();
        Mockito.when(rankService.getRank("All", 0, true, 2010, 2010))
                .thenReturn(responseVO);
        rankController = new RankController();
        rankController.setRankService(rankService);
        assertEquals(rankController.getRank("All", 0, true, 2010, 2010), responseVO);
    }

    /**
     * 对RankController中 getRank的测试2
     * 通过url测试 一般情况
     *
     * @throws Exception
     */
    @Test
    public void testGetRank2() throws Exception {
        ResponseVO responseVO = new ResponseVO();
        Mockito.when(rankService.getRank("All", 0, true, 2010, 2010))
                .thenReturn(responseVO);
        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.get("/rank/All")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("pageNumber", "0")
                        .param("descend", "true")
                        .param("startYear", "2010")
                        .param("endYear", "2010"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    /**
     * 对RankController中 getRank的测试3
     * 通过url测试 不传可选参数pageNumber时
     *
     * @throws Exception
     */
    @Test
    public void testGetRank3() throws Exception {
        ResponseVO responseVO = new ResponseVO();
        Mockito.when(rankService.getRank("All", 0, true, 2010, 2010))
                .thenReturn(responseVO);
        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.get("/rank/All")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("descend", "true")
                        .param("startYear", "2010")
                        .param("endYear", "2010"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    /**
     * 对RankController中 getPopRank的测试
     * 通过url测试 传参数type时
     * @throws Exception
     */
    @Test
    public void testGetPopRank() throws Exception{
        List<RankItem> rankItemList = new ArrayList<>();
        Mockito.when(rankService.getPopRank(1))
                .thenReturn(rankItemList);
        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.get("/hot")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("type", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
}
