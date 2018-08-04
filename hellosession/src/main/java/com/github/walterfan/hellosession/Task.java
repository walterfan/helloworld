package com.github.walterfan.hellosession;



import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String description;
    private int priority;
    private Date deadline;
    private Date startTime;
    private Date endTime;
    private Date scheduleTime;


}