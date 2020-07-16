package com.github.lazyboyl.websocket.integrate;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author linzef
 * @since 2020-03-16
 * 注解描述： 实现开启netty服务
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({WebSocketScannerRegister.class})
public @interface EnableWebSocketServer {

    /**
     * 功能描述： 需要进行spring扫描的目录
     *
     * @return String[]
     */
    String[] basePackages() default {"com.github.lazyboyl.websocket"};

    /**
     * 当前需要进行netty扫描的目录
     *
     * @return String[]
     */
    String[] webSocketScanPackage() default {"com.github.lazyboyl.websocket"};

}
