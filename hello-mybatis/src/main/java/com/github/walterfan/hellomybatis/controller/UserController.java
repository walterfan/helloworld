package com.github.walterfan.hellomybatis.controller;

import com.github.walterfan.hellomybatis.model.User;
import com.github.walterfan.hellomybatis.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}