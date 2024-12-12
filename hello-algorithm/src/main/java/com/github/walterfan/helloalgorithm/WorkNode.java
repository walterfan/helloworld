package com.github.walterfan.helloalgorithm;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: Walter Fan
 * @Date: 20/9/2020, Sun
 **/
@Data
@AllArgsConstructor
public class WorkNode {
    private long capacity;
    private long workload;
    private long connections;


    public double getRatio() {
        return (double)workload/capacity;
    }
    @Override
    public String toString() {
        return "WorkNode{" +
                "capacity=" + capacity +
                ", workload=" + workload +
                ", ratio=" + getRatio() +
                ", connections=" + connections +
                '}';
    }
}
