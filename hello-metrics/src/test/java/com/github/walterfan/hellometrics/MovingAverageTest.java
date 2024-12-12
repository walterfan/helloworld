package com.github.walterfan.hellometrics;

import com.codahale.metrics.EWMA;
import com.codahale.metrics.ExponentialMovingAverages;
import com.google.common.util.concurrent.Uninterruptibles;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.exp;
import static junit.framework.TestCase.assertTrue;

/**
 * @Author: Walter Fan
 * @Date: 10/4/2020, Fri
 **/

@Slf4j
public class MovingAverageTest {

    private MovingAverage movingAverage;

    private EWMA ewma;

    @Test
    public void testMovingAverage() {
        movingAverage = new MovingAverage(10, TimeUnit.MILLISECONDS);
        for(int i = 0; i< 100; i++) {
            movingAverage.update(i);
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.MILLISECONDS);
        }
        double avg = movingAverage.get();
        log.info("avg={}", avg);
        assertTrue(Double.compare(avg, 90) > 0);
    }

    @Test
    public void testExponentialMovingAverages() {
        double alpha = 0.9;
        log.info("alpha={}", alpha);
        ewma = new EWMA(1-alpha, 1, TimeUnit.MILLISECONDS);
        long[] array = {60, 50, 97, 98, 99};
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for(long num: array) {
            stats.addValue(num);
            ewma.update(num);
            ewma.tick();

            log.info("ewma avg={}, math avg = {}", ewma.getRate(TimeUnit.MILLISECONDS), stats.getMean());
        }

        assertTrue(Double.compare(ewma.getRate(TimeUnit.MILLISECONDS), 60) > 0);
    }

    @Test
    public void testTrendCalculation() {
        double[][] dataPoints =  {
                {Instant.parse("2020-04-10T07:10:40.00Z").toEpochMilli(), 19000},
                {Instant.parse("2020-04-10T07:10:30.00Z").toEpochMilli(), 18000},
                {Instant.parse("2020-04-10T07:10:20.00Z").toEpochMilli(), 15000},
                {Instant.parse("2020-04-10T07:10:10.00Z").toEpochMilli(), 3000},
                {Instant.parse("2020-04-10T07:10:00.00Z").toEpochMilli(), 2000}
        };

        SimpleRegression model = new SimpleRegression();
        model.addData(dataPoints);
        // querying for model parameters
        log.info("slope :" + model.getSlope());
        log.info("intercept : " + model.getIntercept());

        // trying to run model for unknown data
        log.info("prediction: " + model.predict(Instant.parse("2020-04-10T07:10:50.00Z").toEpochMilli()));
     }

    @Test
    public void testTrendCalculation2() {
        double[][] dataPoints =  {
                {Instant.parse("2020-04-10T07:10:40.00Z").toEpochMilli(), 9000},
                {Instant.parse("2020-04-10T07:10:30.00Z").toEpochMilli(), 10000},
                {Instant.parse("2020-04-10T07:10:20.00Z").toEpochMilli(), 18000},
                {Instant.parse("2020-04-10T07:10:10.00Z").toEpochMilli(), 20000},
                {Instant.parse("2020-04-10T07:10:00.00Z").toEpochMilli(), 22000}
        };

        SimpleRegression model = new SimpleRegression();
        model.addData(dataPoints);
        // querying for model parameters
        log.info("slope :" + model.getSlope());
        log.info("intercept : " + model.getIntercept());

        // trying to run model for unknown data
        log.info("prediction: " + model.predict(Instant.parse("2020-04-10T07:10:50.00Z").toEpochMilli()));
    }
}
