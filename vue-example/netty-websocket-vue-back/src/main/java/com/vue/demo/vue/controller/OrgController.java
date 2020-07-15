package com.vue.demo.vue.controller;

import com.github.lazyboyl.websocket.annotation.WebSocketController;
import com.github.lazyboyl.websocket.annotation.WebSocketRequestMapping;
import com.vue.demo.vue.entity.OrgVo;
import com.vue.demo.vue.service.OrgService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author linzf
 * @since 2020/7/15
 * 类描述：
 */
@WebSocketController
@WebSocketRequestMapping("/org/")
public class OrgController {

    @Autowired
    private OrgService orgService;

    @WebSocketRequestMapping("getOrgVo")
    public OrgVo getOrgVo(String orgId, String orgName) {
        System.out.println("获取到的请求数据是："+ orgId + "和" + orgName);
        return orgService.getOrgByOrgId(orgId);
    }

}
