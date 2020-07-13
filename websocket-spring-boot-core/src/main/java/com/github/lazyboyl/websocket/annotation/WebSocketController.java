package com.github.lazyboyl.websocket.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类描述： websocket的controller的注解
 *
 * @author linzef
 * @since 2020-07-13
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface WebSocketController {
}
