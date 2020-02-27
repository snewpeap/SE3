package edu.nju.se.teamnamecannotbeempty.backend.po;

import javax.persistence.*;

@Entity
@Table(name = "conferences")
public class Conference {
    @Id
    private Long id;
    @Column(name = "c_name")
    @Enumerated(EnumType.ORDINAL)
    private Conference_name name;
    @Column(name = "hold_year")
    private Integer year;
    // 届数
    private Integer ordno;

    public Conference() {
    }

    @Override
    public String toString() {
        return "Conference{" +
                "id=" + id +
                ", name=" + name +
                ", year=" + year +
                ", ordno=" + ordno +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Conference_name getName() {
        return name;
    }

    public void setName(Conference_name name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getOrdno() {
        return ordno;
    }

    public void setOrdno(Integer order) {
        this.ordno = order;
    }
}

enum Conference_name {
    ASE("IEEE/ACM International Conference on Automated Software Engineering (ASE)"),
    ICSE("IEEE/ACM International Conference on Software Engineering (ICSE)");

    public String fullText;

    Conference_name(String fullText){
        this.fullText = fullText;
    }
}
