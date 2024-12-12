package com.github.walterfan.helloalgorithm;


import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Walter Fan
 **/
@Slf4j
public class TimSortExam {
    public static class WorkLoadComparator implements Comparator<WorkNode> {

        @Override
        public int compare(WorkNode o1, WorkNode o2) {
            double ratio1 = o1.getRatio();
            double ratio2 = o2.getRatio();

            double distance = ratio1 - ratio2;
            if(Double.compare(Math.abs(distance), 0.02) < 0) {

                int ret =  Long.compare(o1.getConnections(), o2.getConnections());
                log.info("o1={}, o2={}, distance={}, ret={}" , ratio1, ratio2, distance, ret);
                return ret;
            }

            return Double.compare(ratio1, ratio2);
        }
    }
   public static void main(String[] args) {
       WorkNode node1 = new WorkNode(100, 20, 30);
       WorkNode node2 = new WorkNode(100, 22, 20);
       WorkNode node3 = new WorkNode(100, 23, 10);
       WorkNode node4 = new WorkNode(100, 25, 50);

       Comparator<WorkNode> comparator = new  WorkLoadComparator();

       int ret = comparator.compare(node1, node2); log.info("node1 {} node2 ", ret > 0? ">":"<=");
       ret = comparator.compare(node2, node3); log.info("node2 {} node3",  ret > 0? ">":"<=");
       ret = comparator.compare(node1, node3); log.info("node1 {} node3",  ret > 0? ">":"<=");

       List<WorkNode> aList = new LinkedList<>();
       for(int i = 0; i < 1; ++i) {
           aList.addAll(Arrays.asList(node1, node2, node3, node4));
       }
       List<WorkNode> sortedList = aList.stream().sorted(comparator).collect(Collectors.toList());
       log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
       sortedList.stream().forEach(x -> log.info("{}", x));
   }




}
