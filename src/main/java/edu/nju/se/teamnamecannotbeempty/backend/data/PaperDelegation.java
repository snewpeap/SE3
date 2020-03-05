package edu.nju.se.teamnamecannotbeempty.backend.data;

import com.opencsv.bean.CsvBindAndSplitByPosition;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import com.opencsv.bean.CsvDate;
import edu.nju.se.teamnamecannotbeempty.backend.data.converters.ToAffiliation;
import edu.nju.se.teamnamecannotbeempty.backend.data.converters.ToAuthor;
import edu.nju.se.teamnamecannotbeempty.backend.data.converters.ToConference;
import edu.nju.se.teamnamecannotbeempty.backend.data.converters.ToTerm;
import edu.nju.se.teamnamecannotbeempty.backend.po.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaperDelegation {
    private static Logger logger = LoggerFactory.getLogger(PaperDelegation.class);

    @CsvBindByPosition(position = 0)
    private String title;

    @CsvBindAndSplitByPosition(
            position = 1,
            elementType = Author.class,
            splitOn = "; ",
            converter = ToAuthor.class,
            collectionType = ArrayList.class
    )
    private List<Author> authors;

    @CsvBindAndSplitByPosition(
            position = 2,
            elementType = Affiliation.class,
            splitOn = "; ",
            converter = ToAffiliation.class,
            collectionType = ArrayList.class
    )
    private List<Affiliation> affiliations;

    @CsvCustomBindByPosition(
            position = 3,
            converter = ToConference.class
    )
    private Conference conference;

    @CsvBindByPosition(position = 4)
    @CsvDate
    private Date date_added_Xplore;
    @CsvBindByPosition(position = 6)
    private Integer volume;
    @CsvBindByPosition(position = 8)
    private String start_page;
    @CsvBindByPosition(position = 9)
    private String end_page;
    @CsvBindByPosition(position = 10)
    private String summary;
    @CsvBindByPosition(position = 11)
    private String issn;
    @CsvBindByPosition(position = 12)
    private String isbn;
    @CsvBindByPosition(position = 13)
    private String doi;
    @CsvBindByPosition(position = 14)
    private String funding_info;
    @CsvBindByPosition(position = 15)
    private String pdf_link;

    @CsvBindAndSplitByPosition(
            position = 16,
            elementType = Term.class,
            splitOn = ";",
            converter = ToTerm.class,
            collectionType = ArrayList.class
    )
    //author keywords
    private List<Term> ak;
    @CsvBindAndSplitByPosition(
            position = 17,
            elementType = Term.class,
            splitOn = ";",
            converter = ToTerm.class,
            collectionType = ArrayList.class
    )
    private List<Term> ie3t;
    @CsvBindAndSplitByPosition(
            position = 18,
            elementType = Term.class,
            splitOn = ";",
            converter = ToTerm.class,
            collectionType = ArrayList.class
    )
    private List<Term> ict;
    @CsvBindAndSplitByPosition(
            position = 19,
            elementType = Term.class,
            splitOn = ";",
            converter = ToTerm.class,
            collectionType = ArrayList.class
    )
    private List<Term> inct;
    @CsvBindAndSplitByPosition(
            position = 20,
            elementType = Term.class,
            splitOn = ";|, ",
            converter = ToTerm.class,
            collectionType = ArrayList.class
    )
    private List<Term> mt;

    @CsvBindByPosition(position = 21)
    private Integer citation;
    @CsvBindByPosition(position = 22)
    private Integer reference;
    @CsvBindByPosition(position = 23)
    private String license;
    @CsvBindByPosition(position = 24)
    @CsvDate
    private Date online_date;
    @CsvBindByPosition(position = 25)
    @CsvDate
    private Date issue_date;
    @CsvBindByPosition(position = 26)
    @CsvDate
    private Date meeting_date;
    @CsvBindByPosition(position = 27)
    private String publisher;
    @CsvBindByPosition(position = 28)
    private String document_identifier;

    Paper toPaper() {
        Paper paper = new Paper();
        try {
            paper.setPdf_link(new URL(pdf_link));
        } catch (MalformedURLException e) {
            logger.error("Parsing PaperDelegation to Paper error. title: '" + title + "', exception: " + e.getMessage());
            return null;
        }
        ArrayList<Author_Affiliation> aas = new ArrayList<>();
        for (int i = 0; i < Math.min(authors.size(), affiliations.size()); i++) {
            if (authors.get(i).getName().isEmpty()) {
                aas = null;
                break;
            }
            aas.add(new Author_Affiliation(authors.get(i), affiliations.get(i)));
        }
        paper.setTitle(title);
        paper.setAa(aas);
        paper.setConference(conference);
        paper.setDate_added_Xplore(date_added_Xplore);
        paper.setVolume(volume);
        if (start_page != null && !start_page.isEmpty() && StringUtils.isNumeric(start_page))
            paper.setStart_page(Integer.parseInt(start_page));
        if (end_page != null && !end_page.isEmpty() && StringUtils.isNumeric(end_page))
            paper.setEnd_page(Integer.parseInt(end_page));
        paper.setSummary(summary);
        paper.setIssn(issn);
        paper.setIsbn(isbn);
        paper.setDoi(doi);
        paper.setFunding_info(funding_info);
        paper.setAuthor_keywords(ak);
        paper.setIeee_terms(ie3t);
        paper.setInspec_controlled(ict);
        paper.setInspec_non_controlled(inct);
        paper.setMesh_terms(mt);
        if (citation == null) paper.setCitation(0);
        else paper.setCitation(citation);
        if (reference == null) paper.setReference(0);
        else paper.setReference(reference);
        paper.setLicense(license);
        paper.setOnline_date(online_date);
        paper.setIssue_date(issue_date);
        paper.setMeeting_date(meeting_date);
        paper.setPublisher(publisher);
        paper.setDocument_identifier(document_identifier);
        return paper;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<Affiliation> getAffiliations() {
        return affiliations;
    }

    public void setAffiliations(List<Affiliation> affiliations) {
        this.affiliations = affiliations;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public Date getDate_added_Xplore() {
        return date_added_Xplore;
    }

    public void setDate_added_Xplore(Date date_added_Xplore) {
        this.date_added_Xplore = date_added_Xplore;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public String getStart_page() {
        return start_page;
    }

    public void setStart_page(String start_page) {
        this.start_page = start_page;
    }

    public String getEnd_page() {
        return end_page;
    }

    public void setEnd_page(String end_page) {
        this.end_page = end_page;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getFunding_info() {
        return funding_info;
    }

    public void setFunding_info(String funding_info) {
        this.funding_info = funding_info;
    }

    public String getPdf_link() {
        return pdf_link;
    }

    public void setPdf_link(String pdf_link) {
        this.pdf_link = pdf_link;
    }

    public List<Term> getAk() {
        return ak;
    }

    public void setAk(List<Term> ak) {
        this.ak = ak;
    }

    public List<Term> getIe3t() {
        return ie3t;
    }

    public void setIe3t(List<Term> ie3t) {
        this.ie3t = ie3t;
    }

    public List<Term> getIct() {
        return ict;
    }

    public void setIct(List<Term> ict) {
        this.ict = ict;
    }

    public List<Term> getInct() {
        return inct;
    }

    public void setInct(List<Term> inct) {
        this.inct = inct;
    }

    public List<Term> getMt() {
        return mt;
    }

    public void setMt(List<Term> mt) {
        this.mt = mt;
    }

    public Integer getCitation() {
        return citation;
    }

    public void setCitation(Integer citation) {
        this.citation = citation;
    }

    public Integer getReference() {
        return reference;
    }

    public void setReference(Integer reference) {
        this.reference = reference;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Date getOnline_date() {
        return online_date;
    }

    public void setOnline_date(Date online_date) {
        this.online_date = online_date;
    }

    public Date getIssue_date() {
        return issue_date;
    }

    public void setIssue_date(Date issue_date) {
        this.issue_date = issue_date;
    }

    public Date getMeeting_date() {
        return meeting_date;
    }

    public void setMeeting_date(Date meeting_date) {
        this.meeting_date = meeting_date;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDocument_identifier() {
        return document_identifier;
    }

    public void setDocument_identifier(String document_identifier) {
        this.document_identifier = document_identifier;
    }

    public PaperDelegation() {
    }
}
