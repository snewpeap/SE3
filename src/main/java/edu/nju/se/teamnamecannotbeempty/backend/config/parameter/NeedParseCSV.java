package edu.nju.se.teamnamecannotbeempty.backend.config.parameter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "parse-csv")
public class NeedParseCSV {
    public boolean isNeed() {
        return need;
    }

    public void setNeed(boolean need) {
        this.need = need;
    }

    private boolean need;
}
