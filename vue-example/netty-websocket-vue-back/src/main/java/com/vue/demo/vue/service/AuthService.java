package com.vue.demo.vue.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 类描述： 鉴权的service
 *
 * @author linzef
 * @since 2020-07-15
 */
@Service
public class AuthService {

    private static Logger log = LoggerFactory.getLogger(AuthService.class);


    /**
     * 功能描述： 实现模拟鉴权
     * @param url 当前响应的地址
     * @return
     */
    public Boolean authUrl(String url) {
        log.info("当前请求的url：{}", url);
        if (url.indexOf("user") == -1) {
            return false;
        }
        return true;
    }

}


