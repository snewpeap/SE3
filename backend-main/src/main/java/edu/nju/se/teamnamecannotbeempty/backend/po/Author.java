package edu.nju.se.teamnamecannotbeempty.backend.po;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;
import java.io.Serializable;
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
    @ManyToOne
    //别名，在需要去重的时候为空；去重后，如果没有重复为this，否则为重复对象
    private Author alias;

    @Override
    public Author getSelf() {
        return this;
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
    public static class Popularity implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @OneToOne(optional = false)
        private Author author;
        @ColumnDefault("0.0")
        private Double popularity;

        public Popularity(Author author, Double popularity) {
            this.author = author;
            this.popularity = popularity;
        }

        public Popularity() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Popularity that = (Popularity) o;
            return author.equals(that.author);
        }

        @Override
        public int hashCode() {
            return Objects.hash(author);
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

    public Author getAlias() {
        return alias;
    }

    public void setAlias(Author alias) {
        this.alias = alias;
    }
}
