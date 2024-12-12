package com.github.walterfan.helloconcurrency;

import com.codahale.metrics.Clock;
import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.codahale.metrics.Timer;

import java.util.List;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Walter Fan
 * @Date: 29/3/2020, Sun
 **/
public class MetricsReporter  extends ScheduledReporter {

    public MetricsReporter(MetricRegistry registry) {
        super(registry, "influx-scheduled-reporter", MetricFilter.ALL, TimeUnit.SECONDS, TimeUnit.MILLISECONDS);
    }

    public MetricsReporter(MetricRegistry registry, MetricFilter filter) {
        super(registry, "influx-scheduled-reporter", filter, TimeUnit.SECONDS, TimeUnit.MILLISECONDS);
    }

    @Override
    public void report(SortedMap<String, Gauge> gauges,
                       SortedMap<String, Counter> counters,
                       SortedMap<String, Histogram> histograms,
                       SortedMap<String, Meter> meters,
                       SortedMap<String, Timer> timers) {

    }
}
