package com.github.walterfan.hellonetty.reactor;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Reactor implements Runnable {
    private final Selector selector;
    private final ServerSocketChannel serverChannel;
    private volatile boolean isStopRequested = false;

    public Reactor(int port) throws IOException {
        selector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        serverChannel.socket().bind(new InetSocketAddress(port));
        serverChannel.configureBlocking(false);

        // Register the server socket channel with interest-set set to ACCEPT operation
        SelectionKey sk = serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(new Acceptor());
    }

    public void run() {
        try {
            while (!isStopRequested) {

                selector.select();
                Iterator it = selector.selectedKeys().iterator();

                while (it.hasNext()) {
                    SelectionKey sk = (SelectionKey) it.next();
                    it.remove();
                    Runnable r = (Runnable) sk.attachment();
                    if (r != null)
                        r.run();
                }
            }
        } catch (IOException ex) {
            log.error("process socket error", ex);
        }
        log.info("stop running");
    }

    public void stop() {
        this.isStopRequested = true;
    }

    class Acceptor implements Runnable {
        public void run() {
            try {
                SocketChannel channel = serverChannel.accept();
                if (channel != null)
                    new Handler(selector, channel);
            } catch (IOException ex) {
                log.error("accept socket error", ex);
            }
        }
    }

}
