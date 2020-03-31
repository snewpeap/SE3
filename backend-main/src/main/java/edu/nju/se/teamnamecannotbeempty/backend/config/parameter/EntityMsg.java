package edu.nju.se.teamnamecannotbeempty.backend.config.parameter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "entity-msg")
public class EntityMsg extends GlobalMsg{
    private int authorType;
    private int affiliationType;
    private int conferenceType;
    private int termType;
    private int paperType;

    public int getAuthorType() {
        return authorType;
    }

    public void setAuthorType(int authorType) {
        this.authorType = authorType;
    }

    public int getAffiliationType() {
        return affiliationType;
    }

    public void setAffiliationType(int affiliationType) {
        this.affiliationType = affiliationType;
    }

    public int getConferenceType() {
        return conferenceType;
    }

    public void setConferenceType(int conferenceType) {
        this.conferenceType = conferenceType;
    }

    public int getTermType() {
        return termType;
    }

    public void setTermType(int termType) {
        this.termType = termType;
    }

    public int getPaperType() {
        return paperType;
    }

    public void setPaperType(int paperType) {
        this.paperType = paperType;
    }
}
