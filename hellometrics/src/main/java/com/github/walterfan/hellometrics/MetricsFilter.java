package com.github.walterfan.hellometrics;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author: Walter Fan
 * @Date: 5/8/2020, Wed
 **/

@Component
@Order(1)
@Slf4j
public class MetricsFilter implements Filter {

    @Autowired
    @Qualifier("ComposeHandler")
    private MetricsHandler metricsHandler;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }


        long startTime = System.currentTimeMillis();
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error("do filter error", e);
        } finally {
            recordMetrics(request, response, startTime);
        }

    }

    private void recordMetrics(ServletRequest request, ServletResponse response, long startTime) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;

        MetricsEvent metricsEvent = MetricsEvent.builder()
                .timestmap(startTime)
                .requestUri(httpServletRequest.getRequestURI())
                .method(httpServletRequest.getMethod())
                .clientAddress(httpServletRequest.getHeader("X-Forwarded-For"))
                .build();

        String trackingId = httpServletRequest.getHeader("trackingId");
        if(StringUtils.isBlank(trackingId)) {
            trackingId = UUID.randomUUID().toString();
        }
        metricsEvent.setTrackingId(trackingId);

        MetricsHolderPerThread.setMetricsEvent(metricsEvent);
        updateMetricsEvent(metricsEvent, response, startTime);
        metricsHandler.handle(metricsEvent);
        MetricsHolderPerThread.cleanMetricsEvent();
    }

    private void updateMetricsEvent(MetricsEvent metricsEvent, ServletResponse response, long startTime) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        metricsEvent.setDurationInMs(System.currentTimeMillis() - startTime);
        metricsEvent.setStatusCode(((HttpServletResponse) response).getStatus());
    }


    @Override
    public void destroy() {
    }


}