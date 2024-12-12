package com.github.walterfan.hellotest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by yafan on 19/11/2017.
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    public List<Task> listTasks(@RequestParam(name="page", required=false)Integer pageNum,
                                @RequestParam(name="size", required=false)Integer pageSize) {
        PageRequest pageRequest = new PageRequest(pageNum==null?0:pageNum, pageSize==null?20:pageSize);
        return taskRepository.findAll(pageRequest).getContent();
    }

    @PostMapping
    public Task addTask(Task task) {
        return taskRepository.save(task);
    }
}
