package com.github.walterfan.hellonetty.netty;

import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created by yafan on 27/4/2018.
 */
public class WebSocketServer extends AbstractServer {

    public WebSocketServer(String host, int port) {
        super(host, port);
    }

    public void init() {
        this.addChannelHandler("http-codec", new HttpServerCodec());
        this.addChannelHandler("http-aggregator", new HttpObjectAggregator(65536));
        this.addChannelHandler("http-chunked", new ChunkedWriteHandler());
        this.addChannelHandler("websocket-handler", new WebSocketServerHandler());
    }
}
