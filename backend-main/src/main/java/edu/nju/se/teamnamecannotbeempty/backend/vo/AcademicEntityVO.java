package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.List;
import java.util.Objects;

public class AcademicEntityVO {
    private int type;
    private long id;


    private String name;
    private List<AcademicEntityItem> authors;
    private List<AcademicEntityItem> affiliations;
    private List<AcademicEntityItem> conferences;
    private List<TermItem> terms;
    private List<SimplePaperVO> significantPapers;

    public AcademicEntityVO(int type, long id, String name,List<AcademicEntityItem> authors, List<AcademicEntityItem> affiliations, List<AcademicEntityItem> conferences, List<TermItem> terms, List<SimplePaperVO> significantPapers) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.authors = authors;
        this.affiliations = affiliations;
        this.conferences = conferences;
        this.terms = terms;
        this.significantPapers = significantPapers;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AcademicEntityItem> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AcademicEntityItem> authors) {
        this.authors = authors;
    }

    public List<AcademicEntityItem> getAffiliations() {
        return affiliations;
    }

    public void setAffiliations(List<AcademicEntityItem> affiliations) {
        this.affiliations = affiliations;
    }

    public List<AcademicEntityItem> getConferences() {
        return conferences;
    }

    public void setConferences(List<AcademicEntityItem> conferences) {
        this.conferences = conferences;
    }

    public List<TermItem> getTerms() {
        return terms;
    }

    public void setTerms(List<TermItem> terms) {
        this.terms = terms;
    }

    public List<SimplePaperVO> getSignificantPapers() {
        return significantPapers;
    }

    public void setSignificantPapers(List<SimplePaperVO> significantPapers) {
        this.significantPapers = significantPapers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AcademicEntityVO that = (AcademicEntityVO) o;
        return type == that.type &&
                id == that.id &&
                Objects.equals(authors, that.authors) &&
                Objects.equals(affiliations, that.affiliations) &&
                Objects.equals(conferences, that.conferences) &&
                Objects.equals(terms, that.terms) &&
                Objects.equals(significantPapers, that.significantPapers);
    }

    @Override
    public int hashCode() {

        return Objects.hash(type, id, authors, affiliations, conferences, terms, significantPapers);
    }
}
