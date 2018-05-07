package com.github.walterfan;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * Created by yafan on 28/4/2018.
 */
@Slf4j
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker handshaker;

    private String wsUrl = "ws://localhost: 10050";

     @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof FullHttpRequest) {
            httpMessageRead(ctx, (FullHttpRequest)msg);
        } else if(msg instanceof WebSocketFrame) {
            webSocketMessageRead(ctx, (WebSocketFrame)msg);
        } else {
            log.error("Got unkown {}", msg);
        }

    }

    public void httpMessageRead(ChannelHandlerContext ctx, FullHttpRequest req) {
        if(!req.getDecoderResult().isSuccess()
                || ! "websocket".equals(req.headers().get("Upgrade"))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
        }

        WebSocketServerHandshakerFactory factory =  new WebSocketServerHandshakerFactory(wsUrl, null, false);

        handshaker = factory.newHandshaker(req);

        if(handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    public void webSocketMessageRead(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if(frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame)(frame).retain());
            return;
        }

        if(frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        if(frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame reqFrame =  (TextWebSocketFrame)frame;
            log.info("received {}", reqFrame.text());
            TextWebSocketFrame respFrame = new TextWebSocketFrame(reqFrame.text() + " got at " + (new Date()).toString());
            ctx.channel().write(respFrame);
        }


    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse resp) {
        ByteBuf bytes = Unpooled.copiedBuffer(resp.getStatus().toString(), CharsetUtil.UTF_8);
        resp.content().writeBytes(bytes);
        bytes.release();
        HttpHeaders.setContentLength(resp, resp.content().readableBytes());
        
        ChannelFuture future = ctx.channel().writeAndFlush(resp);
        
        if(!HttpHeaders.isKeepAlive(req) || resp.getStatus().code() != 200) {
        	future.addListener(ChannelFutureListener.CLOSE);
        	
        }
    }
}
