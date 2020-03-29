package edu.nju.se.teamnamecannotbeempty.backend.config.security;

import com.alibaba.fastjson.JSON;
import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.SecurityMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 没有登录时返回给前端的数据
 */
@Component
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final SecurityMsg securityMsg;

    @Autowired
    public SecurityAuthenticationEntryPoint(SecurityMsg securityMsg) {
        this.securityMsg = securityMsg;
    }

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        SecurityResponseBody securityResponseBody = new SecurityResponseBody();
        securityResponseBody.setStatus("000");
        securityResponseBody.setMsg(securityMsg.getNeedAuthorities());

        httpServletResponse.getWriter().write(JSON.toJSONString(securityResponseBody));
    }
}
