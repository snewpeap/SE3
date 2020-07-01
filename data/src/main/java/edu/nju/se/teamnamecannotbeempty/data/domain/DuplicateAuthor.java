package edu.nju.se.teamnamecannotbeempty.data.domain;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class DuplicateAuthor implements IDuplication<Author> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_DUP_FATHER_AUTHOR"))
    //被怀疑有重复的
    private Author father;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_DUP_SON_AUTHOR"))
    //发生重复的对象
    private Author son;
    //标识是否已经处理了这条
    @ColumnDefault("false")
    private Boolean clear;
    @LastModifiedDate
    private Date updatedAt;

    public DuplicateAuthor() {
        clear = false;
    }

    public DuplicateAuthor(Author father, Author son) {
        this();
        this.father = father;
        this.son = son;
    }

    public Boolean getClear() {
        return clear;
    }

    @Override
    public void setClear(Boolean clear) {
        this.clear = clear;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Author getFather() {
        return father;
    }

    public void setFather(Author source) {
        this.father = source;
    }

    @Override
    public Author getSon() {
        return son;
    }

    public void setSon(Author target) {
        this.son = target;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
