package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.Date;
import java.util.List;

public class PaperVO {
    private long id;

    private String title;

    private List<String> author;

    private List<String> affiliation; // 机构

    private String publicationTitle; // 会议

    private int publicationYear;

    private int startPage;

    private int endPage;

    private String summary; //摘要，abstract重名了

    private String DOI;

    private String PDFLink;

    private List<String> authorKeywords;

    private List<String> IEEETerms;

    private List<String> controlledTerms;

    private List<String> nonControlledTerms;

    private int citationCount;

    private int referenceCount;

    private String publisher;

    private String identifier;

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

    public List<String> getAuthor() {
        return author;
    }

    public void setAuthor(List<String> author) {
        this.author = author;
    }

    public List<String> getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(List<String> affiliation) {
        this.affiliation = affiliation;
    }

    public String getPublicationTitle() {
        return publicationTitle;
    }

    public void setPublicationTitle(String publicationTitle) {
        this.publicationTitle = publicationTitle;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDOI() {
        return DOI;
    }

    public void setDOI(String DOI) {
        this.DOI = DOI;
    }

    public String getPDFLink() {
        return PDFLink;
    }

    public void setPDFLink(String PDFLink) {
        this.PDFLink = PDFLink;
    }

    public List<String> getAuthorKeywords() {
        return authorKeywords;
    }

    public void setAuthorKeywords(List<String> authorKeywords) {
        this.authorKeywords = authorKeywords;
    }

    public List<String> getIEEETerms() {
        return IEEETerms;
    }

    public void setIEEETerms(List<String> IEEETerms) {
        this.IEEETerms = IEEETerms;
    }

    public List<String> getControlledTerms() {
        return controlledTerms;
    }

    public void setControlledTerms(List<String> controlledTerms) {
        this.controlledTerms = controlledTerms;
    }

    public List<String> getNonControlledTerms() {
        return nonControlledTerms;
    }

    public void setNonControlledTerms(List<String> nonControlledTerms) {
        this.nonControlledTerms = nonControlledTerms;
    }

    public int getCitationCount() {
        return citationCount;
    }

    public void setCitationCount(int citationCount) {
        this.citationCount = citationCount;
    }

    public int getReferenceCount() {
        return referenceCount;
    }

    public void setReferenceCount(int referenceCount) {
        this.referenceCount = referenceCount;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
