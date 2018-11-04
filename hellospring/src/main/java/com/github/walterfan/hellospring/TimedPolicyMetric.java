package com.github.walterfan.hellospring;

public @interface TimedPolicyMetric {

    String policyName();

    long defaultTimeout() default 0;
}
