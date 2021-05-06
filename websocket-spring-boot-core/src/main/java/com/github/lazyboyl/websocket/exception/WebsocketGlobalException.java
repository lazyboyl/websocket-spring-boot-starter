package com.github.lazyboyl.websocket.exception;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author linzf
 * @since 2021/5/6
 * 类描述： 全局异常补获的实现
 */
public interface WebsocketGlobalException {

    /**
     * 当前执行的级别
     *
     * @return 返回当前的级别，数字越高代表拦截级别越高
     */
    int level();

    /**
     * 功能描述： websocket的全局异常的处理
     *
     * @param e 异常
     * @return 返回处理结果
     */
    Object errorHandler(Exception e);

}
