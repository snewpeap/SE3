package edu.nju.se.teamnamecannotbeempty.backend.vo;

public class Author_AffiliationVO {
    private String author;
    private AffiliationVO affiliation;

    public Author_AffiliationVO(String author, String name, String country) {
        this.author = author;
        this.affiliation = new AffiliationVO(name, country);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public AffiliationVO getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(AffiliationVO affiliation) {
        this.affiliation = affiliation;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Author_AffiliationVO)) return false;
        return author.equals(((Author_AffiliationVO) o).author) && affiliation.equals(((Author_AffiliationVO) o).affiliation);
    }
}
