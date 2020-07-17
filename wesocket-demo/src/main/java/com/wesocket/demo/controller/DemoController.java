package com.wesocket.demo.controller;

import com.github.lazyboyl.websocket.annotation.WebSocketController;
import com.github.lazyboyl.websocket.annotation.WebSocketRequestMapping;
import com.github.lazyboyl.websocket.annotation.WebSocketRequestParam;
import com.wesocket.demo.entity.OrgVo;
import com.wesocket.demo.service.OrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


@WebSocketController
@WebSocketRequestMapping("/org/")
public class DemoController {

    private OrgService orgService;

    @Value("${websocket.port}")
    private Integer port;

    @Autowired
    public DemoController(OrgService orgService, @Value("${websocket.port}") Integer port) {
        this.orgService = orgService;
        this.port = port;
    }

    @WebSocketRequestMapping("getOrgVo111")
    public OrgVo getOrgVo111(String orgId, String orgName) {
        System.out.println("获取到的请求数据是：" + orgId + "和" + orgName + "，端口是" + port);
        return orgService.getOrgByOrgId(orgId);
    }


    @WebSocketRequestMapping("getOrgVo2222")
    public OrgVo getOrgVo2222(@WebSocketRequestParam(name = "oId") String orgId, @WebSocketRequestParam(name = "oName") String orgName) {
        System.out.println("获取到的请求数据是：" + orgId + "和" + orgName + "，端口是" + port);
        return orgService.getOrgByOrgId(orgId);
    }


}
