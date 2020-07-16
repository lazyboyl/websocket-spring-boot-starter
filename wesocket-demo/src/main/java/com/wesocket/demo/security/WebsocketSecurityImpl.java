package com.wesocket.demo.security;

import com.github.lazyboyl.websocket.security.WebsocketSecurity;
import com.github.lazyboyl.websocket.server.channel.entity.SocketRequest;
import com.github.lazyboyl.websocket.server.channel.entity.SocketResponse;
import com.github.lazyboyl.websocket.util.JsonUtils;
import com.wesocket.demo.service.AuthService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author linzf
 * @since 2020/7/14
 * 类描述：请求拦截器
 */
public class WebsocketSecurityImpl implements WebsocketSecurity {

    @Autowired
    private AuthService authService;

    @Override
    public int level() {
        return 10;
    }

    @Override
    public Boolean authentication(ChannelHandlerContext ctx, SocketRequest socketRequest) {
        if(socketRequest.getUrl().indexOf("getOrgVo111")!=-1){
            return true;
        }
        Boolean isPass = authService.authUrl(socketRequest.getUrl());
        System.out.println("----我在这里做了一个模拟的鉴权过程---");
        if(!isPass){
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtils.objToJson(new SocketResponse(HttpResponseStatus.UNAUTHORIZED.code(), "授权不通过！"))));
        }
        return isPass;
    }
}
