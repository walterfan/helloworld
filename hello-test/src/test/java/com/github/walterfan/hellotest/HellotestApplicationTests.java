package com.github.walterfan.hellotest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HellotestApplicationTests {

	@Autowired
	private TaskController controller;

	@Test
	public void contexLoads() throws Exception {
		assertTrue(controller != null);
	}

}
