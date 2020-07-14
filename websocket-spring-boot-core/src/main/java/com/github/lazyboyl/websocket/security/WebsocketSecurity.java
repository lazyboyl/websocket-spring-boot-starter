package com.github.lazyboyl.websocket.security;

import com.github.lazyboyl.websocket.server.channel.entity.SocketRequest;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author linzf
 * @since 2020/7/14
 * 类描述： websocket请求的过滤器
 */
public interface WebsocketSecurity {

    /**
     * 当前执行的级别
     *
     * @return 返回当前的级别，数字越高代表拦截级别越高
     */
    int level();


    /**
     * 功能描述： 实现websocket的权限的拦截方法
     *
     * @param ctx           当前通道对象
     * @param socketRequest 请求对象实体
     * @return true：表示继续执行；false：表示直接结束
     */
    Boolean authentication(ChannelHandlerContext ctx, SocketRequest socketRequest);

}
