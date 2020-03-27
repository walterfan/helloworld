package com.github.walterfan.helloconcurrency;


import com.codahale.metrics.Slf4jReporter;

import com.codahale.metrics.MetricRegistry;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import java.util.concurrent.ExecutorService;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Walter Fan
 * @Date: 23/3/2020, Mon
 **/
@Slf4j
public class ThreadPoolDemo {

    private ExecutorService executorService;

    public ThreadPoolDemo(ThreadPoolParam threadPoolParam) {
        executorService = ThreadPoolUtil.createExecutorService(threadPoolParam);

    }

    public Callable<Long> createTask(int cardSuiteCount, SortCardTask.SortMethod method) {
        List<Poker.Card> cards = Poker.createCardList(cardSuiteCount);
        return new SortCardTask(cards, method);

    }

    public List<Future<Long>> exeucteTasks(List<Callable<Long>> tasks)  {
        try {
            return this.executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            log.warn("invokeAll interrupt", e);
            return Collections.emptyList();
        }
    }

    public void waitUntil(long ms) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(ms, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        log.info("--- start ---");
        MetricRegistry metricRegistry = new MetricRegistry();

        final Slf4jReporter logReporter = Slf4jReporter.forRegistry(metricRegistry)
                .outputTo(log)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();

        logReporter.start(1, TimeUnit.MINUTES);

        ThreadPoolParam threadPoolParam = ThreadPoolParam.builder()
                .minPoolSize(4)
                .maxPoolSize(8)
                .daemon(true)
                .keepAliveTime(Duration.ofSeconds(1))
                .queueSize(2)
                .threadPrefix("cards-thread-pool")
                .metricRegistry(metricRegistry)
                .build();

        ThreadPoolDemo demo = new ThreadPoolDemo(threadPoolParam);
        List<Callable<Long>> tasks = new ArrayList<>();
        tasks.add(demo.createTask(2, SortCardTask.SortMethod.BUBBLE_SORT));
        tasks.add(demo.createTask(2, SortCardTask.SortMethod.INSERT_SORT));
        tasks.add(demo.createTask(2, SortCardTask.SortMethod.TIM_SORT));

        tasks.add(demo.createTask(4, SortCardTask.SortMethod.BUBBLE_SORT));
        tasks.add(demo.createTask(4, SortCardTask.SortMethod.INSERT_SORT));
        tasks.add(demo.createTask(4, SortCardTask.SortMethod.TIM_SORT));

        tasks.add(demo.createTask(8, SortCardTask.SortMethod.BUBBLE_SORT));
        tasks.add(demo.createTask(8, SortCardTask.SortMethod.INSERT_SORT));
        tasks.add(demo.createTask(8, SortCardTask.SortMethod.TIM_SORT));

        List<Future<Long>> results = demo.exeucteTasks(tasks);

        logReporter.report();
        stopwatch.stop();
        log.info("--- end {} ---", stopwatch);


    }
}
