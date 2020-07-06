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
@Table(name = "authors")
public class Author implements Aliasable<Author> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "au_name", nullable = false)
    @Field
    private String name;
    private String lowerCaseName;
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_AUTHOR_ALIAS"))
    //别名，在需要去重的时候为空；去重后，如果没有重复为this，否则为重复对象
    private Author alias;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "author")
    @Fetch(FetchMode.SUBSELECT)
    private List<Popularity> pops = new ArrayList<>();

    @Override
    public Author getActual() {
        return (alias == null || this.equals(alias)) ? this : alias.getActual();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.id) &&
                Objects.equals(alias, author.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, alias);
    }

    public Author() {
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Entity(name = "author_popularity")
    @Table(indexes = {
            @javax.persistence.Index(name = "POPULARITY_DESC", columnList = "popularity DESC"),
            @javax.persistence.Index(name = "YEAR", columnList = "year")
    })
    public static class Popularity implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @ManyToOne(optional = false)
        @JoinColumn(foreignKey = @ForeignKey(name = "FK_POP_AUTHOR"))
        private Author author;
        @ColumnDefault("0.0")
        private Double popularity;
        private Integer year;

        public Popularity(Author author, Double popularity) {
            this.author = author;
            this.popularity = popularity;
        }

        public Popularity(Author author, Double popularity, Integer year) {
            this.author = author;
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
            return Objects.equals(author, that.author) &&
                    Objects.equals(year, that.year);
        }

        @Override
        public int hashCode() {
            return Objects.hash(author, year);
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Author getAuthor() {
            return author;
        }

        public void setAuthor(Author author) {
            this.author = author;
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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
        if (id == null) {
            System.out.println(name + "'s ID set to null");
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Author getAlias() {
        return alias;
    }

    @Override
    public void setAlias(Author alias) {
        this.alias = alias;
    }

    public String getLowerCaseName() {
        return lowerCaseName;
    }

    public void setLowerCaseName(String lowerCaseName) {
        this.lowerCaseName = lowerCaseName;
    }

    public List<Popularity> getPops() {
        return pops;
    }

    public void setPops(List<Popularity> pops) {
        this.pops = pops;
    }
}
