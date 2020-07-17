package com.github.lazyboyl.websocket.server.channel;

import com.github.lazyboyl.websocket.beans.NettyBeanDefinition;
import com.github.lazyboyl.websocket.beans.NettyMethodDefinition;
import com.github.lazyboyl.websocket.server.NettySocketServer;
import com.github.lazyboyl.websocket.server.channel.entity.SocketRequest;
import com.github.lazyboyl.websocket.server.channel.entity.SocketResponse;
import com.github.lazyboyl.websocket.server.channel.entity.WebSocketRequestEntity;
import com.github.lazyboyl.websocket.util.ClassUtil;
import com.github.lazyboyl.websocket.util.JsonUtils;
import com.github.lazyboyl.websocket.util.SocketUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.AttributeKey;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;


/**
 * @author linzf
 * @since 2020/7/9
 * 类描述：
 */
public class WebSocketHandler extends SimpleChannelInboundHandler<WebSocketRequestEntity> {

    private static final AttributeKey<WebSocketServerHandshaker> HAND_SHAKE_ATTR = AttributeKey.valueOf("HAND_SHAKE");


    /**
     * @param ctx                    通道对象
     * @param webSocketRequestEntity 请求实体
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketRequestEntity webSocketRequestEntity) {
        //处理握手
        if (webSocketRequestEntity.getRequest() != null) {
            this.handleShake(ctx, webSocketRequestEntity.getRequest());
        }
        //处理websocket数据
        if (webSocketRequestEntity.getFrame() != null) {
            this.handleFrame(ctx, webSocketRequestEntity.getFrame());
        }
    }

    /**
     * @param ctx 通道对象
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("与客户端断开连接，通道关闭！" + ctx.channel().id().asLongText());
        List<NettyBeanDefinition> nettyBeanDefinitions = NettySocketServer.websocketHandlerListenterBeanFactory.getNettyBeanDefinitionList();
        doHandlerListenter(ctx, nettyBeanDefinitions, "channelInactive");
    }

    /**
     * 功能描述： 处理websocket数据
     *
     * @param ctx   通道对象
     * @param frame socket请求
     */
    private void handleFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 判断是否关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            WebSocketServerHandshaker handshaker = ctx.channel().attr(HAND_SHAKE_ATTR).get();
            if (handshaker == null) {
                ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
                return;
            }
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 暂仅支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException("暂不支持该消息类型：" + frame.getClass().getName());
        }
        // 处理消息
        String msg = ((TextWebSocketFrame) frame).text();
        try {
            SocketRequest socketRequest = JsonUtils.jsonToPojo(msg, SocketRequest.class);
            socketRequest.setSocketId(ctx.channel().id().asLongText());
            // 实现鉴权的拦截
            List<NettyBeanDefinition> nettyBeanDefinitions = NettySocketServer.websocketSecurityBeanFactory.getNettyBeanDefinitionList();
            if (!doAuthentication(ctx, nettyBeanDefinitions, socketRequest)) {
                return;
            }
            String uri = socketRequest.getUrl();
            uri = parseUri(uri);
            NettyMethodDefinition nettyMethodDefinition = NettySocketServer.webSocketControllerBeanFactory.getNettyMethodDefinition(uri);
            if (nettyMethodDefinition == null) {
                SocketUtil.writeAndFlush(ctx.channel(), new SocketResponse(HttpResponseStatus.NOT_FOUND.code(), "无此方法！"));
            } else {
                NettyBeanDefinition nettyBeanDefinition = NettySocketServer.webSocketControllerBeanFactory.getNettyBeanDefinition(nettyMethodDefinition.getBeanName());
                if (nettyBeanDefinition == null) {
                    SocketUtil.writeAndFlush(ctx.channel(), new SocketResponse(HttpResponseStatus.NOT_FOUND.code(), "无此方法！"));
                } else {
                    invokeMethod(ctx, nettyMethodDefinition, nettyBeanDefinition, socketRequest.getParams());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            SocketUtil.writeAndFlush(ctx.channel(), new SocketResponse(HttpResponseStatus.BAD_REQUEST.code(), "JSON解析异常"));
        }
    }

    /**
     * 功能描述：通道关闭/开启的时候的监听事件
     *
     * @param ctx                  当前关闭的通道对象
     * @param nettyBeanDefinitions 待响应的方法
     * @param action               当前请求的操作类型
     */
    protected void doHandlerListenter(ChannelHandlerContext ctx, List<NettyBeanDefinition> nettyBeanDefinitions, String action) {
        for (NettyBeanDefinition nbd : nettyBeanDefinitions) {
            for (Map.Entry<String, NettyMethodDefinition> entry : nbd.getMethodMap().entrySet()) {
                String[] k1s = entry.getKey().split("\\.");
                Object[] obj = new Object[]{ctx};
                if (k1s[k1s.length - 1].equals(action)) {
                    try {
                        entry.getValue().getMethod().invoke(nbd.getObject(), obj);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 功能描述： 实现前置的鉴权的逻辑
     *
     * @param ctx                  请求对象
     * @param nettyBeanDefinitions 定义的鉴权的实现类
     * @param socketRequest        请求的对象
     * @return true：鉴权通过，false：鉴权不通过
     */
    protected Boolean doAuthentication(ChannelHandlerContext ctx, List<NettyBeanDefinition> nettyBeanDefinitions, SocketRequest socketRequest) {
        for (NettyBeanDefinition nbd : nettyBeanDefinitions) {
            for (Map.Entry<String, NettyMethodDefinition> entry : nbd.getMethodMap().entrySet()) {
                String[] k1s = entry.getKey().split("\\.");
                Object[] obj = new Object[]{ctx, socketRequest};
                if (k1s[k1s.length - 1].equals("authentication")) {
                    try {
                        Boolean isContinue = (Boolean) entry.getValue().getMethod().invoke(nbd.getObject(), obj);
                        if (!isContinue) {
                            return false;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    /**
     * 功能描述： 重新处理uri使其符合map的映射标准
     *
     * @param uri 请求地址
     * @return 处理以后的请求地址
     */
    protected String parseUri(String uri) {
        if ("/".equals(uri.substring(0, 1))) {
            uri = uri.substring(1);
        }
        if (!"/".equals(uri.substring(uri.length() - 1))) {
            uri = uri + "/";
        }
        return uri;
    }

    /**
     * 功能描述： 实现反射调用方法
     *
     * @param ctx                   netty通道镀锡
     * @param nettyMethodDefinition 方法对象
     * @param nettyBeanDefinition   类对象
     * @param paramMap              请求参数
     */
    protected void invokeMethod(ChannelHandlerContext ctx, NettyMethodDefinition nettyMethodDefinition, NettyBeanDefinition nettyBeanDefinition, Map<String, Object> paramMap) {
        Object object = null;
        if (nettyMethodDefinition.getParameters().length == 0) {
            try {
                object = nettyMethodDefinition.getMethod().invoke(nettyBeanDefinition.getObject());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            Parameter[] ps = nettyMethodDefinition.getParameters();
            Object[] obj = new Object[ps.length];
            Class[] parameterTypesClass = nettyMethodDefinition.getParameterTypesClass();
            String [] requestParamName = nettyMethodDefinition.getRequestParamName();
            for (int i = 0; i < ps.length; i++) {
                Parameter p = ps[i];
                if (isMyClass(parameterTypesClass[i])) {
                    if (parameterTypesClass[i].getName().equals("java.util.Map")) {
                        obj[i] = paramMap;
                    } else if (parameterTypesClass[i].getName().equals("java.util.List")) {
                        obj[i] = JsonUtils.objToList(paramMap.get(p.getName()), ClassUtil.getClass((p.getParameterizedType().getTypeName().replace("java.util.List<", "").replace(">", ""))));
                    } else if ("java.util.".indexOf(parameterTypesClass[i].getName()) != -1) {
                        SocketUtil.writeAndFlush(ctx.channel(), new SocketResponse(HttpResponseStatus.BAD_REQUEST.code(), "java.util系列暂时只支持map和list，其他类型暂不支持。"));
                    } else {
                        if("".equals(requestParamName[i])){
                            obj[i] = paramMap.get(p.getName());
                        } else {
                            obj[i] = paramMap.get(requestParamName[i]);
                        }
                    }
                } else {
                    if (parameterTypesClass[i].getName().equals(HttpHeaders.class.getName())) {
                        obj[i] = paramMap.get("headers");
                    } else if (parameterTypesClass[i].getName().equals(ChannelHandlerContext.class.getName())) {
                        obj[i] = ctx;
                    } else {
                        obj[i] = JsonUtils.map2object(paramMap, parameterTypesClass[i]);
                    }
                }
            }
            try {
                object = nettyMethodDefinition.getMethod().invoke(nettyBeanDefinition.getObject(), obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        if (!"void".equals(nettyMethodDefinition.getReturnClass().getName())) {
            SocketUtil.writeAndFlush(ctx.channel(), new SocketResponse(HttpResponseStatus.OK.code(), "", object));
        }
    }

    /**
     * 功能描述： 判断当前的class是否是自己定义的class
     *
     * @param s 需要判断的class对象
     * @return true: JDK本身的类；false：自己定义的类
     */
    protected Boolean isMyClass(Class s) {
        if (s.getClassLoader() == null) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 功能描述： 处理握手
     *
     * @param ctx     通道对戏
     * @param request http请求实体
     */
    private void handleShake(ChannelHandlerContext ctx, FullHttpRequest request) {
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(null, null, false);
        WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(request);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), request);
            System.out.println("------当前正在握手-------");
            List<NettyBeanDefinition> nettyBeanDefinitions = NettySocketServer.websocketHandlerListenterBeanFactory.getNettyBeanDefinitionList();
            doHandlerListenter(ctx, nettyBeanDefinitions, "handleShake");
        }
    }
}
