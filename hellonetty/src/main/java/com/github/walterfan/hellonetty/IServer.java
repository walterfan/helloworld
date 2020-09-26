package com.github.walterfan.hellonetty;

/**
 * Created by yafan on 28/4/2018.
 */
public interface IServer {

    void init() throws Exception;

    boolean isStarted();

    void start() throws Exception;

    void stop();

}
