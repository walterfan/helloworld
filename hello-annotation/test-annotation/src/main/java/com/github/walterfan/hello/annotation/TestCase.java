package com.github.walterfan.hello.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yafan on 24/11/2017.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface TestCase {
    String value();
    String feature() default "";
    String scenario() default "";
    String given() default "";
    String when() default "";
    String then() default "";
    //String[] checkpoints();
}
