package com.github.walterfan.helloconcurrency;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;


/**
 * @Author: Walter Fan
 * @Date: 26/3/2020, Thu
 **/
@Getter
@Setter
@Builder
public class ThreadPoolParam {
    private int minPoolSize;
    private int maxPoolSize;
    private Duration keepAliveTime;
    private int queueSize;
    private String threadPrefix;
    private boolean daemon;
    private MetricRegistry metricRegistry;

}
