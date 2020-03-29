package edu.nju.se.teamnamecannotbeempty.backend.po;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class DuplicateAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    //被怀疑有重复的
    private Author source;
    @ManyToOne
    //发生重复的对象
    private Author target;
    //标识是否已经处理了这条
    @ColumnDefault("false")
    private Boolean clear;
    @LastModifiedDate
    private Date updatedAt;

    public DuplicateAuthor() {
        clear = false;
    }

    public Boolean getClear() {
        return clear;
    }

    public void setClear(Boolean clear) {
        this.clear = clear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Author getSource() {
        return source;
    }

    public void setSource(Author source) {
        this.source = source;
    }

    public Author getTarget() {
        return target;
    }

    public void setTarget(Author target) {
        this.target = target;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
