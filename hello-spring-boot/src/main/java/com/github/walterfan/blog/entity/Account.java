package com.github.walterfan.blog.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by yafan on 24/9/2017.
 */
@Data
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    private String id;
    private String username;
    private String password;
    private String email;
    private String hint;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="bookmark_id")
    private Bookmark bookmark;
}
