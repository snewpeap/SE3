package edu.nju.se.teamnamecannotbeempty.backend.po;

import org.hibernate.search.annotations.Field;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "authors")
public class Author {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return name.equals(author.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
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
