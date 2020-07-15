package com.vue.demo.vue.service;

import com.vue.demo.vue.entity.OrgVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 类描述： 组织的service
 *
 * @author linzef
 * @since 2020-07-15
 */
@Service
public class OrgService {

    private static Logger log = LoggerFactory.getLogger(OrgService.class);

    /**
     * 功能描述： 模拟请求获取组织数据
     *
     * @param orgId 组织ID
     * @return 返回组织数据
     */
    public OrgVo getOrgByOrgId(String orgId) {
        log.info("当前请求的组织ID是：{}", orgId);
        OrgVo orgVo = new OrgVo();
        orgVo.setOrgId(orgId);
        return orgVo;
    }

}
