package com.github.walterfan.hellospringsecurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public interface UserService {

    User findUserByEmail(String email);

    User saveUser(User user);

    User getActiveUser();
}
