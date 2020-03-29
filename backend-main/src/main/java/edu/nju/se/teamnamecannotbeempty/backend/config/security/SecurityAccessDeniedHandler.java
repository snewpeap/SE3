package edu.nju.se.teamnamecannotbeempty.backend.config.security;

import com.alibaba.fastjson.JSON;
import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.SecurityMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户没有权限访问时
 */
@Component
public class SecurityAccessDeniedHandler implements AccessDeniedHandler {

    private final SecurityMsg securityMsg;

    @Autowired
    public SecurityAccessDeniedHandler(SecurityMsg securityMsg) {
        this.securityMsg = securityMsg;
    }

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        SecurityResponseBody securityResponseBody = new SecurityResponseBody();
        securityResponseBody.setStatus("300");
        securityResponseBody.setMsg(securityMsg.getNeedAuthorities());

        httpServletResponse.getWriter().write(JSON.toJSONString(securityResponseBody));
    }
}
