package com.github.walterfan.hellonetty.reactor;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

@Slf4j
public class Handler implements Runnable {

    private final SocketChannel channel;
    private final SelectionKey selectionKey;

    private static final int READ_BUF_SIZE = 1024;
    private static final int WRiTE_BUF_SIZE = 1024;

    private ByteBuffer readBuf = ByteBuffer.allocate(READ_BUF_SIZE);
    private ByteBuffer writeBuf = ByteBuffer.allocate(WRiTE_BUF_SIZE);

    public Handler(Selector selector, SocketChannel sc) throws IOException {
        channel = sc;
        channel.configureBlocking(false);

        // Register the socket channel with interest-set set to READ operation
        selectionKey = channel.register(selector, SelectionKey.OP_READ);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    public void run() {
        try {
            if (selectionKey.isReadable())
                read();
            else if (selectionKey.isWritable())
                write();
        } catch (IOException ex) {
            log.error("read or write socket error", ex);
        }
    }

    // Process data by echoing input to output
    synchronized void process() {
        byte[] bytes;

        readBuf.flip();
        bytes = new byte[readBuf.remaining()];
        readBuf.get(bytes, 0, bytes.length);
        log.info("process(): " + new String(bytes, Charset.forName("utf-8")));

        writeBuf = ByteBuffer.wrap(bytes);

        // Set the key's interest to WRITE operation
        selectionKey.interestOps(SelectionKey.OP_WRITE);
        selectionKey.selector().wakeup();
    }

    synchronized void read() throws IOException {
        int numBytes;

        try {
            numBytes = channel.read(readBuf);
            log.info("read(): #bytes read into 'readBuf' buffer = " + numBytes);

            if (numBytes == -1) {
                selectionKey.cancel();
                channel.close();
                log.warn("read(): client connection might have been dropped!");
            } else {
                EchoServer.getWorkExecutor().execute(new Runnable() {
                    public void run() {
                        process();
                    }
                });
            }
        } catch (IOException ex) {
            log.error("read or write socket error", ex);
            return;
        }
    }

    void write() throws IOException {
        int numBytes = 0;

        try {
            numBytes = channel.write(writeBuf);
            log.info("write(): #bytes read from 'writeBuf' buffer = " + numBytes);

            if (numBytes > 0) {
                readBuf.clear();
                writeBuf.clear();

                // Set the key's interest-set back to READ operation
                selectionKey.interestOps(SelectionKey.OP_READ);
                selectionKey.selector().wakeup();
            }
        } catch (IOException ex) {
            log.error("write socket error", ex);
        }
    }
}
