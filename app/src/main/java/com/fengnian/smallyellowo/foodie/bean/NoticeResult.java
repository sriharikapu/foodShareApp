package com.fengnian.smallyellowo.foodie.bean;

import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;

import java.util.List;

/**
 * Created by Administrator on 2016-11-8.
 */

public class NoticeResult extends BaseResult{
    private String result;
    private String messageId;
    private List<MessageListBean> messageList;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public List<MessageListBean> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageListBean> messageList) {
        this.messageList = messageList;
    }

    public static class MessageListBean {
        private String foodRecordId;
        private boolean foodRecordIsDelete;
        private String  interactType;
        private String interactTime;
        private String interactAccount;
        private int pubType;
        private String summerize;
        private String content;
        private String commentIsDelete;
        private String interactHeadImage;
        private String foodRecordImage;
        private String interactNickname;
        private String foodRecordPosition;
        private String messageId;

        public String getFoodRecordId() {
            return foodRecordId;
        }

        public void setFoodRecordId(String foodRecordId) {
            this.foodRecordId = foodRecordId;
        }

        public boolean isFoodRecordIsDelete() {
            return foodRecordIsDelete;
        }

        public void setFoodRecordIsDelete(boolean foodRecordIsDelete) {
            this.foodRecordIsDelete = foodRecordIsDelete;
        }

        public String getInteractType() {
            return interactType;
        }

        public void setInteractType(String interactType) {
            this.interactType = interactType;
        }

        public String getInteractTime() {
            return interactTime;
        }

        public void setInteractTime(String interactTime) {
            this.interactTime = interactTime;
        }

        public String getInteractAccount() {
            return interactAccount;
        }

        public void setInteractAccount(String interactAccount) {
            this.interactAccount = interactAccount;
        }

        public int getPubType() {
            return pubType;
        }

        public void setPubType(int pubType) {
            this.pubType = pubType;
        }

        public String getSummerize() {
            return summerize;
        }

        public void setSummerize(String summerize) {
            this.summerize = summerize;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCommentIsDelete() {
            return commentIsDelete;
        }

        public void setCommentIsDelete(String commentIsDelete) {
            this.commentIsDelete = commentIsDelete;
        }

        public String getInteractHeadImage() {
            return interactHeadImage;
        }

        public void setInteractHeadImage(String interactHeadImage) {
            this.interactHeadImage = interactHeadImage;
        }

        public String getFoodRecordImage() {
            return foodRecordImage;
        }

        public void setFoodRecordImage(String foodRecordImage) {
            this.foodRecordImage = foodRecordImage;
        }

        public String getInteractNickname() {
            return interactNickname;
        }

        public void setInteractNickname(String interactNickname) {
            this.interactNickname = interactNickname;
        }

        public String getFoodRecordPosition() {
            return foodRecordPosition;
        }

        public void setFoodRecordPosition(String foodRecordPosition) {
            this.foodRecordPosition = foodRecordPosition;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }
    }
}
