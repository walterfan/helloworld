package com.github.walterfan.hello.annotation;


import lombok.Data;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by yafan on 3/12/2017.
 */

@Data
public class User {
    private String name;
    private String email;
    private Date birthDay;


    public int age() {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());

        Calendar birth = Calendar.getInstance();
        birth.setTime(birthDay);

        return Math.abs(now.get(Calendar.YEAR) - birth.get(Calendar.YEAR));
    }

}
