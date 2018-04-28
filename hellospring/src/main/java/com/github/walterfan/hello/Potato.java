package com.github.walterfan.hello;

import lombok.Data;

import java.time.Instant;

/**
 * Created by yafan on 4/4/2018.
 */
@Data
public class Potato {
    private String name;

    private String description;

    private Instant deadline;
}
