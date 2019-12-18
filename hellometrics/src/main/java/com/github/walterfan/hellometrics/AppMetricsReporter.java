package com.github.walterfan.hellometrics;

import com.codahale.metrics.*;
import com.google.common.util.concurrent.Uninterruptibles;
import lombok.extern.slf4j.Slf4j;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
@Slf4j
public class AppMetricsReporter
{
    private final MetricRegistry metricRegistry = new MetricRegistry();
    private final Meter requests = metricRegistry.meter("requests");

    public void handleRequest() {
        requests.mark();
        // etc
    }

    public void report() {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(3, TimeUnit.SECONDS);

        final JmxReporter jmxReporter = JmxReporter.forRegistry(metricRegistry).build();
        jmxReporter.start();
    }

    public void registerGauge(){
        metricRegistry.register(MetricRegistry.name(SessionStore.class, "cache-evictions"), new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return cache.getEvictionsCount();
            }
        });
    }

    public void testMeter() {
        for(int i=0;i<10;i++) {
            handleRequest();
            Uninterruptibles.sleepUninterruptibly(500, TimeUnit.MILLISECONDS);
        }
    }


    private static void registerServerStatusMonitor(String[] args) {
        try {
            String programName = (args.length == 0) ? "AppMetricsReporter" : args[0];

            // Initialize the object
            ServerStatus systemStatus = new ServerStatus(programName);

            // Register the object in the MBeanServer
            MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
            ObjectName objectName = new ObjectName("com.github.walterfan.hellometrics:name=AppMetricsReporter");
            platformMBeanServer.registerMBean(systemStatus, objectName);

        } catch (Exception e) {
            log.error("registerMBean", e);
        }
    }


    public static void main( String[] args ) {

        System.out.println( "--- Application Metrics --- " );
        registerServerStatusMonitor(args);


        //new AppMetricsReporter().testMeter();
        Uninterruptibles.sleepUninterruptibly(500, TimeUnit.SECONDS);
    }
}
