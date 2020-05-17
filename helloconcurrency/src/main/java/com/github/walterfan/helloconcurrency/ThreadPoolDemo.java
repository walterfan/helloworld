package com.github.walterfan.helloconcurrency;


import com.codahale.metrics.CsvReporter;
import com.codahale.metrics.Slf4jReporter;

import com.codahale.metrics.MetricRegistry;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.Uninterruptibles;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import java.util.concurrent.ExecutorService;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Walter Fan
 **/
@Slf4j
public class ThreadPoolDemo {
    private static AtomicInteger finishCounter = new AtomicInteger(0);

    private AtomicInteger taskNumber = new AtomicInteger(0);

    private ExecutorService executorService;

    public ThreadPoolDemo(ThreadPoolParam threadPoolParam) {
        executorService = ThreadPoolUtil.createExecutorService(threadPoolParam);

    }

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

        final CsvReporter csvReporter = CsvReporter.forRegistry(metricRegistry)
                .formatFor(Locale.US)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build(new File("./"));
        csvReporter.start(100, TimeUnit.MILLISECONDS);

/*        final Slf4jReporter logReporter = Slf4jReporter.forRegistry(metricRegistry)
                .outputTo(log)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();

        logReporter.start(1, TimeUnit.MINUTES);*/

        ThreadPoolParam threadPoolParam = ThreadPoolParam.builder()
                .minPoolSize(1)
                .maxPoolSize(4)
                .daemon(true)
                .keepAliveTime(Duration.ofSeconds(1))
                .queueSize(5)
                .threadPrefix("cards-thread-pool")
                .metricRegistry(metricRegistry)
                .build();

        ThreadPoolDemo demo = new ThreadPoolDemo(threadPoolParam);
        List<Callable<Long>> tasks = new ArrayList<>();
        for(int i=1; i<=10; i++) {
            tasks.add(demo.createTask(i*i*10, SortCardTask.SortMethod.BUBBLE_SORT));
            tasks.add(demo.createTask(i*i*10, SortCardTask.SortMethod.INSERT_SORT));
            tasks.add(demo.createTask(i*i*10, SortCardTask.SortMethod.TIM_SORT));
        }


        List<Future<Long>> results = demo.exeucteTasks(tasks, Duration.ofMinutes(1));

        //logReporter.report();
        stopwatch.stop();
        log.info("--- end finish {}, spent {} ---", finishCounter.get(), stopwatch);
        results.stream().filter(x -> !x.isDone()).forEach(x -> log.info("{} is not done", x));
        Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(3));

    }
}
