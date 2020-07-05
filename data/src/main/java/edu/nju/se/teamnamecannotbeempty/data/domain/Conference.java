package edu.nju.se.teamnamecannotbeempty.data.domain;

import org.hibernate.search.annotations.Field;

import javax.persistence.*;
import java.util.Objects;

/**
 * 在迭代一和迭代二中作为会议PO，在迭代三中统一为出版物PO
 * 年份因为数据源扩展后数据无法统一，故不再作为论文的发表年份根据
 * 在论文PO中新增年份属性
 */
@Entity
@Table(name = "conferences")
public class Conference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "c_name")
    @Field
    private String name;
    @Column(name = "hold_year")
    // 出版或会议举办年份，可能为空
    private Integer year;
    // 届数，可能为空
    private Integer ordno;

    @Transient
    private String year_highlight;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conference that = (Conference) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(year, that.year) &&
                Objects.equals(ordno, that.ordno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, year, ordno);
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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getOrdno() {
        return ordno;
    }

    public void setOrdno(Integer ordno) {
        this.ordno = ordno;
    }

    public String getYear_highlight() {
        return year_highlight;
    }

    public void setYear_highlight(String year_highlight) {
        this.year_highlight = year_highlight;
    }
}
