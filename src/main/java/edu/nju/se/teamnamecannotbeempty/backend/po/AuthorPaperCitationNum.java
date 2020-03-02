package edu.nju.se.teamnamecannotbeempty.backend.po;

public class AuthorPaperCitationNum {

    private String author;
    private int paperCitation;

    public AuthorPaperCitationNum(String author, int paperCitation) {
        this.author = author;
        this.paperCitation = paperCitation;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPaperCitation() {
        return paperCitation;
    }

    public void setPaperCitation(int paperCitation) {
        this.paperCitation = paperCitation;
    }
}
