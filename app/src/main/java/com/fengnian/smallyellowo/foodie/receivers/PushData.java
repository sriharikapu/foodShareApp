package com.fengnian.smallyellowo.foodie.receivers;

import android.text.TextUtils;

/**
 * Created by Administrator on 2016-11-8.
 */

public class PushData {
    private UserinfoBean userinfo;
    private String type;

    public PushData(){
        setType("");
        setUserinfo(new UserinfoBean());
    }

    public UserinfoBean getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserinfoBean userinfo) {
        this.userinfo = userinfo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class UserinfoBean {

        public UserinfoBean(){
            newAddFriendCount = "0";
            haveNewMessage = "0";
            unReadDynamicCount = "0";
            hasNewProgress = "0";
            noticeCount = "0";
        }


        private String newAddFriendCount;
        private String haveNewMessage;
        private String unReadDynamicCount;
        private String hasNewProgress;
        private String noticeCount;


        public String getHasNewProgress() {
            return hasNewProgress;
        }

        public void setHasNewProgress(String hasNewProgress) {
            this.hasNewProgress = hasNewProgress;
        }


        public String getNewAddFriendCount() {
            return newAddFriendCount;
        }

        public void setNewAddFriendCount(String newAddFriendCount) {
            this.newAddFriendCount = newAddFriendCount;
        }

        public String getHaveNewMessage() {
            return haveNewMessage;
        }

        public void setHaveNewMessage(String haveNewMessage) {
            this.haveNewMessage = haveNewMessage;
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

        public int getNewFrientNum() {
            return getNum(newAddFriendCount);
        }

        public int getNewProgress(){
            return getNum(hasNewProgress);
        }

        public int getMessageNum() {
            return getNum(haveNewMessage);
        }

        public int getDynamicNum() {
            return getNum(unReadDynamicCount);
        }

        public int getNoticeNum() {
            return getNum(noticeCount);
        }

        private int getNum(String str) {
            if (TextUtils.isEmpty(str)) {
                return 0;
            }
            int num = 0;
            try {
                num = Integer.parseInt(str);
            } catch (Exception e) {

            }
            return num;
        }
    }
}
