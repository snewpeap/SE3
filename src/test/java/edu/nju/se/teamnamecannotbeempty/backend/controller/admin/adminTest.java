package edu.nju.se.teamnamecannotbeempty.backend.controller.admin;

import edu.nju.se.teamnamecannotbeempty.backend.service.manage.AdminService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.AliasVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.ResponseVO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class adminTest {
    private MockMvc mockMvc;
    @Mock private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    public void getConfusedAliasTest1() throws Exception{
        List<AliasVO> aliasVOS1 = new ArrayList<>();

        Mockito.when(
                adminService.getDataOperated()
        ).thenReturn(aliasVOS1);
        List<AliasVO> aliasVOS = adminController.getConfusedAlias();
        assertEquals(aliasVOS,aliasVOS1);
    }

    @Test
    public void modifyAliasTest1() throws Exception{
        ResponseVO responseVO = new ResponseVO();
        Mockito.when(adminService.operateDataAlias(0,true,1)).thenReturn(responseVO);
        assertEquals(adminController.modifyAlias(0,true,1),responseVO);
    }

    @Test
    public void getEffectiveAliasTest1() throws Exception{
        List<AliasVO> aliasVOS1 = new ArrayList<>();
        Mockito.when(adminService.getDataError()).thenReturn(aliasVOS1);
        assertEquals(adminController.getEffectiveAlias(),aliasVOS1);
    }

    @Test
    public void cancelAliasTest1() throws Exception{
        ResponseVO responseVO = new ResponseVO();
        Mockito.when(adminService.undoOperate(0,1)).thenReturn(responseVO);
        assertEquals(adminController.cancelAlias(0,1),responseVO);
    }
}
