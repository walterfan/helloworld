package com.github.walterfan.helloconcurrency;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.InstrumentedExecutorService;
import com.codahale.metrics.Meter;

import com.codahale.metrics.MetricRegistry;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import java.util.function.Supplier;

//import static com.codahale.metrics.MetricRegistry.name;

/**
 * @Author: Walter Fan
 *
 **/
@Slf4j
public class ThreadPoolUtil {

    public static class DiscardAndLogPolicy implements RejectedExecutionHandler {
        final MetricRegistry metricRegistry;
        final Meter rejectedMeter;
        final Counter rejectedCounter;

        public DiscardAndLogPolicy(String threadPrefix, MetricRegistry metricRegistry) {
            this.metricRegistry = metricRegistry;
            this.rejectedMeter =  metricRegistry.meter(threadPrefix + ".rejected-meter");
            this.rejectedCounter = metricRegistry.counter(threadPrefix + ".rejected-counter");
        }


        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            rejectedMeter.mark();
            rejectedCounter.inc();
            if (!e.isShutdown()) {
                log.warn("reject task and run {} directly", r);
                r.run();

            }
        }
    }


    public static ThreadPoolExecutor createThreadExecutor(ThreadPoolParam threadPoolParam) {
        MetricRegistry metricRegistry = threadPoolParam.getMetricRegistry();

        metricRegistry.register(threadPoolParam.getThreadPrefix() + ".min", createIntGauge(() -> threadPoolParam.getMinPoolSize()));
        metricRegistry.register(threadPoolParam.getThreadPrefix() + ".max", createIntGauge(() -> threadPoolParam.getMaxPoolSize()));
        metricRegistry.register(threadPoolParam.getThreadPrefix() + ".queue_limitation", createIntGauge(() -> threadPoolParam.getQueueSize()));


        ThreadPoolExecutor executor = new ThreadPoolExecutor(threadPoolParam.getMinPoolSize(),
                threadPoolParam.getMaxPoolSize(),
                threadPoolParam.getKeepAliveTime().toMillis(),
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(threadPoolParam.getQueueSize()),
                createThreadFactory(threadPoolParam),
                createRejectedExecutionHandler(threadPoolParam));

        metricRegistry.register(threadPoolParam.getThreadPrefix() + ".pool_size", createIntGauge(() -> executor.getPoolSize()));
        metricRegistry.register(threadPoolParam.getThreadPrefix() + ".queue_size", createIntGauge(() -> executor.getQueue().size()));
        return executor;
    }

    public static ExecutorService createExecutorService(ThreadPoolParam threadPoolParam) {
        ThreadPoolExecutor executor = createThreadExecutor(threadPoolParam);

        return new InstrumentedExecutorService(executor,
                threadPoolParam.getMetricRegistry(),
                threadPoolParam.getThreadPrefix());
    }

    private static Gauge<Integer> createIntGauge(Supplier<Integer> suppier) {
        return () -> suppier.get();
    }

    public static ThreadFactory createThreadFactory(ThreadPoolParam threadPoolParam) {
        return new ThreadFactoryBuilder()
                .setDaemon(threadPoolParam.isDaemon())
                .setNameFormat(threadPoolParam.getThreadPrefix() + "-%d")
                .build();
    }

    public static RejectedExecutionHandler createRejectedExecutionHandler(ThreadPoolParam threadPoolParam) {
        return new DiscardAndLogPolicy(threadPoolParam.getThreadPrefix(), threadPoolParam.getMetricRegistry());
    }
}
