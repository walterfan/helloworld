package com.github.walterfan.blog.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yafan on 17/12/2017.
 */
@Data
@Entity
@Table(name="tag")
public class Tag {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    @Column(name = "tag_id")
    private String id;

    private String value;

    @ManyToMany(mappedBy = "tags")
    private List<Article> articles = new ArrayList<>();
}
