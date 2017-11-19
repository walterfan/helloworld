package com.github.walterfan;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

/**
 * Created by yafan on 17/11/2017.
 */
@Data
public class Task  implements Serializable {

    private String id;

    private String name;

    private int progress;

    private Instant deadline;

}
