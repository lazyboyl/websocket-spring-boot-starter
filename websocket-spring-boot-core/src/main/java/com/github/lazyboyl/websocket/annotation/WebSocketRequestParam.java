package com.github.lazyboyl.websocket.annotation;

import java.lang.annotation.*;

/**
 * 类描述：请求参数的别名的在定义的注解
 *
 * @author linzef
 * @since 2020-07-17
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebSocketRequestParam {

    String name() default "";

}
