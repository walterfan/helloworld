package com.github.walterfan.hellometrics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Walter Fan
 * @Date: 5/8/2020, Wed
 **/
@Component("ComposeHandler")
@Slf4j
public class ComposeMetricsHandler implements MetricsHandler {

    @Autowired
    @Qualifier("MetricsHandler")
    private List<MetricsHandler> metricsHandlers = new ArrayList<>();

    @Override
    public void handle(MetricsEvent metricsEvent) {
        for(MetricsHandler handler: metricsHandlers) {
            handler.handle(metricsEvent);
        }
    }
}
