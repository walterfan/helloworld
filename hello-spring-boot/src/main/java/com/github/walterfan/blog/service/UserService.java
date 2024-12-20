package com.github.walterfan.blog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.github.walterfan.blog.entity.User;
import com.github.walterfan.blog.repository.UserRepository;
import org.springframework.data.domain.Sort;

@Service
public class UserService {
	
    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> readUser(String id) {
        return userRepository.findById(id);
    }

    public List<User> readUsers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> users = userRepository.findAll(pageRequest);
        return users.getContent();
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

}
