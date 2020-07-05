package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.List;
import java.util.Objects;

public class AcademicEntityVO {
    private int type;
    private long id;
    private String name;
    private int refSum;
    private List<AcademicEntityItem> authors;
    private List<AcademicEntityItem> affiliations;
    private List<AcademicEntityItem> conferences;
    private List<TermItem> terms;
    private List<YearlyTerm> yearlyTerms;
    private List<SimplePaperVO> significantPapers;


    public AcademicEntityVO(int type, long id, String name, int refSum,
                            List<AcademicEntityItem> authors, List<AcademicEntityItem> affiliations,
                            List<AcademicEntityItem> conferences, List<TermItem> terms,
                            List<SimplePaperVO> significantPapers, List<YearlyTerm> yearlyTerms) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.refSum = refSum;
        this.authors = authors;
        this.affiliations = affiliations;
        this.conferences = conferences;
        this.terms = terms;
        this.significantPapers = significantPapers;
        this.yearlyTerms = yearlyTerms;
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

    public int getRefSum() {
        return refSum;
    }

    public void setRefSum(int refSum) {
        this.refSum = refSum;
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

    public List<YearlyTerm> getYearlyTerms() {
        return yearlyTerms;
    }

    public void setYearlyTerms(List<YearlyTerm> yearlyTerms) {
        this.yearlyTerms = yearlyTerms;
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
                refSum == that.refSum &&
                Objects.equals(name, that.name) &&
                Objects.equals(authors, that.authors) &&
                Objects.equals(affiliations, that.affiliations) &&
                Objects.equals(conferences, that.conferences) &&
                Objects.equals(terms, that.terms) &&
                Objects.equals(yearlyTerms, that.yearlyTerms) &&
                Objects.equals(significantPapers, that.significantPapers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id, name, refSum, authors, affiliations,
                conferences, terms, yearlyTerms, significantPapers);
    }
}
