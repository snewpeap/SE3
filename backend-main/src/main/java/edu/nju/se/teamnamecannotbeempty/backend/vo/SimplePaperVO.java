package edu.nju.se.teamnamecannotbeempty.backend.vo;

import edu.nju.se.teamnamecannotbeempty.data.domain.Author_Affiliation;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.domain.Term;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimplePaperVO {

    private long id;

    private String title;

    private List<Author_SimpleAffiliationVO> author_simpleAffiliationVOS;

    private String publicationTitle;

    private long conferenceId;

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

    public String getPublicationTitle() {
        return publicationTitle;
    }

    public void setPublicationTitle(String publicationTitle) {
        this.publicationTitle = publicationTitle;
    }

    public long getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(long conferenceId) {
        this.conferenceId = conferenceId;
    }

    public void setAuthor_simpleAffiliationVOS(List<Author_SimpleAffiliationVO> author_simpleAffiliationVOS) {
        this.author_simpleAffiliationVOS = author_simpleAffiliationVOS;
    }

    public SimplePaperVO(long id, String title, List<Author_SimpleAffiliationVO> author_simpleAffiliationVOS, String publicationTitle,long conferenceId, String publicationYear, List<String> keywords) {
        this.id = id;
        this.title = title;
        this.author_simpleAffiliationVOS = author_simpleAffiliationVOS;
        this.publicationTitle = publicationTitle;
        this.conferenceId = conferenceId;
        this.publicationYear = publicationYear;
        this.keywords = keywords;
    }

    public SimplePaperVO(Paper paper){
        List<Author_SimpleAffiliationVO> author_simpleAffiliationVOS = new ArrayList<>();
        List<Author_Affiliation> author_affiliations = paper.getAa();
        for (Author_Affiliation author_affiliation : author_affiliations) {
            author_simpleAffiliationVOS.add(new Author_SimpleAffiliationVO(author_affiliation.getAuthor().getName(),
                    author_affiliation.getAuthor().getActual().getId(),author_affiliation.getAffiliation().getName(),
                    author_affiliation.getAffiliation().getActual().getId()));
        }
        List<String> keywords = new ArrayList<>();
        List<Term> termList = paper.getAuthor_keywords();
        for (Term term : termList) {
            keywords.add(term.getContent());
        }
        this.id = paper.getId();
        this.title = paper.getTitle();
        this.author_simpleAffiliationVOS = author_simpleAffiliationVOS;
        this.publicationTitle = paper.getConference().getName();
        this.conferenceId = paper.getConference().getId();
        this.publicationYear = paper.getConference().getYear_highlight();
        this.keywords = keywords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimplePaperVO that = (SimplePaperVO) o;
        return id == that.id &&
                conferenceId == that.conferenceId &&
                Objects.equals(title, that.title) &&
                Objects.equals(author_simpleAffiliationVOS, that.author_simpleAffiliationVOS) &&
                Objects.equals(publicationTitle, that.publicationTitle) &&
                Objects.equals(publicationYear, that.publicationYear) &&
                Objects.equals(keywords, that.keywords);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, title, author_simpleAffiliationVOS, publicationTitle, conferenceId, publicationYear, keywords);
    }
}
