package edu.nju.se.teamnamecannotbeempty.backend.po;

import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class Author_Affiliation {
    @ManyToOne
    @IndexedEmbedded(depth = 1)
    private Author author;
    @ManyToOne
    @IndexedEmbedded(depth = 1)
    private Affiliation affiliation;

    @Override
    public String toString() {
        return "Author_Affiliation{" +
                "author=" + author +
                ", affiliation=" + affiliation +
                '}';
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
