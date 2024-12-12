package com.github.walterfan.hellometrics;

import org.slf4j.MDC;

/**
 * @Author: Walter Fan
 * @Date: 6/8/2020, Thu
 **/
public class MetricsHolderPerThread {

    private static final ThreadLocal<MetricsEvent> METRICS_THREAD_LOCAL = new ThreadLocal<>();

    private MetricsHolderPerThread() {
    }

    public static MetricsEvent getMetricsEvent() {
        return METRICS_THREAD_LOCAL.get();
    }

    public static void setMetricsEvent(MetricsEvent event) {
        METRICS_THREAD_LOCAL.set(event);
    }

    public static void cleanMetricsEvent() {
        METRICS_THREAD_LOCAL.remove();
    }
}
