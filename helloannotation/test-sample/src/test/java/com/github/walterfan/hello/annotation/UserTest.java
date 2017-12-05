package com.github.walterfan.hello.annotation;

import com.github.walterfan.hello.annotation.User;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;


import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

import static org.testng.Assert.assertEquals;


/**
 * Created by yafan on 3/12/2017.
 */

public class UserTest {

    @Test
    @TestCase(value = "testAge", feature = "f1779", given = "setBirthday", when="retrieveAge", then = "Age is current time minus birthday")
    public void testAge() throws ParseException {
        User user = new User();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date birthDay = formatter.parse("1980/02/10");
        user.setBirthDay(birthDay);

        Calendar birthCal = Calendar.getInstance();
        birthCal.setTime(birthDay);

        int diffYear = Calendar.getInstance().get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);
        System.out.println("diffYear: "+ diffYear);
        assertEquals(user.age(), diffYear);
    }

    @Test
    @TestCase(value = "testName", feature = "f1779", given = "setName", when="retrieveName", then = "name is same")
    public void testName() throws ParseException {
        String name = "Walter";
        User user = new User();
        user.setName(name);
        user.getName().equals(name);
    }
}
