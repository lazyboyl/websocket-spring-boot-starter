package com.github.lazyboyl.websocket.util;

import com.github.lazyboyl.websocket.server.NettySocketServer;
import com.github.lazyboyl.websocket.server.channel.entity.SocketResponse;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @author linzf
 * @since 2020/7/14
 * 类描述：
 */
public class SocketUtil {

    /**
     * 功能描述： 全局发送消息
     * @param socketResponse 需要发送的消息
     */
    public static void sendToGroup(SocketResponse socketResponse){
        NettySocketServer.channelGroup.writeAndFlush(new TextWebSocketFrame(JsonUtils.objToJson(socketResponse)));
    }

    /**
     * 功能描述： 像前端推送信息
     * @param channel 当前的通道
     * @param socketResponse 返回的结果
     */
    public static void writeAndFlush(Channel channel, SocketResponse socketResponse) {
        channel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objToJson(socketResponse)));
    }

}
