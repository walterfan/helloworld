package com.github.walterfan.hello;

import lombok.Data;

import java.time.Instant;
import java.util.List;

/**
 * Created by yafan on 4/4/2018.
 */
@Data
public class Potato {

    private String name;

    private int priority;

    private String description;

    private List<String> tags;

    private Instant deadline;
}
