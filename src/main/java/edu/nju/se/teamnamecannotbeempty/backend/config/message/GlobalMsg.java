package edu.nju.se.teamnamecannotbeempty.backend.config.message;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "global-msg")
public class GlobalMsg {
    private String internalError;

    public String getInternalError() {
        return internalError;
    }

    public void setInternalError(String internalError) {
        this.internalError = internalError;
    }
}
