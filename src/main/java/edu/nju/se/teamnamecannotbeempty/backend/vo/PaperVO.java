package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.List;

public class PaperVO {
    private long id;

    private String title;

    private List<Author_AffiliationVO> author_affiliationVOS;


    private String publicationTitle; // 会议

    private String publicationYear;

    private String ordno;

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

    public PaperVO(long id, String title, List<Author_AffiliationVO> author_affiliationVOS, String publicationTitle, String publicationYear, String ordno, int startPage, int endPage, String summary, String DOI, String PDFLink, List<String> authorKeywords, List<String> IEEETerms, List<String> controlledTerms, List<String> nonControlledTerms, int citationCount, int referenceCount, String publisher, String identifier) {
        this.id = id;
        this.title = title;
        this.author_affiliationVOS = author_affiliationVOS;
        this.publicationTitle = publicationTitle;
        this.publicationYear = publicationYear;
        this.ordno = ordno;
        this.startPage = startPage;
        this.endPage = endPage;
        this.summary = summary;
        this.DOI = DOI;
        this.PDFLink = PDFLink;
        this.authorKeywords = authorKeywords;
        this.IEEETerms = IEEETerms;
        this.controlledTerms = controlledTerms;
        this.nonControlledTerms = nonControlledTerms;
        this.citationCount = citationCount;
        this.referenceCount = referenceCount;
        this.publisher = publisher;
        this.identifier = identifier;
    }

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

    public List<Author_AffiliationVO> getAuthor_affiliationVOS() {
        return author_affiliationVOS;
    }

    public void setAuthor_affiliationVOS(List<Author_AffiliationVO> author_affiliationVOS) {
        this.author_affiliationVOS = author_affiliationVOS;
    }

    public String getPublicationTitle() {
        return publicationTitle;
    }

    public void setPublicationTitle(String publicationTitle) {
        this.publicationTitle = publicationTitle;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(String publicationYear) {
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


    public String getOrdno() {
        return ordno;
    }

    public void setOrdno(String ordno) {
        this.ordno = ordno;
    }

    public boolean equals(Object o) {
        if (!(o instanceof PaperVO)) return false;
        return id == ((PaperVO) o).getId() && title.equals(((PaperVO) o).getTitle()) && author_affiliationVOS.equals(((PaperVO) o).getAuthor_affiliationVOS())
                && publicationTitle.equals(((PaperVO) o).getPublicationTitle()) && publicationYear.equals(((PaperVO) o).getPublicationYear())
                && startPage == ((PaperVO) o).getStartPage() && endPage == ((PaperVO) o).endPage && summary.equals(((PaperVO) o).getSummary())
                && DOI.equals(((PaperVO) o).getDOI()) && ((PDFLink == null && ((PaperVO) o).getPDFLink() == null) || PDFLink.equals(((PaperVO) o).getPDFLink())) && authorKeywords.equals(((PaperVO) o).getAuthorKeywords())
                && IEEETerms.equals(((PaperVO) o).getIEEETerms()) && controlledTerms.equals(((PaperVO) o).getControlledTerms())
                && nonControlledTerms.equals(((PaperVO) o).getNonControlledTerms()) && citationCount == ((PaperVO) o).getCitationCount()
                && referenceCount == ((PaperVO) o).getReferenceCount() && ((publisher == null && ((PaperVO) o).getPublisher() == null) || publisher.equals(((PaperVO) o).getPublisher()))
                && ((identifier == null && ((PaperVO) o).getIdentifier() == null) || identifier.equals(((PaperVO) o).getIdentifier())) && ((ordno == null && ((PaperVO) o).getOrdno() == null) || ordno.equals(((PaperVO) o).getOrdno()));

    }
}
