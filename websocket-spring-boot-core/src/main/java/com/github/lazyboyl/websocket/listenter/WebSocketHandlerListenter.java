package com.github.lazyboyl.websocket.listenter;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author linzf
 * @since 2020/7/14
 * 类描述：
 */
public interface WebSocketHandlerListenter {

    /**
     * 当前执行的级别
     *
     * @return 返回当前的级别，数字越高代表拦截级别越高
     */
    int level();

    /**
     * 功能描述： 通道关闭的时候的响应事件
     *
     * @param ctx 当前的通道对象
     */
    void channelInactive(ChannelHandlerContext ctx);

    /**
     * 功能描述： 当首次握手的时候的响应事件
     *
     * @param ctx     当前的通道对象
     */
    void handleShake(ChannelHandlerContext ctx);

}
