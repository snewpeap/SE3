package edu.nju.se.teamnamecannotbeempty.data.domain;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "terms")
public class Term {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @Field
    private String content;
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "term")
//    @Fetch(FetchMode.SUBSELECT)
//    private Set<Popularity> pops = new HashSet<>();

    public Term() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Term term = (Term) o;
        return Objects.equals(id, term.id);
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

    @Entity(name = "term_popularity")
    public static class Popularity implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @ManyToOne(optional = false)
        @JoinColumn(foreignKey = @ForeignKey(name = "FK_POP_TERM"))
        private Term term;
        @ColumnDefault("0.0")
        private Double popularity;
        private Integer year;

        public Popularity(Term term, Double popularity) {
            this.term = term;
            this.popularity = popularity;
        }

        public Popularity(Term term, Double popularity, Integer year) {
            this.term = term;
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
            return term.equals(that.term);
        }

        @Override
        public int hashCode() {
            return Objects.hash(term);
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
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

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
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

//    public Set<Popularity> getPops() {
//        return pops;
//    }
//
//    public void setPops(Set<Popularity> pops) {
//        this.pops = pops;
//    }
}
