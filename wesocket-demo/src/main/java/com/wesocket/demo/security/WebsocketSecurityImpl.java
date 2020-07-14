package com.wesocket.demo.security;

import com.github.lazyboyl.websocket.security.WebsocketSecurity;
import com.github.lazyboyl.websocket.server.channel.entity.SocketRequest;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author linzf
 * @since 2020/7/14
 * 类描述：
 */
public class WebsocketSecurityImpl implements WebsocketSecurity {
    @Override
    public int level() {
        return 10;
    }

    @Override
    public Boolean authentication(ChannelHandlerContext ctx, SocketRequest socketRequest) {
        System.out.println("----我是一个拦截器的实现---");
        return true;
    }
}
