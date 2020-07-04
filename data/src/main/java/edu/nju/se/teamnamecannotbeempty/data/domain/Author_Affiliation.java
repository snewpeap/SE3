package edu.nju.se.teamnamecannotbeempty.data.domain;

import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;

@Embeddable
public class Author_Affiliation {
    @ManyToOne(cascade = CascadeType.DETACH)
    @IndexedEmbedded(depth = 1)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_AA_AUTHOR"))
    private Author author;
    @ManyToOne(cascade = CascadeType.DETACH)
    @IndexedEmbedded(depth = 1)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_AA_AFFI"))
    private Affiliation affiliation;

    @Override
    public String toString() {
        return "Author_Affiliation{" +
                "author=" + author +
                ", affiliation=" + affiliation +
                '}';
    }

    public Author_Affiliation(Author author, Affiliation affiliation) {
        this.author = author;
        this.affiliation = affiliation;
    }

    public Author_Affiliation() {
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Affiliation getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(Affiliation affiliation) {
        this.affiliation = affiliation;
    }
}
