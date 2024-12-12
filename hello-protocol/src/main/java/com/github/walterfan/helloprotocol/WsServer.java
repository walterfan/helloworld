package com.github.walterfan.helloprotocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WsServer {
    private static final int PORT = 9000;
    private ChannelFuture channel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;


    public WsServer(){
         bossGroup = new NioEventLoopGroup(1);
         workerGroup = new NioEventLoopGroup();
    }


    public void startup() {

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run() { shutdown(); }
        });

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HTTPInitializer());

            this.channel = b.bind(PORT).sync();

        } catch (InterruptedException e) {
            log.error("interrupt", e);
        }
    }

    public void shutdown()
    {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();

        try {
            channel.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("interrupt", e);
        }
    }

    public static void main(String[] args) {
        new WsServer().startup();
    }
}