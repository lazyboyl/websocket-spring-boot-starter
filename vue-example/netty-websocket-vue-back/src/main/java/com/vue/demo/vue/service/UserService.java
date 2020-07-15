package com.vue.demo.vue.service;

import com.vue.demo.vue.entity.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author linzf
 * @since 2020/7/15
 * 类描述：
 */
@Service
public class UserService {

    private static Logger log = LoggerFactory.getLogger(UserService.class);

    /**
     * 功能描述： 根据用户ID来获取用户数据
     *
     * @param userId 用户流水ID
     * @return
     */
    public UserVo getUserVoByUserId(String userId) {
        log.info("当前请求的userId的值是：{}", userId);
        // 模拟获取用户的ID数据
        UserVo userVo = new UserVo();
        userVo.setUserId("1");
        userVo.setBirthDate(new Date());
        userVo.setUserName("林泽锋");
        return userVo;
    }

}
