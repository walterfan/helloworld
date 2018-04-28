package com.github.walterfan.hellotest;

import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yafan on 23/4/2018.
 */
@Slf4j
public class HelloTest {

    public static void main(String[] args) {
        String testUrl = "api.abc.net";
        if(!testUrl.startsWith("http")) {
            testUrl = "http://" + testUrl;
        }
        URL aURL = null;
        try {
            aURL = new URL(testUrl);
            log.info("aUrl host: " + aURL.getHost());
        } catch (MalformedURLException e) {
            log.error("GIVR URL is wrong" + testUrl, e);
        }
    }
}
