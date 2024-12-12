package com.github.walterfan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.Instant;
import java.util.UUID;

@SpringBootApplication
public class HelloRedisApplication implements CommandLineRunner {

	@Autowired
	private HelloRedisService helloRedisService;

	@Override
	public void run(String... args) {
		Task task = new Task();

		task.setId(UUID.randomUUID().toString());
		task.setName("write blog");
		task.setProgress(80);
		task.setDeadline(Instant.now());

		helloRedisService.addTask(task);

		Task foundTask = helloRedisService.getTask(task.getId());

		System.out.println(foundTask);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(HelloRedisApplication.class, args).close();
	}

}
