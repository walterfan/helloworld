package com.github.walterfan.blog.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.walterfan.blog.entity.User;
import com.github.walterfan.blog.entity.User;
import com.github.walterfan.blog.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class UserApiController {
	@Autowired
    private UserService userService;

    @RequestMapping(value = "/users" , method = RequestMethod.POST)
    public User createUser(@RequestBody @Valid User user) {
        log.info("createMeetingSession: " + user);
        return userService.createUser(user);
    }

    @RequestMapping(value = "/users/{id}" , method = RequestMethod.PUT)
    public User updateUser(@PathVariable("id") String userId, @RequestBody @Valid User user) {
        user.setId(userId);
        log.info("updateUser: " + user);
        return userService.createUser(user);
    }

    @RequestMapping(value = "/users/{id}" , method = RequestMethod.GET)
    public User readUser(@PathVariable("id") String userId) {
        log.info("readUser: " + userId);
        return userService.readUser(userId).orElse(null);
    }

    @RequestMapping(value = "/users" , method = RequestMethod.GET)
    public List<User> readUser(@RequestParam(value="page", required = false) Integer page, @RequestParam(value="size", required = false) Integer size) {
        if(null == page) {
            page = 0;
        }
        if(null == size) {
            size = 20;
        }
        log.info("readUsers:{}, {} " , page, size);
        return userService.readUsers(page, size);
    }

    @RequestMapping(value = "/users/{id}" , method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable("id") String userId) {
        log.info("deleteUser: " + userId);
        userService.deleteUser(userId);
    }
}
