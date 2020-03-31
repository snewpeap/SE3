package edu.nju.se.teamnamecannotbeempty.data.domain;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
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
    @Column(nullable = false)
    @LastModifiedDate
    private Date updatedAt;

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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
