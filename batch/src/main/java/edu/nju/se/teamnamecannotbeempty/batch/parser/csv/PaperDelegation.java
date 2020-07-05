package edu.nju.se.teamnamecannotbeempty.batch.parser.csv;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters.ToAffiliation;
import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters.ToAuthor;
import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters.ToConference;
import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters.ToTerm;
import edu.nju.se.teamnamecannotbeempty.data.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Paper PO 在数据转换中的代理对象
 */
@SuppressWarnings("unused")
public class PaperDelegation {
    public static final Logger logger = LoggerFactory.getLogger(PaperDelegation.class);
    public static final String DEFAULT_SPLIT_ON = ",(?=[^ ])|; ";
    public static final String TERMS_SPLIT_ON = ";|, ?";

    @CsvBindByName(column = "Document Title")
    private String title;

    @CsvBindAndSplitByName(
            column = "Authors",
            elementType = Author.class,
            splitOn = DEFAULT_SPLIT_ON,
            converter = ToAuthor.class,
            collectionType = ArrayList.class
    )
    private List<Author> authors;

    @CsvBindAndSplitByName(
            column = "Author Affiliations",
            elementType = Affiliation.class,
            splitOn = DEFAULT_SPLIT_ON,
            converter = ToAffiliation.class,
            collectionType = ArrayList.class
    )
    private List<Affiliation> affiliations;

    @CsvCustomBindByName(
            column = "Publication Title",
            converter = ToConference.class
    )
    private Conference conference;

    @CsvBindByName(column = "Publication Year")
    private Integer year;
    @CsvBindByName(column = "Volume")
    private Integer volume;
    @CsvBindByName(column = "Start Page")
    private String start_page;
    @CsvBindByName(column = "End Page")
    private String end_page;
    @CsvBindByName(column = "Abstract")
    private String summary;
    @CsvBindByName(column = "ISSN")
    private String issn;
    @CsvBindByName(column = "ISBNs")
    private String isbn;
    @CsvBindByName(column = "DOI")
    private String doi;
    @CsvBindByName(column = "Funding Information")
    private String funding_info;
    @CsvBindByName(column = "PDF Link")
    private String pdf_link;

    @CsvBindAndSplitByName(
            column = "Author Keywords",
            elementType = Term.class,
            splitOn = TERMS_SPLIT_ON,
            converter = ToTerm.class,
            collectionType = ArrayList.class
    )
    //author keywords
    private List<Term> ak;

    @CsvBindByName(column = "Article Citation Count")
    private Integer citation;
    @CsvBindByName(column = "Reference Count")
    private Integer reference;
    @CsvBindByName(column = "Publisher")
    private String publisher;

    Paper toPaper() {
        Paper paper = new Paper();
        if (pdf_link != null && pdf_link.contains("arnumber=")) {
            paper.setIeeeId(Long.parseLong(pdf_link.split("arnumber=")[1]));
        }
        if (paper.getIeeeId() == null && StringUtils.isBlank(doi)) {
            return null;
        }
        ArrayList<Author_Affiliation> aas = new ArrayList<>();
        //有作者或机构为null的都是数据源出错了
        //因为作者和机构无法对齐了
        for (int i = 0; i < Math.min(authors.size(), affiliations.size()); i++) {
            if (authors.get(i) == null || affiliations.get(i) == null) {
                return null;
            }
            aas.add(new Author_Affiliation(authors.get(i), affiliations.get(i)));
        }
        paper.setAa(aas);
        paper.setDoi(StringUtils.isBlank(doi) ? null : doi.toUpperCase());
        paper.setPdf_link(pdf_link);
        paper.setTitle(title);

        if (conference.getName().equals("NA") && !StringUtils.isBlank(publisher)) {
            conference = ToConference.addConference(publisher);
        }
        paper.setConference(conference);
        paper.setVolume(volume);
        if (StringUtils.isNumeric(start_page))
            try {
                paper.setStart_page(Integer.parseInt(start_page));
            } catch (NumberFormatException e) {
                logger.warn(String.format("Start page of paper %s is %s, cannot parse", title, start_page));
            }
        if (StringUtils.isNumeric(end_page))
            try {
                paper.setEnd_page(Integer.parseInt(end_page));
            } catch (NumberFormatException e) {
                logger.warn(String.format("End page of paper %s is %s, cannot parse", title, start_page));
            }
        paper.setSummary(summary);
        paper.setIssn(issn);
        paper.setIsbn(isbn);
        paper.setFunding_info(funding_info);
        ak.removeIf(Objects::isNull);
        paper.setAuthor_keywords(ak);

        paper.setCitation(citation == null ? Integer.valueOf(0) : citation);
        paper.setReference(reference == null ? Integer.valueOf(0) : reference);
        paper.setPublisher(publisher);
        paper.setYear(year);
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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public PaperDelegation() {
    }
}
