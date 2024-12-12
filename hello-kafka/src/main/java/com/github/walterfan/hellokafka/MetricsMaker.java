package com.github.walterfan.hellokafka;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

/**
 * @Author: Walter Fan
 * @Date: 8/9/2019, Sun
 **/
@Service
@Slf4j
public class MetricsMaker {
    public static final DateTimeFormatter isoDateTimeFormatter = ISODateTimeFormat.dateTime().withZoneUTC();
    private static ObjectMapper objectMapper = new ObjectMapper();;

    static {
        SimpleModule module = new SimpleModule("potato", new Version(1, 0, 0, null, null, null));
        module.addSerializer(new StdSerializer<Instant>(Instant.class) {
            @Override
            public void serialize(Instant date, JsonGenerator jg, SerializerProvider sp) throws IOException {
                jg.writeString(isoDateTimeFormatter.print(date.toEpochMilli()));
            }
        });

        module.addDeserializer(Instant.class, new StdScalarDeserializer<Instant>(Instant.class) {
            @Override
            public Instant deserialize(JsonParser jp, DeserializationContext dc) throws IOException {
                try {
                    if (jp.getText() == null || jp.getText().trim().isEmpty()) {
                        return null;
                    } else {
                        return Instant.parse(jp.getText());
                    }
                } catch (IllegalArgumentException e) {
                    throw dc.mappingException("Unable to parse date: " + e.getMessage());
                }
            }
        });
        objectMapper.registerModule(module);
    }

    public static void main(String[] args) throws JsonProcessingException {
        MetricsEvent metricsEvent = MetricsEvent.builder()
                .metricName("potato")
                .featureName("createPotato")
                .componentAddress("192.168.1.2")
                .componentType("potato-web")
                .componentVersion("1.0")
                .timestamp(Instant.now())
                .trackingId(UUID.randomUUID().toString())
                .properties(ImmutableMap.<String, Object>builder()
                        .put("responseCode", 200)
                        .put("responseTime", 120)
                        .build())
                .build();
        log.info(objectMapper.writeValueAsString(metricsEvent));
    }
}
