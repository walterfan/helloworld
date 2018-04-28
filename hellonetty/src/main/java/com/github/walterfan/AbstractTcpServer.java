package com.github.walterfan;

import java.nio.channels.SocketChannel;
import java.util.LinkedHashMap;
import java.util.SortedMap;
import java.util.TreeMap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by yafan on 27/4/2018.
 */
@Slf4j
public class AbstractTcpServer {

    private static final int READ_TIMEOUT_IN_SECONDS = 30;
    private static final int WRITE_TIMEOUT_IN_SECONDS = 30;
    private static final int WRITE_BUFFER_HIGH_WATER_MARK = 32 * 1024;
    private static final int WRITE_BUFFER_LOW_WATER_MARK = 4 * 1024;

    private SortedMap<String, ChannelHandler> handlers = new TreeMap<>();

    private String host;
    private int port;

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();


    public AbstractTcpServer(String host,int port) {
        this.host = host;
        this.port = port;
    }


    public void addChannelHandler(String name, ChannelHandler handler) {
        this.handlers.put(name, handler);
    }

    public void start(){

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
            bootstrap.childOption(ChannelOption.SO_LINGER, 0);
            bootstrap.childOption(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, WRITE_BUFFER_HIGH_WATER_MARK);
            bootstrap.childOption(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, WRITE_BUFFER_LOW_WATER_MARK);

            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);

            bootstrap.childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    handlers.entrySet().stream().map( entry -> channel.pipeline().addLast(entry.getKey(), entry.getValue()));
                }

            });

            bootstrap.bind(host, port).sync();

            log.info("started on {}" , port);

        } catch(Exception e){
            log.error("start failed" + port,e);
            throw new RuntimeException("tcp server", e);
        }
    }

    public void stop() {
        try{
            log.info("[tcp][server]close");
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }catch(Exception e){
            log.warn("stop  failed ",e);
        }

    }
}