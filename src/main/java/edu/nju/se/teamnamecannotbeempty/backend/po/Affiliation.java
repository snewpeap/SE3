package edu.nju.se.teamnamecannotbeempty.backend.po;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;
import java.io.Serializable;
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
    // 实际上是地理位置，最精确到市
    private String country;
    @ManyToOne
    //别名，在需要去重的时候为空；去重后，如果没有重复为this，否则为重复对象
    private Affiliation alias;

    @Override
    public Affiliation getSelf() {
        return this;
    }

    public Affiliation() {
    }

    @Entity(name = "affi_popularity")
    public static class Popularity implements Serializable{
        @Id @GeneratedValue
        private Long id;
        @OneToOne(optional = false)
        private Affiliation affiliation;
        @ColumnDefault("0.0")
        private Double popularity;

        public Popularity(Affiliation affiliation, Double popularity) {
            this.affiliation = affiliation;
            this.popularity = popularity;
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
    }

    @Override
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
