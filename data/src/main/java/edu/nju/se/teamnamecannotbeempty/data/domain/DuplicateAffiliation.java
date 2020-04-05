package edu.nju.se.teamnamecannotbeempty.data.domain;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class DuplicateAffiliation implements IDuplication<Affiliation> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.MERGE)
    //被怀疑有重复的
    private Affiliation father;
    @ManyToOne(cascade = CascadeType.MERGE)
    //发生重复的对象
    private Affiliation son;
    @ColumnDefault("false")
    //是否已经处理
    private Boolean clear;
    @Column(nullable = false)
    @LastModifiedDate
    private Date updatedAt;

    public DuplicateAffiliation() {
        clear = false;
    }

    public DuplicateAffiliation(Affiliation father, Affiliation son) {
        this();
        this.father = father;
        this.son = son;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Affiliation getFather() {
        return father;
    }

    public void setFather(Affiliation source) {
        this.father = source;
    }

    @Override
    public Affiliation getSon() {
        return son;
    }

    public void setSon(Affiliation target) {
        this.son = target;
    }

    public Boolean getClear() {
        return clear;
    }

    @Override
    public void setClear(Boolean clear) {
        this.clear = clear;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
