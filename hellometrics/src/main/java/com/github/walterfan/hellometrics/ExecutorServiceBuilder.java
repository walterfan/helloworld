package com.github.walterfan.hellometrics;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by yafan on 4/1/2018.
 */
@Slf4j
public class ExecutorServiceBuilder {
    private int corePoolSize;
    private int maximumPoolSize;
    private Duration keepAliveTime;

    private int queueSize;

    private String uniquePrefix;
    private RejectedExecutionHandler handler;

    private int minThreads;
    private int maxThreads;

    private String prefix;

    public ThreadPoolExecutor getThreadPoolExecutor(BlockingQueue<Runnable> workQueue) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(minThreads, maxThreads, keepAliveTime.getSeconds(), TimeUnit.SECONDS, workQueue);
        executor.setThreadFactory(new ThreadFactoryBuilder().setNameFormat(prefix + "-%d").setDaemon(true).build());
        log.info("Prestart thread pool with executor: {}, min: {}, max: {}", prefix, minThreads, maxThreads);
        executor.prestartAllCoreThreads();
        return executor;
    }
}
