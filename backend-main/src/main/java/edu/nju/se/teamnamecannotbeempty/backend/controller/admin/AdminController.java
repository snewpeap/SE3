package edu.nju.se.teamnamecannotbeempty.backend.controller.admin;

import edu.nju.se.teamnamecannotbeempty.backend.data.DataRefresh;
import edu.nju.se.teamnamecannotbeempty.backend.service.manage.AdminService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.AliasVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 管理员的controller层
 */
@RestController
public class AdminController {
    @Autowired
    AdminService adminService;
    @Autowired
    DataRefresh dataRefresh;

    @PostMapping("/admin/refresh")
    public ResponseVO refresh() {
        try {
            dataRefresh.refresh();
            return ResponseVO.success();
        } catch (Exception e) {
            return ResponseVO.fail();
        }
    }

    @GetMapping(value = "/admin/getConfusedAlias")
    public List<AliasVO> getConfusedAlias(@RequestParam int page, @RequestParam int type) {
        return adminService.getDataError(page, type);
    }

    @PostMapping(value = "/admin/modifyAlias")
    public ResponseVO modifyAlias(@RequestParam long sonId, @RequestParam long fatherId, @RequestParam int type) {
        return adminService.operateDataAlias(sonId, fatherId, type);
    }

    @GetMapping(value = "/admin/getEffectiveAlias")
    public List<AliasVO> getEffectiveAlias(@RequestParam int page, @RequestParam int type) {
        return adminService.getDataOperated(page, type);
    }

    @PostMapping("/admin/cancelAlias")
    public ResponseVO cancelAlias(@RequestParam long sonId, @RequestParam int type) {
        return adminService.undoOperate(sonId, type);
    }
}
