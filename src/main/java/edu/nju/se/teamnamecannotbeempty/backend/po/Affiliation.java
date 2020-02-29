package edu.nju.se.teamnamecannotbeempty.backend.po;

import org.hibernate.search.annotations.Field;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "affiliations")
public class Affiliation {
    @Id
    private Long id;
    @Column(name = "af_name", nullable = false)
    @Field
    // 机构名，包括部门/院系名和组织/单位名（如软件学院of南京大学）
    private String name;
    // 实际上是地理位置，最精确到市
    private String country;

    public Affiliation() {
    }

    @Override
    public String toString() {
        return "Affiliation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
