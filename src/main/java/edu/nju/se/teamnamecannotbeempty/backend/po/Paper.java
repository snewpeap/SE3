package edu.nju.se.teamnamecannotbeempty.backend.po;

import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "papers")
@Indexed
public class Paper {
    @Id
    // 论文的id
    private Long id;
    @Column(nullable = false)
    @Fields({
            @Field(name = "title", index = Index.YES, analyze = Analyze.YES, store = Store.NO),
            @Field(name = "sortTitle", index = Index.NO, analyze = Analyze.NO, store = Store.NO)
    })
    @SortableField(forField = "sortTitle")
    // 论文的标题
    private String title;
    @ElementCollection
    @IndexedEmbedded
    // 发表论文的每个作者-机构构成的对象的列表
    private List<Author_Affiliation> aa = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "conference_id", foreignKey = @ForeignKey(name = "FK_PAPER_CONFERENCE"))
    @IndexedEmbedded
    // 会议对象。对应数据中的出版物
    private Conference conference;
    @Temporal(TemporalType.DATE)
    // 加入Xplore的时间
    private Date date_added_Xplore;
    // 卷数
    private Integer volume;
    // 开始页
    private Integer start_page;
    // 结束页
    private Integer end_page;
    @Column(name = "abstract", columnDefinition = "TEXT")
    // 摘要。对应数据中的abstract
    private String summary;
    // issn号
    private String issn;
    // isbn号
    private String isbn;
    // doi号，加上
    private String doi;
    // 资助信息，根据Xplore来看是一串编码
    private String funding_info;
    // pdf原文链接
    private URL pdf_link;
    @ManyToMany
    @IndexedEmbedded
    // 作者给出的关键字
    private List<Term> author_keywords = new ArrayList<>();
    @ManyToMany
    // IEEE术语
    private List<Term> ieee_terms = new ArrayList<>();
    @ManyToMany
    // INSPEC受控索引，有限集合
    private List<Term> inspec_controlled = new ArrayList<>();
    @ManyToMany
    // INSPEC非受控索引，无限集合
    private List<Term> inspec_non_controlled = new ArrayList<>();
    @ManyToMany
    // mesh terms，作用未知
    private List<Term> mesh_terms = new ArrayList<>();
    // 被引数
    private Integer citation;
    // 引文数
    private Integer reference;
    // 许可证，有限集合
    private String license;
    @Temporal(TemporalType.DATE)
    private Date online_date;
    @Temporal(TemporalType.DATE)
    private Date issue_date;
    @Temporal(TemporalType.DATE)
    private Date meeting_date;
    // 出版商
    private String publisher;
    // 文档标识符？
    private String Document_identifier;

    public Paper() {
    }

    @Override
    public String toString() {
        return "Paper{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", aa=" + aa +
                ", conference=" + conference +
                '}';
    }

    public static String getFieldName_searchByTitle() {
        return "title";
    }

    public static String getFieldName_searchByAuthor() {
        return "aa.author.name";
    }

    public static String getFieldName_searchByAffiliation() {
        return "aa.affiliation.name";
    }

    public static String getFieldName_searchByConference() {
        return "conference.name";
    }

    public static String getFieldName_searchByAuthorKeywords() {
        return "author_keywords.content";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author_Affiliation> getAa() {
        return aa;
    }

    public void setAa(List<Author_Affiliation> aa) {
        this.aa = aa;
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

    public Integer getStart_page() {
        return start_page;
    }

    public void setStart_page(Integer start_page) {
        this.start_page = start_page;
    }

    public Integer getEnd_page() {
        return end_page;
    }

    public void setEnd_page(Integer end_page) {
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

    public URL getPdf_link() {
        return pdf_link;
    }

    public void setPdf_link(URL pdf_link) {
        this.pdf_link = pdf_link;
    }

    public List<Term> getAuthor_keywords() {
        return author_keywords;
    }

    public void setAuthor_keywords(List<Term> author_keywords) {
        this.author_keywords = author_keywords;
    }

    public List<Term> getIeee_terms() {
        return ieee_terms;
    }

    public void setIeee_terms(List<Term> ieee_terms) {
        this.ieee_terms = ieee_terms;
    }

    public List<Term> getInspec_controlled() {
        return inspec_controlled;
    }

    public void setInspec_controlled(List<Term> inspec_controlled) {
        this.inspec_controlled = inspec_controlled;
    }

    public List<Term> getInspec_non_controlled() {
        return inspec_non_controlled;
    }

    public void setInspec_non_controlled(List<Term> inspec_non_controlled) {
        this.inspec_non_controlled = inspec_non_controlled;
    }

    public List<Term> getMesh_terms() {
        return mesh_terms;
    }

    public void setMesh_terms(List<Term> mesh_terms) {
        this.mesh_terms = mesh_terms;
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
        return Document_identifier;
    }

    public void setDocument_identifier(String document_identifier) {
        Document_identifier = document_identifier;
    }
}
