package edu.nju.se.teamnamecannotbeempty.backend.config.parameter;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security-msg")
public class SecurityMsg extends GlobalMsg {
    private String needAuthorities;
    private String loginFailure;
    private String loginSuccess;
    private String logoutSuccess;
    private String noSuchUser;

    public String getNeedAuthorities() {
        return needAuthorities;
    }

    public void setNeedAuthorities(String needAuthorities) {
        this.needAuthorities = needAuthorities;
    }

    public String getLoginFailure() {
        return loginFailure;
    }

    public void setLoginFailure(String loginFailure) {
        this.loginFailure = loginFailure;
    }

    public String getLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(String loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    public String getLogoutSuccess() {
        return logoutSuccess;
    }

    public void setLogoutSuccess(String logoutSuccess) {
        this.logoutSuccess = logoutSuccess;
    }

    public String getNoSuchUser() {
        return noSuchUser;
    }

    public void setNoSuchUser(String noSuchUser) {
        this.noSuchUser = noSuchUser;
    }
}
