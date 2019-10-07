package com.github.walterfan.hellospringsecurity;

import lombok.Data;

import static javax.persistence.GenerationType.AUTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Data
@Entity
@Table(name="user_roles",uniqueConstraints=@UniqueConstraint(
        columnNames={"role","username"}))
public class UserRole {
    @Id
    @GeneratedValue(strategy=AUTO)
    @Column(unique=true,nullable=false)
    private Integer id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="username",nullable=false)
    private User user;
    @Column(nullable=false,length=45)
    private String role;


}
