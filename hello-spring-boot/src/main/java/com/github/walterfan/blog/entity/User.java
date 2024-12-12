package com.github.walterfan.blog.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by walterfan on 7/2/2017.
 */
@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    @Column(name = "user_id")
    private String id;


    private String username;

    private String password;

    private String email;


    private String phoneNumber;


    private UserStatus status;


    private Date createTime;


    private Date lastModifiedTime;

    @OneToMany(cascade= CascadeType.ALL)
    private List<Token> tokens;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User() {
        this.status = UserStatus.pending;
        this.createTime = new Date(System.currentTimeMillis());
        this.lastModifiedTime = new Date(System.currentTimeMillis());
    }
}
