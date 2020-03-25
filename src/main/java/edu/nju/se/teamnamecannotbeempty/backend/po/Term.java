package edu.nju.se.teamnamecannotbeempty.backend.po;

import org.hibernate.search.annotations.Field;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity @Table(name = "terms")
public class Term implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) @Field
    private String content;

    public Term() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Term term = (Term) o;
        return id.equals(term.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Term{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }

    @Entity @Table(name = "term_popularity")
    public static class Popularity implements Serializable {
        @Id @OneToOne(optional = false)
        private Term term;
        private Double popularity;

        public Popularity(Term term, Double popularity) {
            this.term = term;
            this.popularity = popularity;
        }

        public Popularity() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Popularity that = (Popularity) o;
            return term.equals(that.term);
        }

        @Override
        public int hashCode() {
            return Objects.hash(term);
        }

        public Term getTerm() {
            return term;
        }

        public void setTerm(Term term) {
            this.term = term;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
