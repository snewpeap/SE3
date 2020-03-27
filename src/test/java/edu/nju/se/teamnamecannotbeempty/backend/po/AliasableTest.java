package edu.nju.se.teamnamecannotbeempty.backend.po;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AliasableTest {

    @Test
    public void getActual_null() {
        Author author = new Author();
        author.setAlias(null);
        assertEquals(author, author.getActual());
    }

    @Test
    public void getActual_self() {
        Affiliation affiliation = new Affiliation();
        affiliation.setAlias(affiliation);
        assertEquals(affiliation, affiliation.getAlias());
    }

    @Test
    public void getActual() {
        Affiliation affiliation = new Affiliation();
        Affiliation actual = new Affiliation();
        affiliation.setAlias(actual);
        actual.setAlias(null);
        assertEquals(actual, affiliation.getActual());
    }
}