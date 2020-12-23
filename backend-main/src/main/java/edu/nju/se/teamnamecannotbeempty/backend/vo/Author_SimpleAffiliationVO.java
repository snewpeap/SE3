package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.Objects;

public class Author_SimpleAffiliationVO{
    private String author;
    private Long authorId;
    private String affiliation;
    private Long affiliationId;

    public Author_SimpleAffiliationVO(String author, Long authorId, String affiliation, Long affiliationId) {
        this.author = author;
        this.authorId = authorId;
        this.affiliation = affiliation;
        this.affiliationId = affiliationId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public Long getAffiliationId() {
        return affiliationId;
    }

    public void setAffiliationId(Long affiliationId) {
        this.affiliationId = affiliationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author_SimpleAffiliationVO that = (Author_SimpleAffiliationVO) o;
        return authorId.equals(that.authorId) &&
                Objects.equals(affiliationId, that.affiliationId) &&
                Objects.equals(author, that.author) &&
                Objects.equals(affiliation, that.affiliation);
    }

    @Override
    public int hashCode() {

        return Objects.hash(author, authorId, affiliation, affiliationId);
    }
}
