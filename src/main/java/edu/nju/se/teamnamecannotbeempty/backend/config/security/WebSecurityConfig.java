package edu.nju.se.teamnamecannotbeempty.backend.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    private final SecurityAuthenticationEntryPoint authenticationEntryPoint;
    private final SecurityAuthenticationSuccessHandler authenticationSuccessHandler;
    private final SecurityAuthenticationFailureHandler authenticationFailureHandler;
    private final SecurityLogoutSuccessHandler logoutSuccessHandler;
    private final SecurityAccessDeniedHandler accessDeniedHandler;
    private final SecurityUserDetailService userDetailService;
    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;


    @Autowired
    public WebSecurityConfig(SecurityAuthenticationEntryPoint authenticationEntryPoint, SecurityAuthenticationSuccessHandler authenticationSuccessHandler, SecurityAuthenticationFailureHandler authenticationFailureHandler, SecurityLogoutSuccessHandler logoutSuccessHandler, SecurityAccessDeniedHandler accessDeniedHandler, SecurityUserDetailService userDetailService, JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.accessDeniedHandler = accessDeniedHandler;
        this.userDetailService = userDetailService;
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/admin/**")
                .access("@rbacauthorityservice.hasPermission(request,authentication)")
                .and()
                .formLogin()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .permitAll()
                .and()
                .logout()
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll();
        http.rememberMe().rememberMeParameter("remember-me")
                .userDetailsService(userDetailService).tokenValiditySeconds(300);
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
        http.addFilterBefore(jwtAuthenticationTokenFilter,UsernamePasswordAuthenticationFilter.class);
    }
}
