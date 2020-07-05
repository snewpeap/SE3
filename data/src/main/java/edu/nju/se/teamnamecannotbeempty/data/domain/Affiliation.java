package edu.nju.se.teamnamecannotbeempty.data.domain;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "affiliations")
public class Affiliation implements Aliasable<Affiliation> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "af_name", nullable = false)
    @Field
    // 机构名，包括部门/院系名和组织/单位名（如软件学院of南京大学）
    private String name;
    private String formattedName;
    // 实际上是地理位置，最精确到市
    private String country;
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_AFFI_ALIAS"))
    //别名，在需要去重的时候为空；去重后，如果没有重复为this，否则为重复对象
    private Affiliation alias;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "affiliation")
    @Fetch(FetchMode.SUBSELECT)
    private List<Popularity> pops = new ArrayList<>();

    public Affiliation() {
    }

    @Entity(name = "affi_popularity")
    public static class Popularity implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @ManyToOne(optional = false)
        @JoinColumn(foreignKey = @ForeignKey(name = "FK_POP_AFFI"))
        private Affiliation affiliation;
        @ColumnDefault("0.0")
        private Double popularity;
        private Integer year;

        public Popularity(Affiliation affiliation, Double popularity) {
            this.affiliation = affiliation;
            this.popularity = popularity;
        }

        public Popularity(Affiliation affiliation, Double popularity, Integer year) {
            this.affiliation = affiliation;
            this.popularity = popularity;
            this.year = year;
        }

        public Popularity() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Popularity that = (Popularity) o;
            return affiliation.equals(that.affiliation);
        }

        @Override
        public int hashCode() {
            return Objects.hash(affiliation);
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Affiliation getAffiliation() {
            return affiliation;
        }

        public void setAffiliation(Affiliation affiliation) {
            this.affiliation = affiliation;
        }

        public Double getPopularity() {
            return popularity;
        }

        public void setPopularity(Double popularity) {
            this.popularity = popularity;
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }
    }

    @Override
    public Affiliation getActual() {
        return (alias == null || this.equals(alias)) ? this : alias.getActual();
    }

    public Affiliation getAlias() {
        return alias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Affiliation that = (Affiliation) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(alias, that.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, alias);
    }

    @Override
    public void setAlias(Affiliation alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "Affiliation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormattedName() {
        return formattedName;
    }

    public void setFormattedName(String formatted_name) {
        this.formattedName = formatted_name;
    }

    @Override
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

    public List<Popularity> getPops() {
        return pops;
    }

    public void setPops(List<Popularity> pops) {
        this.pops = pops;
    }
}
