package com.wesocket.demo.exception;

import com.github.lazyboyl.websocket.exception.WebsocketGlobalException;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author linzf
 * @since 2021/5/6
 * 类描述：
 */
public class WebsocketGlobalExceptionImpl implements WebsocketGlobalException {

    @Override
    public int level() {
        return 0;
    }

    @Override
    public Object errorHandler(Exception e) {
        Map<String,Object> r = new HashMap<>();
        r.put("code",200);
        r.put("result","失败了");
        return r;
    }
}
