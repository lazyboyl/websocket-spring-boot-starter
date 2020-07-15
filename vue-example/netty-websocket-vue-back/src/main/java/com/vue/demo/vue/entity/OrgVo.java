package com.vue.demo.vue.entity;

import java.util.List;

/**
 * @author linzf
 * @since 2020/7/15
 * 类描述：
 */
public class OrgVo {

    private String orgId;

    private String orgName;

    private List<UserVo> userVoList;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public List<UserVo> getUserVoList() {
        return userVoList;
    }

    public void setUserVoList(List<UserVo> userVoList) {
        this.userVoList = userVoList;
    }
}
