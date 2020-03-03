package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.List;

public class SimplePaperVO {

    public SimplePaperVO(long id, String title, List<Author_SimpleAffiliationVO> author_simpleAffiliationVOS, String publicationTitle, List<String> keywords) {
        this.id = id;
        this.title = title;
        this.author_simpleAffiliationVOS = author_simpleAffiliationVOS;
        this.publicationTitle = publicationTitle;
        this.keywords = keywords;
    }

    private long id;

    private String title;

    private List<Author_SimpleAffiliationVO> author_simpleAffiliationVOS;


    private String publicationTitle;

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

    public boolean equals(Object o){
        if(!(o instanceof SimplePaperVO)) return false;
        return id==((SimplePaperVO) o).id&&title.equals(((SimplePaperVO) o).title)&&author_simpleAffiliationVOS.equals(((SimplePaperVO) o).author_simpleAffiliationVOS)
                &&publicationTitle.equals(((SimplePaperVO) o).publicationTitle)&&keywords.equals(((SimplePaperVO) o).keywords);
    }
}
