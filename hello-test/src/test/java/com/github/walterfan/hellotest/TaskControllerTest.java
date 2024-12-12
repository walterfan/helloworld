package com.github.walterfan.hellotest;


import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.inject.Inject;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertNotNull;

/**
 * Created by yafan on 20/11/2017.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
public class TaskControllerTest  {

    @Autowired
    private MockMvc mockMvc;

    private TaskRepository taskRepository;



    private List<Task> getDefaultTaskList() {
        Task task = new Task();
        task.setDeadline(Instant.now().plus(25, ChronoUnit.MINUTES) );
        task.setDuration(Duration.ofMinutes(25));
        task.setId(UUID.randomUUID());
        task.setName("learnSpringTest");

        return Arrays.asList(task);
    }

    @BeforeMethod
    public void setup() {
        taskRepository = Mockito.mock(TaskRepository.class, Mockito.withSettings().defaultAnswer(Mockito.RETURNS_SMART_NULLS));
    }

    @Test
    public void getTasks() throws Exception {

        given(taskRepository.findAll(new PageRequest(0, 20)))
                .willReturn(new PageImpl<Task>(getDefaultTaskList()));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.[0].duration").value("PT25M"))
                .andExpect(jsonPath("$.[0].name").value("learnSpringTest"))
        ;



    }
}
