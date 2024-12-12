package com.github.walterfan.hellonetty.reactor;


import com.github.walterfan.hellonetty.IServer;
import com.google.common.util.concurrent.Uninterruptibles;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
public class EchoServer implements IServer {


    class ShutdownHandler implements SignalHandler {

        @Override
        public void handle(Signal signal) {
            log.info("shutdown: {}", signal);
            stop();
            System.exit(0);
        }
    }

    private static final int WORKER_POOL_SIZE = 10;

    private static ExecutorService bossExecutor;
    private static ExecutorService workExecutor;
    private volatile boolean isStarted;
    private Reactor reactor;
    private SignalHandler shutdownHandler;

    public static ExecutorService getWorkExecutor() {
        return workExecutor;
    }

    @Override
    public void init() throws IOException {
        log.info("init...");
        isStarted = false;
        bossExecutor = Executors.newSingleThreadExecutor();
        workExecutor = Executors.newFixedThreadPool(WORKER_POOL_SIZE);

        shutdownHandler = new ShutdownHandler();

        reactor = new Reactor(9090);

        registerStopSignal();
    }

    @Override
    public boolean isStarted() {
        return isStarted;
    }

    @Override
    public void start() {
        log.info("start...");
        if(isStarted) {
            return;
        }
        bossExecutor.execute(reactor);
        isStarted = true;
    }

    @Override
    public void stop() {
        log.info("stop...");
        reactor.stop();
        workExecutor.shutdownNow();
        bossExecutor.shutdownNow();
        isStarted = false;
        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
        log.info("stopped");
    }


    public void registerStopSignal() {

        String osName = System.getProperties().getProperty("os.name");
        log.info("os={}", osName);
        Signal sigInt = new Signal("INT");
        Signal sigTerm = new Signal("TERM");
        Signal.handle(sigInt, this.shutdownHandler);
        Signal.handle(sigTerm, this.shutdownHandler);
    }

    public static void main(String[] args) throws IOException {

        EchoServer echoServer = new EchoServer();
        echoServer.init();
        echoServer.start();
        log.info("started? {}", echoServer.isStarted());
        Uninterruptibles.sleepUninterruptibly(Long.MAX_VALUE, TimeUnit.MINUTES);
    }
}
