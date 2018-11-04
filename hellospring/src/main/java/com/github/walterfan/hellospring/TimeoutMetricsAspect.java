package com.github.walterfan.hellospring;


import com.cisco.wx2.client.ClientException;
import com.cisco.wx2.client.TimeoutPolicy;
import com.codahale.metrics.MetricRegistry;
import java.net.SocketTimeoutException;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

@Aspect
@Slf4j
public class TimeoutMetricsAspect {


    @Inject
    private MetricRegistry metricRegistry;
    @Inject
    TimedPolicyMetric timeoutPolicy;
    private final static String EXCEEDED_TAG = ".exceeded";

    private static final long NANOSECOND_TO_MILLISECOND = 1000000;

    /**
     * Add a proxy to object to allow performance metric aspects to run. This is optional; any Spring beans will
     * be automatically proxied.
     *
     * @param o
     * @return
     */
    public <T> T proxy(T o) {
        final AspectJProxyFactory factory = new AspectJProxyFactory(o);
        factory.setProxyTargetClass(true);
        factory.addAspect(this);
        return factory.getProxy();
    }

    /**
     * This around advice adds timing to any method annotated with the TimedPolicy annotation.
     * It binds the annotation to the parameter timedPolicyAnnotation so that the values are available at runtime.
     * The result of the timing is sent through statsD using the SimpleMetricsClient.
     *
     * @param pjp             the join point for this advice
     * @param timedPolicyAnnotation the TimedPolicyMetric annotation as declared on the method
     * @return
     * @throws Throwable
     */
    @Around("@annotation( timedPolicyAnnotation ) ")
    public Object measureTimeRequest(final ProceedingJoinPoint pjp, TimedPolicyMetric timedPolicyAnnotation) throws Throwable {
        final String timingName = timedPolicyAnnotation.policyName();
        final long start = System.nanoTime();
        //get policy timeout value from timeout policy defined in properties , if not present use default value
        final long policyTimeout = timeoutPolicy != null && timeoutPolicy.getPolicyValue(timingName).isPresent() ? timeoutPolicy.getPolicyValue(timingName).get().longValue() : timedPolicyAnnotation.defaultTimeout();

        try {
            final Object retVal = pjp.proceed();
            try {
                final long differenceMs = (System.nanoTime() - start)/NANOSECOND_TO_MILLISECOND;
                if (metricRegistry != null) {
                    metricRegistry.histogram(timingName).update(differenceMs);
                }
                if (policyTimeout != 0 && differenceMs >= policyTimeout) {
                    log.info("timing for locus policy {} expected: {}ms actual: {}ms", timingName, policyTimeout, differenceMs);
                    if (metricRegistry != null) {
                        metricRegistry.counter(timingName + EXCEEDED_TAG).inc();
                    }
                }
                log.debug("Sending statsD timing for {} with time = {}", timingName, differenceMs);

            } catch (Exception ex) {
                log.error("Cannot measure api timing.... :" + ex.getMessage());
            }
            return retVal;
        }
        catch(Exception e){
            if(e.getCause() instanceof  SocketTimeoutException) {
                final long differenceMs = (System.nanoTime() - start)/NANOSECOND_TO_MILLISECOND;
                log.info("timing for locus policy {} expected: {}ms actual: {}ms", timingName, policyTimeout, differenceMs);
                if (metricRegistry != null) {
                    metricRegistry.counter(timingName + EXCEEDED_TAG).inc();
                }
            }
            throw e;
        }

    }


}
