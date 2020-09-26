package com.github.walterfan.hellometrics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: Walter Fan
 * @Date: 6/8/2020, Thu
 **/
@Component("MetricsHandler")
@Slf4j
public class LogMetricsHandler implements MetricsHandler {
    @Override
    public void handle(MetricsEvent metricsEvent) {
        log.info("{}", metricsEvent.toJson());
    }
}
