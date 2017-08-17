package com.fengnian.smallyellowo.foodie.bean.publics;

import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;

/**
 * Created by Administrator on 2016-9-18.
 */

public class Logindate extends BaseResult {

//    private UserInfo  account;
//
//    public UserInfo getAccount() {
//        return account;
//    }
//
//    public void setAccount(UserInfo account) {
//        this.account = account;
//    }

    private  String token;
    private String  deviceToken;
    private SYUser user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public SYUser getUser() {
        return user;
    }

    public void setUser(SYUser user) {
        this.user = user;
    }
}
