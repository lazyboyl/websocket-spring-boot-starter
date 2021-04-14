package com.vue.demo.vue.service;

import com.github.lazyboyl.websocket.util.JsonUtils;
import com.vue.demo.vue.entity.UserVo;
import com.vue.demo.vue.security.WebSocketHandlerListenterImpl;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author linzf
 * @since 2020/7/15
 * 类描述：
 */
@Service
public class UserService {

    private static Logger log = LoggerFactory.getLogger(UserService.class);

    @Value("${lazyboyl.user.name}")
    private String userName;

    /**
     * 批量推送当前登录用户的信息
     */
    public void sendMsg(){
        for (Map.Entry<String, Channel> entry : WebSocketHandlerListenterImpl.chanelIdMap.entrySet()) {
            entry.getValue().writeAndFlush(new TextWebSocketFrame("你好阿"));
        }
    }

    /**
     * 功能描述： 根据用户ID来获取用户数据
     *
     * @param userId 用户流水ID
     * @return
     */
    public UserVo getUserVoByUserId(String userId) {
        log.info("当前请求的userName的值是：{}", userName);
        // 模拟获取用户的ID数据
        UserVo userVo = new UserVo();
        userVo.setUserId("1");
        userVo.setBirthDate(new Date());
        userVo.setUserName("林泽锋");
        return userVo;
    }

}
