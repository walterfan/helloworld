package com.github.walterfan.helloprotocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import lombok.extern.slf4j.Slf4j;

import static io.netty.buffer.Unpooled.copiedBuffer;

@Slf4j
public class HttpServerHandler extends ChannelInboundHandlerAdapter {

    WebSocketServerHandshaker handshaker;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        if (msg instanceof DefaultHttpRequest) {

            HttpRequest httpRequest = (HttpRequest) msg;

            log.info("Http Request Received");

            HttpHeaders headers = httpRequest.headers();
            log.info("Connection : " + headers.get("Connection"));
            log.info("Upgrade : " + headers.get("Upgrade"));

            if ("Upgrade".equalsIgnoreCase(headers.get(HttpHeaderNames.CONNECTION)) &&
                    "WebSocket".equalsIgnoreCase(headers.get(HttpHeaderNames.UPGRADE))) {

                //Adding new handler to the existing pipeline to handle WebSocket Messages
                ctx.pipeline().replace(this, "websocketHandler", new WebSocketHandler());

                log.info("WebSocketHandler added to the pipeline");
                log.info("Opened Channel : " + ctx.channel());
                log.info("Handshaking....");
                //Do the Handshake to upgrade connection from HTTP to WebSocket protocol
                handleHandshake(ctx, httpRequest);
                log.info("Handshake is done");
            } else {

                    final DefaultHttpRequest request = (DefaultHttpRequest) msg;
                    final String responseMessage = "haha, pls add HTTP header: \nConnection: Upgrade\nUpgrade: WebSocket\n";
                    FullHttpResponse response = new DefaultFullHttpResponse(
                            HttpVersion.HTTP_1_1,
                            HttpResponseStatus.OK,
                            copiedBuffer(responseMessage.getBytes())
                    );

                    if (HttpHeaders.isKeepAlive(request)) {
                        response.headers().set(
                                HttpHeaders.Names.CONNECTION,
                                HttpHeaders.Values.KEEP_ALIVE
                        );
                    }
                    response.headers().set(HttpHeaders.Names.CONTENT_TYPE,
                            "text/plain");
                    response.headers().set(HttpHeaders.Names.CONTENT_LENGTH,
                            responseMessage.length());

                    ctx.writeAndFlush(response);

            }
        } else {
            log.info("Incoming request is unknown {}", msg.getClass());
        }
    }

    /* Do the handshaking for WebSocket request */
    protected void handleHandshake(ChannelHandlerContext ctx, HttpRequest req) {
        WebSocketServerHandshakerFactory wsFactory =
                new WebSocketServerHandshakerFactory(getWebSocketURL(req), null, true);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    protected String getWebSocketURL(HttpRequest req) {
        log.info("Req URI : " + req.getUri());
        String url = "ws://" + req.headers().get("Host") + req.getUri();
        log.info("Constructed URL : " + url);
        return url;
    }
}