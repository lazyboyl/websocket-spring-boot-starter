package com.github.lazyboyl.websocket.constant;

/**
 * 类描述： websocket的配置枚举类
 *
 * @author linzef
 * @since 2020-07-14
 */
public enum WebSocketProperties {

    /**
     * 服务端口号
     */
    PORT("websocket.port", "8399"),
    /**
     * boss的线程数
     */
    BOSSTHREAD("websocket.thread.boss", "12"),
    /**
     * 工作线程数
     */
    WORKTHREAD("websocket.thread.work", "12"),
    /**
     * websocket请求的地址
     */
    WEBSOCKETACITON("websocket.action","ws");

    private String key;

    private String defaultValue;

    WebSocketProperties(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return key;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
