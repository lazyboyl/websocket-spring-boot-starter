package com.wesocket.demo.security;

import com.github.lazyboyl.websocket.listenter.WebSocketHandlerListenter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author linzf
 * @since 2020/7/14
 * 类描述：
 */
public class WebSocketHandlerListenterImpl implements WebSocketHandlerListenter {
    @Override
    public int level() {
        return 0;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("当前关闭的通道的id是：" + ctx.channel().id().asLongText());
    }
}
