package edu.nju.se.teamnamecannotbeempty.backend.config.security;

import com.alibaba.fastjson.JSON;
import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.SecurityMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户登录成功时
 */
@Component
public class SecurityAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final SecurityMsg securityMsg;

    @Autowired
    public SecurityAuthenticationSuccessHandler(SecurityMsg securityMsg) {
        this.securityMsg = securityMsg;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        SecurityResponseBody securityResponseBody = new SecurityResponseBody();
        securityResponseBody.setStatus("200");
        securityResponseBody.setMsg(securityMsg.getLoginSuccess());

        SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
        String jwtToken = JwtTokenUtil.generateToken(userDetails.getUsername(), 600, "_secret");
        securityResponseBody.setJwtToken(jwtToken);

        httpServletResponse.getWriter().write(JSON.toJSONString(securityResponseBody));
    }
}
