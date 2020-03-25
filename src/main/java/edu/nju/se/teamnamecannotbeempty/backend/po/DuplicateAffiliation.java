package edu.nju.se.teamnamecannotbeempty.backend.po;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
public class DuplicateAffiliation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    //被怀疑有重复的
    private Affiliation source;
    @ManyToOne
    //发生重复的对象
    private Affiliation target;
    @ColumnDefault("false")
    //是否已经处理
    private Boolean clear;

    public DuplicateAffiliation() {
        clear = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Affiliation getSource() {
        return source;
    }

    public void setSource(Affiliation source) {
        this.source = source;
    }

    public Affiliation getTarget() {
        return target;
    }

    public void setTarget(Affiliation target) {
        this.target = target;
    }

    public Boolean getClear() {
        return clear;
    }

    public void setClear(Boolean clear) {
        this.clear = clear;
    }
}
