package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.List;

public class SimplePaperVO {
    private String title;

    private String author;

    private String affiliation;

    private String publicationTitle;

    private List<String> keywords;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getIssue() {
        return publicationTitle;
    }

    public void setIssue(String issue) {
        this.publicationTitle = issue;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
}
