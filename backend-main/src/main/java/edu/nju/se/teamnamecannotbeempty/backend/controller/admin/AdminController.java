package edu.nju.se.teamnamecannotbeempty.backend.controller.admin;

import edu.nju.se.teamnamecannotbeempty.backend.service.manage.AdminService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.AliasVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 管理员的controller层
 */
@RestController()
@Service
public class AdminController {
    @Autowired(required = false)
    AdminService adminService;

    @GetMapping(value = "/admin/getConfusedAlias")
    public List<AliasVO> getConfusedAlias() {
        return adminService.getDataOperated();
    }

    @PostMapping(value = "/admin/modifyAlias")
    public ResponseVO modifyAlias(@RequestParam long recordId, @RequestParam boolean isAlias, @RequestParam int type) {
        return adminService.operateDataAlias(recordId, isAlias, type);
    }

    @GetMapping(value = "/admin/getEffectiveAlias")
    public List<AliasVO> getEffectiveAlias() {
        return adminService.getDataError();
    }

    @PostMapping("/admin/cancelAlias")
    public ResponseVO cancelAlias(@RequestParam long recordId, @RequestParam int type) {
        return adminService.undoOperate(recordId, type);
    }
}
