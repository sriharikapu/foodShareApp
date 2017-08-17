package com.fengnian.smallyellowo.foodie.receivers;

/**
 * Created by lanbiao on 2016/12/20.
 * app内部小红点和数字消息结构
 */

public class SYInternalPushUserInfoData extends SYBaseUserInfoData {
    /**
     * 未读动态列表数目
     */
    private String haveNewMessage;

    public String getHasNewProgress() {
        return hasNewProgress;
    }

    public void setHasNewProgress(String hasNewProgress) {
        this.hasNewProgress = hasNewProgress;
    }

    private String hasNewProgress;

    /**
     * 互动消息数目
     */
    private String unReadDynamicCount;

    /**
     * 新好友关注数目
     */
    private String newAddFriendCount;



    /**
     * 通知列表未读数目
     */
    private String noticeCount;

    public String getHaveNewMessage() {
        return haveNewMessage;
    }

    public void setHaveNewMessage(String haveNewMessage) {
        this.haveNewMessage = haveNewMessage;
    }

    public String getNewAddFriendCount() {
        return newAddFriendCount;
    }

    public void setNewAddFriendCount(String newAddFriendCount) {
        this.newAddFriendCount = newAddFriendCount;
    }

    public String getUnReadDynamicCount() {
        return unReadDynamicCount;
    }

    public void setUnReadDynamicCount(String unReadDynamicCount) {
        this.unReadDynamicCount = unReadDynamicCount;
    }

    public String getNoticeCount() {
        return noticeCount;
    }

    public void setNoticeCount(String noticeCount) {
        this.noticeCount = noticeCount;
    }
}
