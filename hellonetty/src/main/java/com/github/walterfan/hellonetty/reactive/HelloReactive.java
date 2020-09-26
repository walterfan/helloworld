package com.github.walterfan.hellonetty.reactive;

import io.reactivex.rxjava3.core.Flowable;

/**
 * @Author: Walter Fan
 * @Date: 13/9/2020, Sun
 **/
public class HelloReactive {

    public static void hello(String... args) {
        Flowable.fromArray(args).subscribe(s -> System.out.println("Hello " + s + "!"));
    }


    public static void main(String[] args) {
        hello("alice", "bob");


    }
}
