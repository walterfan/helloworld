package com.github.walterfan.hellomybatis.service;

import com.github.walterfan.hellomybatis.mapper.UserMapper;
import com.github.walterfan.hellomybatis.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<User> getAllUsers() {
        return userMapper.findAll();
    }
}