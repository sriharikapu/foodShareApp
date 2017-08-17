package com.fengnian.smallyellowo.foodie.receivers;

import com.fan.framework.base.XData;

/**
 * Created by lanbiao on 2016/12/20.
 */

public class SYPushData extends XData{
    private String pushType;
    private SYBaseUserInfoData userInfo;

    private SYPushData(){};
    public static SYPushData createPushData(String notificationMessage){
        return null;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public SYBaseUserInfoData getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(SYBaseUserInfoData userInfo) {
        this.userInfo = userInfo;
    }
}
