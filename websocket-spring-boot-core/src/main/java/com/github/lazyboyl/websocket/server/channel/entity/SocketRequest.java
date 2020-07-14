package com.github.lazyboyl.websocket.server.channel.entity;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

/**
 * @author linzf
 * @since 2020/7/13
 * 类描述： socket的请求实体
 */
public class SocketRequest {

    /**
     * 请求的唯一标识
     */
    private String socketId;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求的JSON参数
     */
    private Map<String,Object> params;

    /**
     * 当前登录的通道对象
     */
    private ChannelHandlerContext channel;

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public ChannelHandlerContext getChannel() {
        return channel;
    }

    public void setChannel(ChannelHandlerContext channel) {
        this.channel = channel;
    }
}
