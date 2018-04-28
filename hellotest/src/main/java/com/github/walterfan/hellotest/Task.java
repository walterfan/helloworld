package com.github.walterfan.hellotest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * Created by yafan on 19/11/2017.
 */
@Data
@Entity
public class Task {
    static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.registerModule(new JSR310Module());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private UUID id;

    private String name;

    private Duration duration;

    private Instant deadline;

    public String toJson() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "";
        }
    }


    public static Task fromJson(String json) throws IOException {
       return mapper.readValue(json, Task.class);
    }

}
