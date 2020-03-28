package edu.nju.se.teamnamecannotbeempty.backend.config.security;

import com.alibaba.fastjson.JSON;
import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.SecurityMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户登录失败时
 */
@Component
public class SecurityAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final SecurityMsg securityMsg;

    @Autowired
    public SecurityAuthenticationFailureHandler(SecurityMsg securityMsg) {
        this.securityMsg = securityMsg;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        SecurityResponseBody securityResponseBody = new SecurityResponseBody();
        securityResponseBody.setStatus("400");
        securityResponseBody.setMsg(securityMsg.getLoginFailure());

        httpServletResponse.getWriter().write(JSON.toJSONString(securityResponseBody));
    }
}
