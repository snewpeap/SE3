package edu.nju.se.teamnamecannotbeempty.data.domain;

import javax.persistence.*;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * 引用对象，标识了一对引用-被引关系，但被引文章不一定在数据库内
 */
@Entity
@Table(name = "refs", indexes = {
        @Index(name = "ref_lowercase_title", columnList = "lowercaseTitle")
})
public class Ref {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    //引用者
    private Paper referer;
    @Column(nullable = false)
    private String refTitle;
    @Column(nullable = false)
    private String lowercaseTitle;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "referee_id", foreignKey = @ForeignKey(name = "FK_REFEREE"))
    //被引用者，可能为空
    private Paper referee;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ref ref = (Ref) o;
        return Objects.equals(id, ref.id) &&
                referer.equals(ref.referer) &&
                refTitle.equals(ref.refTitle) &&
                Objects.equals(referee, ref.referee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, referer, refTitle, referee);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Ref.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("referer=" + referer)
                .add("refTitle='" + refTitle + "'")
                .add("referee=" + referee)
                .toString();
    }

    public Ref() {
    }

    public Ref(String refTitle) {
        this.refTitle = refTitle;
        this.lowercaseTitle = refTitle.toLowerCase();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Paper getReferer() {
        return referer;
    }

    public void setReferer(Paper referer) {
        this.referer = referer;
    }

    public String getRefTitle() {
        return refTitle;
    }

    public void setRefTitle(String refTitle) {
        this.refTitle = refTitle;
    }

    public String getLowercaseTitle() {
        return lowercaseTitle;
    }

    public void setLowercaseTitle(String lowercaseTitle) {
        this.lowercaseTitle = lowercaseTitle;
    }

    public Paper getReferee() {
        return referee;
    }

    public void setReferee(Paper referee) {
        this.referee = referee;
    }
}
