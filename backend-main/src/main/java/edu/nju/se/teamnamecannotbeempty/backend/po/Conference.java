package edu.nju.se.teamnamecannotbeempty.backend.po;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.hibernate.search.annotations.*;
import org.hibernate.search.bridge.builtin.IntegerBridge;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "conferences")
@Indexed
public class Conference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "c_name")
    @Field
    private String name;
    @Column(name = "hold_year")
    @Field(name = "year", store = Store.YES)
    @NumericField(forField = "year")
    @SortableField(forField = "year")
    @Field(
            name = "search_year",
            bridge = @FieldBridge(impl = IntegerBridge.class),
            analyzer = @Analyzer(impl = KeywordAnalyzer.class)
    )
    private Integer year;
    // 届数
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

    public String buildName() {
        return String.valueOf(year) + " " + ordno + " " + name;
    }
}
