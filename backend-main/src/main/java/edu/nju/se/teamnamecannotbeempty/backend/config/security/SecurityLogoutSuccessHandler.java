package edu.nju.se.teamnamecannotbeempty.backend.config.security;

import com.alibaba.fastjson.JSON;
import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.SecurityMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户登出成功时
 */
@Component
public class SecurityLogoutSuccessHandler implements LogoutSuccessHandler {

    private final SecurityMsg securityMsg;

    @Autowired
    public SecurityLogoutSuccessHandler(SecurityMsg securityMsg) {
        this.securityMsg = securityMsg;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        SecurityResponseBody securityResponseBody = new SecurityResponseBody();
        securityResponseBody.setStatus("100");
        securityResponseBody.setMsg(securityMsg.getLogoutSuccess());

        httpServletResponse.getWriter().write(JSON.toJSONString(securityResponseBody));
    }
}
