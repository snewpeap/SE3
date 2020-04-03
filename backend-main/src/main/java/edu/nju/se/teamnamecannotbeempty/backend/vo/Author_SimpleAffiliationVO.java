package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.Objects;

public class Author_SimpleAffiliationVO {
    private String author;
    private long authorType;
    private String affiliation;
    private long affiliationType;

    public Author_SimpleAffiliationVO(String author, long authorType, String affiliation, long affiliationType) {
        this.author = author;
        this.authorType = authorType;
        this.affiliation = affiliation;
        this.affiliationType = affiliationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author_SimpleAffiliationVO that = (Author_SimpleAffiliationVO) o;
        return authorType == that.authorType &&
                affiliationType == that.affiliationType &&
                Objects.equals(author, that.author) &&
                Objects.equals(affiliation, that.affiliation);
    }

    @Override
    public int hashCode() {

        return Objects.hash(author, authorType, affiliation, affiliationType);
    }
}
