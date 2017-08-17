package com.fengnian.smallyellowo.foodie.bean.publics;

/**
 * Created by Administrator on 2017-3-23.
 */

public class SYAccountUser {


    private String token;
    private String deviceToken;

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
