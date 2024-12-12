package com.github.walterfan.hellometrics;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * @Author: Walter Fan
 * @Date: 5/8/2020, Wed
 **/
@Data
@Builder
public class MetricsEvent {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private long timestmap;
    private String requestUri;
    private String method;
    private String trackingId;
    private int statusCode;
    private long durationInMs;
    private String clientAddress;
    private int contentLength;

    public  String toJson() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to serialize object to json", e);
        }
    }
}
