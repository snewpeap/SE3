package edu.nju.se.teamnamecannotbeempty.backend.vo;

public class Author_SimpleAffiliationVO {
    private String author;
    private String affiliation;

    public Author_SimpleAffiliationVO(String author, String affiliation) {
        this.author = author;
        this.affiliation = affiliation;
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

    public boolean equals(Object o) {
        if (!(o instanceof Author_SimpleAffiliationVO)) return false;
        return author.equals(((Author_SimpleAffiliationVO) o).author) && affiliation.equals(((Author_SimpleAffiliationVO) o).affiliation);
    }
}
