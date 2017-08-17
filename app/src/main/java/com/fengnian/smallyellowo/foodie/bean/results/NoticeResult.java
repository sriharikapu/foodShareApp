package com.fengnian.smallyellowo.foodie.bean.results;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by Administrator on 2016-9-19.
 */
public class NoticeResult extends BaseResult {
    /**
     * messageType : 1
     * messageTitle : 圈友聚会活动招募
     * messageTime : 2016-09-02 18:44:29
     * messageSubContent : 吃货不孤单，周末自助烧烤狂欢趴，仅需1积分
     * messageHtmlUrl : http://static.tinydonuts.cn/notice/barbecue.html
     * messageIsDelete : false
     * noticeid : 4
     * messageId : 31
     * canApplyActivity : true
     */

    private List<NotificationMessagesBean> notificationMessages;

    public List<NotificationMessagesBean> getNotificationMessages() {
        return notificationMessages;
    }

    public void setNotificationMessages(List<NotificationMessagesBean> notificationMessages) {
        this.notificationMessages = notificationMessages;
    }

    public static class NotificationMessagesBean implements Parcelable {
        private int messageType;
        private String messageTitle;
        private String messageTime;
        private String messageSubContent;
        private String messageHtmlUrl;
        @JSONField(serialize = false)
        public boolean isExpand;
        private int noticeid;
        private int messageId;

        public int getMessageStatus() {
            return messageStatus;
        }

        public void setMessageStatus(int messageStatus) {
            this.messageStatus = messageStatus;
        }

        private int messageStatus;
        private boolean canApplyActivity;

        public int getMessageType() {
            return messageType;
        }

        public void setMessageType(int messageType) {
            this.messageType = messageType;
        }

        public String getMessageTitle() {
            return messageTitle;
        }

        public void setMessageTitle(String messageTitle) {
            this.messageTitle = messageTitle;
        }

        public String getMessageTime() {
            return messageTime;
        }

        public void setMessageTime(String messageTime) {
            this.messageTime = messageTime;
        }

        public String getMessageSubContent() {
            return messageSubContent;
        }

        public void setMessageSubContent(String messageSubContent) {
            this.messageSubContent = messageSubContent;
        }

        public String getMessageHtmlUrl() {
            return messageHtmlUrl;
        }

        public void setMessageHtmlUrl(String messageHtmlUrl) {
            this.messageHtmlUrl = messageHtmlUrl;
        }

        public int getNoticeid() {
            return noticeid;
        }

        public void setNoticeid(int noticeid) {
            this.noticeid = noticeid;
        }

        public int getMessageId() {
            return messageId;
        }

        public void setMessageId(int messageId) {
            this.messageId = messageId;
        }

        public boolean isCanApplyActivity() {
            return canApplyActivity;
        }

        public void setCanApplyActivity(boolean canApplyActivity) {
            this.canApplyActivity = canApplyActivity;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.messageType);
            dest.writeString(this.messageTitle);
            dest.writeString(this.messageTime);
            dest.writeString(this.messageSubContent);
            dest.writeString(this.messageHtmlUrl);
            dest.writeInt(this.noticeid);
            dest.writeInt(this.messageId);
            dest.writeByte(this.canApplyActivity ? (byte) 1 : (byte) 0);
        }

        public NotificationMessagesBean() {
        }

        protected NotificationMessagesBean(Parcel in) {
            this.messageType = in.readInt();
            this.messageTitle = in.readString();
            this.messageTime = in.readString();
            this.messageSubContent = in.readString();
            this.messageHtmlUrl = in.readString();
            this.noticeid = in.readInt();
            this.messageId = in.readInt();
            this.canApplyActivity = in.readByte() != 0;
        }

        public static final Parcelable.Creator<NotificationMessagesBean> CREATOR = new Parcelable.Creator<NotificationMessagesBean>() {
            @Override
            public NotificationMessagesBean createFromParcel(Parcel source) {
                return new NotificationMessagesBean(source);
            }

            @Override
            public NotificationMessagesBean[] newArray(int size) {
                return new NotificationMessagesBean[size];
            }
        };
    }
}
