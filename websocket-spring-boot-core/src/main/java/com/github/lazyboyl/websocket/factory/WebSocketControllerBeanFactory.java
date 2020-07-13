package com.github.lazyboyl.websocket.factory;

/**
 * @author linzf
 * @since 2020/7/13
 * 类描述： websocket请求的工厂类
 */
public class WebSocketControllerBeanFactory extends NettyDefaultBeanFactory {

    private WebSocketControllerBeanFactory() {
        super();
    }

    public static WebSocketControllerBeanFactory getInstance() {
        return new WebSocketControllerBeanFactory();
    }

}
