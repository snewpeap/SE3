package edu.nju.se.teamnamecannotbeempty.backend.po;

import javax.persistence.*;
import java.util.StringJoiner;

/**
 * 引用对象，标识了一对引用-被引关系，但被引文章不一定在数据库内
 */
@Entity
@Table(name = "refs")
public class Ref {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    //引用者
    private Paper referer;
    @Column(nullable = false)
    private String refTitle;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "referee_id", foreignKey = @ForeignKey(name = "FK_REFEREE"))
    //被引用者，可能为空
    private Paper referee;

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

    public Paper getReferee() {
        return referee;
    }

    public void setReferee(Paper referee) {
        this.referee = referee;
    }
}
