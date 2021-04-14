package com.vue.demo.vue.security;

import com.github.lazyboyl.websocket.listenter.WebSocketHandlerListenter;
import com.vue.demo.vue.service.WebSocketCloseService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author linzf
 * @since 2020/7/14
 * 类描述： 通道关闭监听器
 */
public class WebSocketHandlerListenterImpl implements WebSocketHandlerListenter {

    public static Map<String, Channel> chanelIdMap = new HashMap<>();

    @Autowired
    private WebSocketCloseService webSocketCloseService;


    @Override
    public int level() {
        return 0;
    }

    /**
     * 功能描述： 当浏览器端的通道关闭的时候的响应处理方法
     *
     * @param ctx 当前的通道对象
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("当前关闭的通道的id是：" + ctx.channel().id().asLongText());
        webSocketCloseService.removeChannel(ctx.channel().id().asLongText());
        chanelIdMap.remove(ctx.channel().id().asLongText());
    }

    @Override
    public void handleShake(ChannelHandlerContext ctx) {
        System.out.println("当前开启的通道的id是：" + ctx.channel().id().asLongText());
        chanelIdMap.put(ctx.channel().id().asLongText(),ctx.channel());
    }
}
