package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.Objects;

public class Author_SimpleAffiliationVO {
    private String author;
    private long authorId;
    private String affiliation;
    private long affiliationId;

    public Author_SimpleAffiliationVO(String author, long authorId, String affiliation, long affiliationId) {
        this.author = author;
        this.authorId = authorId;
        this.affiliation = affiliation;
        this.affiliationId = affiliationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author_SimpleAffiliationVO that = (Author_SimpleAffiliationVO) o;
        return authorId == that.authorId &&
                affiliationId == that.affiliationId &&
                Objects.equals(author, that.author) &&
                Objects.equals(affiliation, that.affiliation);
    }

    @Override
    public int hashCode() {

        return Objects.hash(author, authorId, affiliation, affiliationId);
    }
}
