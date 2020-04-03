package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.Objects;

public class Author_AffiliationVO {
    private String author;
    private long authorId;
    private AffiliationVO affiliation;

    public Author_AffiliationVO(String author, long authorId, AffiliationVO affiliation) {
        this.author = author;
        this.authorId = authorId;
        this.affiliation = affiliation;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public AffiliationVO getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(AffiliationVO affiliation) {
        this.affiliation = affiliation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author_AffiliationVO that = (Author_AffiliationVO) o;
        return authorId == that.authorId &&
                Objects.equals(author, that.author) &&
                Objects.equals(affiliation, that.affiliation);
    }

    @Override
    public int hashCode() {

        return Objects.hash(author, authorId, affiliation);
    }
}
