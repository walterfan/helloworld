package com.github.walterfan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yafan on 17/11/2017.
 */
@Service
public class  HelloRedisService {



    @Autowired
    private TaskRepository taskRepository;



    public void addTask(Task task) {
         taskRepository.saveTask(task);
    }


    public Task getTask(String id) {
        return taskRepository.findTask(id);
    }


    public void deleteTask(String id) {
         taskRepository.deleteTask(id);
    }
}
