package com.vue.demo.vue.entity;

import java.util.Date;

/**
 * @author linzf
 * @since 2020/7/15
 * 类描述：
 */
public class UserVo {

    private String userId;

    private String userName;

    private Date birthDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
