package com.github.walterfan.helloalgorithm;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.Uninterruptibles;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class SortAlgorithmDemo {
	public static final int SORT_TIMEOUT_SEC = 30;
	private static AtomicInteger finishCounter = new AtomicInteger(0);

	private AtomicInteger taskNumber = new AtomicInteger(0);

	private ExecutorService executorService =Executors.newSingleThreadExecutor();

	public Callable<Long> createTask(int cardSuiteCount, SortCardTask.SortMethod method) {
		List<Poker.Card> cards = Poker.createCardList(cardSuiteCount);
		return new SortCardTask(cards, method, taskNumber.incrementAndGet(), finishCounter);

	}

	public List<Future<Long>> exeucteTasks(List<Callable<Long>> tasks, Duration waitTime)  {
		try {
			return this.executorService.invokeAll(tasks, waitTime.toMillis(), TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			log.warn("invokeAll interrupt", e);
			return Collections.emptyList();
		}
	}

	private void runTasks(int cardSuiteCount, SortCardTask.SortMethod sortMethod) {

		List<Callable<Long>> tasks = new ArrayList<>();

		tasks.add(this.createTask(cardSuiteCount, sortMethod));

		List<Future<Long>> results = this.exeucteTasks(tasks, Duration.ofSeconds(SORT_TIMEOUT_SEC));

		results.stream().filter(x -> !x.isDone()).forEach(x -> log.info("{} is not done", x));
	}

	private void waitAndstop(int seconds) {
		Uninterruptibles.sleepUninterruptibly(seconds, TimeUnit.SECONDS);
		this.executorService.shutdownNow();
	}

	public static void main(String[] args) {

		SortAlgorithmDemo demo = new SortAlgorithmDemo();

		int cardSuiteCount = 1000;

		demo.runTasks(cardSuiteCount, SortCardTask.SortMethod.BUBBLE_SORT);

		demo.runTasks(cardSuiteCount, SortCardTask.SortMethod.INSERT_SORT);

		demo.runTasks(cardSuiteCount, SortCardTask.SortMethod.QUICK_SORT);

		demo.runTasks(cardSuiteCount, SortCardTask.SortMethod.MERGE_SORT);

		demo.runTasks(cardSuiteCount, SortCardTask.SortMethod.TIM_SORT);

		demo.waitAndstop(30);
	}
}
