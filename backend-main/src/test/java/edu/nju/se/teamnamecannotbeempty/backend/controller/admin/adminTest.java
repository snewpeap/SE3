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
    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }


    /**
     * 获得别名的测试
     * controller返回的结果和预设相同
     *
     * @throws Exception
     */
    @Test
    public void getConfusedAliasTest1() throws Exception {
        List<AliasVO> aliasVOS1 = new ArrayList<>();

        Mockito.when(
                adminService.getDataOperated(1, 1)
        ).thenReturn(aliasVOS1);
        List<AliasVO> aliasVOS = adminController.getConfusedAlias(1,1);
        assertEquals(aliasVOS, aliasVOS1);
    }

    /**
     * 修改别名的测试
     * controller返回的结果和预设相同
     *
     * @throws Exception
     */
    @Test
    public void modifyAliasTest1() throws Exception {
        ResponseVO responseVO = new ResponseVO();
        Mockito.when(adminService.operateDataAlias(0, 1, 1)).thenReturn(responseVO);
        assertEquals(adminController.modifyAlias(0, 1, 1), responseVO);
    }

    /**
     * 获得有效别名的测试
     * controller返回的结果和预设相同
     *
     * @throws Exception
     */
    @Test
    public void getEffectiveAliasTest1() throws Exception {
        List<AliasVO> aliasVOS1 = new ArrayList<>();
        Mockito.when(adminService.getDataError(1, 1)).thenReturn(aliasVOS1);
        assertEquals(adminController.getEffectiveAlias(1, 1), aliasVOS1);
    }

    /**
     * 取消别名的测试
     * controller返回的结果和预设相同
     *
     * @throws Exception
     */
    @Test
    public void cancelAliasTest1() throws Exception {
        ResponseVO responseVO = new ResponseVO();
        Mockito.when(adminService.undoOperate(0, 1)).thenReturn(responseVO);
        assertEquals(adminController.cancelAlias(0, 1), responseVO);
    }
}
