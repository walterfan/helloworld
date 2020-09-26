package com.github.walterfan.hellonetty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;


@Slf4j
class DiscardHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf)msg;
        log.info("Got message and discard");
        try {
            while (in.isReadable()) {
                log.info("{}", (char) in.readByte());
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}

@Slf4j
public class DiscardServer extends AbstractServer implements CommandLineRunner {

    public DiscardServer(String host, int port) {
        super(host, port);
    }

    public DiscardServer() {
        super();
    }

    @Override
    public void init() {
        log.info("init {}", this.getClass().getSimpleName());
        addChannelHandler("discardServer", new DiscardHandler());
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("run {}", this.getClass().getSimpleName());
        if(args.length > 1) {
            setHost(args[0]);
            setPort(Integer.parseInt(args[1]));
        }
        init();
        start();
    }
}
