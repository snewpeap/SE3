package edu.nju.se.teamnamecannotbeempty.backend.config.parameter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rank-msg")
public class RankMsg extends GlobalMsg {

    private int eachNum;
    private String mismatchPageNumber;

    public int getEachNum() {
        return eachNum;
    }

    public void setEachNum(int eachNum) {
        this.eachNum = eachNum;
    }

    public String getMismatchPageNumber() {
        return mismatchPageNumber;
    }

    public void setMismatchPageNumber(String mismatchPageNumber) {
        this.mismatchPageNumber = mismatchPageNumber;
    }
}
