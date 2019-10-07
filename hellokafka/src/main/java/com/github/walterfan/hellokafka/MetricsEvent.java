package com.github.walterfan.hellokafka;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Walter Fan
 * @Date: 3/10/2019, Thu
 **/
@Data
@Builder
public class MetricsEvent {
    private String metricName;
    private String featureName;
    private Instant timestamp;
    private String trackingId;
    private String componentType;
    private String componentAddress;
    private String componentVersion;
    private String poolName;
    private Map<String, Object> properties = new HashMap<>();

    public void putProperty(String key, Object value) {
        this.properties.put(key, value);
    }
}
