package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.List;

public class SimplePaperVO {
    private String title;

    private List<String> author;

    private List<String> affiliation;

    private String publicationTitle;

    private List<String> keywords;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthor() {
        return author;
    }

    public void setAuthor(List<String> author) {
        this.author = author;
    }

    public List<String> getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(List<String> affiliation) {
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
