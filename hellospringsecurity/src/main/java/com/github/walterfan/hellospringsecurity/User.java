package com.github.walterfan.hellospringsecurity;

import lombok.Data;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Data
@Entity
@Table(name="users")

public class User extends AbstractPersistable<Long> {

    @Column(name="username",unique=true,nullable=false,length=45)
    private String username;

    @Column(name="email",unique=true,nullable=false,length=45)
    private String email;

    @Column(name="password",nullable=false,length=60)
    private String password;

    @Column(name="enabled",nullable=false)
    private boolean enabled;

    @OneToMany(fetch=FetchType.LAZY,mappedBy="user")
    private Set<UserRole> userRole=new HashSet<UserRole>(0);

    @Column(name = "active")
    private int active;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

}
