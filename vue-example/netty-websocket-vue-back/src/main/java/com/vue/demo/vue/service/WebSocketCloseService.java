package com.vue.demo.vue.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 类描述： 关闭websocket的service
 *
 * @author linzef
 * @since 2020-07-15
 */
@Service
public class WebSocketCloseService {

    private static Logger log = LoggerFactory.getLogger(WebSocketCloseService.class);

    /**
     * 功能描述： 关闭的时候根据socket的ID移除
     * @param channelId
     */
    public void removeChannel(String channelId){
        // 模拟下线的时候的一些行为
        log.info("将我从聊天组中移除操作！{}",channelId);
        log.info("将我进行下线操作！{}",channelId);
    }

}
