package edu.nju.se.teamnamecannotbeempty.backend.config.parameter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "paper-msg")
public class PaperMsg extends GlobalMsg {
    private String mismatchId;

    public String getMismatchId() {
        return mismatchId;
    }

    public void setMismatchId(String mismatchId) {
        this.mismatchId = mismatchId;
    }
}
