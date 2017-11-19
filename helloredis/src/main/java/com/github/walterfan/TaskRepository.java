package com.github.walterfan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by yafan on 19/11/2017.
 */
@Repository
public class TaskRepository {

    private static final String KEY = "Task";

    @Autowired
    private RedisTemplate<String, Task> taskRedisTemplate;
    private HashOperations hashOps;

    @PostConstruct
    private void init() {
        hashOps = taskRedisTemplate.opsForHash();
    }

    public void saveTask(Task task) {
        hashOps.put(KEY, task.getId(), task);
    }

    public void updateTask(Task task) {
        hashOps.put(KEY, task.getId(), task);
    }

    public Task findTask(String id) {
        return (Task) hashOps.get(KEY, id);
    }

    public Map<Object, Object> findAllTasks() {
        return hashOps.entries(KEY);
    }

    public void deleteTask(String id) {
        hashOps.delete(KEY, id);
    }
}
