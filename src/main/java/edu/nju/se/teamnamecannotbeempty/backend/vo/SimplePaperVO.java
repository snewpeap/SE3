package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.List;
import java.util.Objects;

public class SimplePaperVO {

    public SimplePaperVO(long id, String title, List<Author_SimpleAffiliationVO> author_simpleAffiliationVOS, String publicationTitle, String publicationYear, List<String> keywords) {
        this.id = id;
        this.title = title;
        this.author_simpleAffiliationVOS = author_simpleAffiliationVOS;
        this.publicationTitle = publicationTitle;
        this.publicationYear = publicationYear;
        this.keywords = keywords;
    }

    private long id;

    private String title;

    private List<Author_SimpleAffiliationVO> author_simpleAffiliationVOS;


    private String publicationTitle;

    private String publicationYear;

    private List<String> keywords;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
    }

    public List<Author_SimpleAffiliationVO> getAuthor_simpleAffiliationVOS() {
        return author_simpleAffiliationVOS;
    }

    public void setAuthor_simpleAffiliationVOS(List<Author_SimpleAffiliationVO> author_simpleAffiliationVOS) {
        this.author_simpleAffiliationVOS = author_simpleAffiliationVOS;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimplePaperVO that = (SimplePaperVO) o;
        return id == that.id &&
                Objects.equals(title, that.title) &&
                Objects.equals(author_simpleAffiliationVOS, that.author_simpleAffiliationVOS) &&
                Objects.equals(publicationTitle, that.publicationTitle) &&
                Objects.equals(publicationYear, that.publicationYear) &&
                Objects.equals(keywords, that.keywords);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, title, author_simpleAffiliationVOS, publicationTitle, publicationYear, keywords);
    }
}
