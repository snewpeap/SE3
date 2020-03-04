package edu.nju.se.teamnamecannotbeempty.backend.vo;

public class AffiliationVO {
    // 机构名，包括部门/院系名和组织/单位名（如软件学院of南京大学）
    private String name;
    // 实际上是地理位置，最精确到市
    private String country;

    public AffiliationVO(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean equals(Object o){
        if(!(o instanceof AffiliationVO)) return false;
        return name.equals(((AffiliationVO) o).name)&&country.equals(((AffiliationVO) o).country);
    }
}
