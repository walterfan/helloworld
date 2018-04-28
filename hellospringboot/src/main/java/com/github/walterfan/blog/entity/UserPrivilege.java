package com.github.walterfan.blog.entity;

import lombok.Data;

import java.util.Set;

/**
 * Created by yafan on 24/9/2017.
 */
@Data
public class UserPrivilege {

    private User user;

    private Set<Role> roles;


}
