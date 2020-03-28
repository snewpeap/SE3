package edu.nju.se.teamnamecannotbeempty.backend.config.security;

import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.SecurityMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SecurityUserDetailService implements UserDetailsService {

    private final SecurityMsg securityMsg;

    @Autowired
    public SecurityUserDetailService(SecurityMsg securityMsg) {
        this.securityMsg = securityMsg;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if(!s.equals("admin")) throw new UsernameNotFoundException(securityMsg.getNoSuchUser());
        SecurityUserDetails user = new SecurityUserDetails();
        user.setUsername(s);
        user.setPassword(new BCryptPasswordEncoder().encode("admin"));

        Set<GrantedAuthority> authoritiesSet = new HashSet<>(1);
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        authoritiesSet.add(authority);
        user.setAuthorities(authoritiesSet);
        return user;
    }
}
