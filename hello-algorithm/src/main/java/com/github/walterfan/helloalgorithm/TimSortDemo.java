package com.github.walterfan.helloalgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;

public class TimSortDemo {

    public static void sortMessageError(List<String> list) {
        System.out.println("this is error comparator");
        Collections.sort(list, new Comparator<String>() {
            public int compare(String arg1, String arg2) {

                if (StringUtils.isBlank(arg1) || StringUtils.isBlank(arg2)) {
                    return 0;
                }

                return arg1.compareTo(arg2);
            }
        });

    }

    public static void sortMessageCorrect(List<String> list) {
        System.out.println("this is correct comparator");
        Collections.sort(list, new Comparator<String>() {
            public int compare(String arg1, String arg2) {

                if(StringUtils.isBlank(arg1) ) {
                    if(StringUtils.isBlank(arg2)) {
                        return 0;
                    }else {
                        return -1;
                    }

                }else if(StringUtils.isBlank(arg2)){
                    return 1;
                }

                return arg1.compareTo(arg2);

            }
        });

    }

    public static List<String> createList() {
        List<String> list = new ArrayList<String>();
        Random random = new Random();
        for (int i = 10000; i > 0; i--) {
            if (i % 5000 != 0) {
                list.add(random.nextInt(1000) + "");
            } else {
                list.add("");
            }

        }
        return list;
    }

    public static void main(String[] args) throws InterruptedException {

        sortMessageCorrect(createList());
        sortMessageError(createList());
    }

}


