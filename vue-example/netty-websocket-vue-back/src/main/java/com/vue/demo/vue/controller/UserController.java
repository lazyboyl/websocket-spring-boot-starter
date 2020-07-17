package com.vue.demo.vue.controller;

import com.github.lazyboyl.websocket.annotation.WebSocketController;
import com.github.lazyboyl.websocket.annotation.WebSocketRequestMapping;
import com.github.lazyboyl.websocket.annotation.WebSocketRequestParam;
import com.vue.demo.vue.entity.OrgVo;
import com.vue.demo.vue.entity.UserVo;
import com.vue.demo.vue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @author linzf
 * @since 2020/7/15
 * 类描述：
 */
@WebSocketController
@WebSocketRequestMapping("/user/")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 功能描述： 模拟字段请求且使用别名的方式来实现根据用户ID来获取用户数据
     *
     * @param userId 用户的流水ID
     * @return
     */
    @WebSocketRequestMapping("getUserVoByUserIdRename")
    public UserVo getUserVoByUserIdRename(@WebSocketRequestParam(name = "uId") String userId) {
        return userService.getUserVoByUserId(userId);
    }

    /**
     * 功能描述： 模拟实体请求的方式来实现根据用户ID来获取用户数据
     *
     * @param userVo 用户的实体
     * @return
     */
    @WebSocketRequestMapping("getUserVoByUserEntity")
    public UserVo getUserVoByUserEntity(UserVo userVo) {
        return userService.getUserVoByUserId(userVo.getUserId());
    }

    /**
     * 功能描述： 模拟字段请求的方式来实现根据用户ID来获取用户数据
     *
     * @param userId 用户的流水ID
     * @return
     */
    @WebSocketRequestMapping("getUserVoByUserId")
    public UserVo getUserVoByUserId(String userId) {
        return userService.getUserVoByUserId(userId);
    }


    /**
     * 功能描述： 模拟map请求的方式来实现根据用户ID来获取用户数据
     *
     * @param user 用户的Map信息
     * @return
     */
    @WebSocketRequestMapping("getUserVoByUserMap")
    public UserVo getUserVoByUserMap(Map<String, Object> user) {
        return userService.getUserVoByUserId((String) user.get("userId"));
    }


    /**
     * 功能描述： 模拟复杂对象请求的方式来实现根据用户ID来获取用户数据
     *
     * @param orgVo 复杂的对象
     * @return
     */
    @WebSocketRequestMapping("getUserVoByUserOrgVo")
    public UserVo getUserVoByUserOrgVo(OrgVo orgVo) {
        return userService.getUserVoByUserId(orgVo.getUserVoList().get(0).getUserId());
    }

    /**
     * 功能描述： 模拟List对象请求的方式来实现根据用户ID来获取用户数据
     *
     * @param userVoList 集合对象
     * @return
     */
    @WebSocketRequestMapping("getUserVoByUserList")
    public UserVo getUserVoByUserList(List<UserVo> userVoList) {
        return userService.getUserVoByUserId(userVoList.get(0).getUserId());
    }


}
