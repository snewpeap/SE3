package edu.nju.se.teamnamecannotbeempty.backend.controller.paper;

import edu.nju.se.teamnamecannotbeempty.backend.controller.rank.RankController;
import edu.nju.se.teamnamecannotbeempty.backend.po.Paper;
import edu.nju.se.teamnamecannotbeempty.backend.service.paper.PaperService;
import edu.nju.se.teamnamecannotbeempty.backend.service.rank.RankService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.PaperVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.ResponseVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.SimplePaperVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration()
public class paperTest {
    private MockMvc mvc;

    @Mock
    private PaperService paperService;

    @InjectMocks
    private PaperController controller;
    //private MockMvc mockMvc;
    private String url;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetPaper1() throws Exception{
        ResponseVO responseVO = new ResponseVO();
        Mockito.when(paperService.getPaper(1))
                .thenReturn(responseVO);
        PaperController paperController = new PaperController();
        paperController.setPaperService(paperService);
        assertEquals(paperController.getPaper(1),responseVO);
    }

    @Test
    public void tsetSearch1() throws Exception{
        List<SimplePaperVO> simplePaperVOList = new ArrayList<>();
        //simplePaperVOList.add(mock(SimplePaperVO.class));
        Mockito.when(paperService.search("","All",0,"descend",20))
                .thenReturn(simplePaperVOList);
        PaperController paperController = new PaperController();
        paperController.setPaperService(paperService);
        assertEquals(paperController.search("","All",0,"descend",20),simplePaperVOList);
    }

//    @Autowired
//    protected WebApplicationContext wac;
//
//    @Mock
//    private PaperService paperService;
//

//
//    @Before
//    public void setUp() throws Exception{
//        MockitoAnnotations.initMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(wac).build();
//    }
//
    @Test
    public void testGetPaper2() throws Exception{
        url = "/paperDetail/1";
        Paper paper = new Paper();
        ResponseVO responseVO = new ResponseVO();
        responseVO.setSuccess(true);
        Mockito.when(paperService.getPaper(1)).thenReturn(responseVO);
        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testSearch2() throws Exception{
        url = "/search/text/All";
        List<SimplePaperVO> simplePaperVOList = new ArrayList<>();
        Mockito.when(paperService.search("text","All",0,"descend",20))
                .thenReturn(simplePaperVOList);
        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.get(url)
                .accept(MediaType.APPLICATION_JSON)
                .param("pageNumber","0")
                .param("sortMode","descend")
                .param("perPage","20"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

}
