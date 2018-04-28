package com.github.walterfan;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.google.common.util.concurrent.Uninterruptibles;

import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App 
{
    private final MetricRegistry metrics = new MetricRegistry();
    private final Meter requests = metrics.meter("requests");

    public void handleRequest() {
        requests.mark();
        // etc
    }

    public void report() {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(1, TimeUnit.SECONDS);
    }


    public void teatMeter() {
        for(int i=0;i<10;i++) {
            handleRequest();
            Uninterruptibles.sleepUninterruptibly(500, TimeUnit.MILLISECONDS);
        }
        report();
    }
    public static void main( String[] args )
    {

        System.out.println( "--- Application Metrics --- " );

        new App().teatMeter();
    }
}
