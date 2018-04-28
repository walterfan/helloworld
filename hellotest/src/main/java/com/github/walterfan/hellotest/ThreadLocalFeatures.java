package com.github.walterfan.hellotest;

import com.google.common.util.concurrent.Uninterruptibles;

import java.util.concurrent.TimeUnit;

/**
 * Created by yafan on 20/4/2018.
 */
public class ThreadLocalFeatures {
    private static final ThreadLocal<FeatureToggle> localFeatureToggle = new ThreadLocal<FeatureToggle>();

    public static class SomethingToRun implements Runnable {

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getId() + " " + localFeatureToggle.get());

            Uninterruptibles.sleepUninterruptibly(1000, TimeUnit.MILLISECONDS);

            long thId = Thread.currentThread().getId();
            localFeatureToggle.set(new FeatureToggle("feature-" + thId, thId%2 == 0));
            System.out.println(Thread.currentThread().getId() + " " + localFeatureToggle.get());
        }
    }

    public static void main(String[] args) {
        SomethingToRun sharedRunnableInstance = new SomethingToRun();

        Thread thread1 = new Thread(sharedRunnableInstance);
        Thread thread2 = new Thread(sharedRunnableInstance);

        thread1.start();
        thread2.start();

        Uninterruptibles.sleepUninterruptibly(2000, TimeUnit.MILLISECONDS);

        System.out.println(Thread.currentThread().getId() + " " + localFeatureToggle.get());
    }
}
