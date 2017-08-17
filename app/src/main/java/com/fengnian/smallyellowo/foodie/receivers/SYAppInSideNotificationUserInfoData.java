package com.fengnian.smallyellowo.foodie.receivers;

/**
 * Created by lanbiao on 2016/12/20.
 * 应用内有广播类消息
 */

public class SYAppInSideNotificationUserInfoData extends SYBaseUserInfoData {

    /**
     * h5对应的url
     */
    private String messageHtmlUrl;

    /**
     * 通知类型 官方消息、活动通知、系统消息
     */
    private String messageType;

    /**
     * 消息的id
     */
    private String noticeId;

    public String getMessageHtmlUrl() {
        return messageHtmlUrl;
    }

    public void setMessageHtmlUrl(String messageHtmlUrl) {
        this.messageHtmlUrl = messageHtmlUrl;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }
}
