package com.github.walterfan.hellometrics;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

/**
 * @Author: Walter Fan
 * @Date: 6/8/2020, Thu
 **/
@Data
public class TomatoDTO {
    private UUID potatoId;
    private UUID tomatoId;
    private String name;
    private String content;
    private String tags;
    private Date startTime;
    private Date endTime;
}
