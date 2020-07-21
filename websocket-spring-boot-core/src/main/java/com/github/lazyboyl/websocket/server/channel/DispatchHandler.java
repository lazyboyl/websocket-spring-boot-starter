package com.github.lazyboyl.websocket.server.channel;

import com.github.lazyboyl.websocket.server.channel.entity.WebSocketRequestEntity;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * @author linzf
 * @since 2020/7/9
 * 类描述： 实现不同的handler的分发
 */
public class DispatchHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            //判断是否为websocket握手请求
            if (isWebSocketHandShake(request)) {
                ctx.fireChannelRead(new WebSocketRequestEntity(request));
            }
        } else if (msg instanceof WebSocketFrame) {
            WebSocketFrame frame = (WebSocketFrame) msg;
            ctx.fireChannelRead(new WebSocketRequestEntity(frame));
        }
    }

    /**
     * 功能描述： 判断是否为websocket握手请求
     *
     * @param request 请求对象
     * @return
     */
    private boolean isWebSocketHandShake(FullHttpRequest request) {
        //1、判断是否为get 2、判断Upgrade头是否包含websocket 3、Connection头是否包含upgrade
        return request.method().equals(HttpMethod.GET)
                && request.headers().contains(HttpHeaderNames.UPGRADE, HttpHeaderValues.WEBSOCKET, true)
                && request.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE, true);
    }

}
