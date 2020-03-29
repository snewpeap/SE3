package edu.nju.se.teamnamecannotbeempty.backend.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component("rbacauthorityservice")
public class RbacAuthorityService {
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object userInfo = authentication.getPrincipal();
        boolean isPermitted = false;
        if (userInfo instanceof UserDetails) {
            String username = ((UserDetails) userInfo).getUsername();
            isPermitted = true;
        }
        return isPermitted;
    }
}
