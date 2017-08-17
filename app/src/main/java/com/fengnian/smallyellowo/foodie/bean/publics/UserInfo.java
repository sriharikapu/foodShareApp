package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

/**
 * 用户的信息
 */

public class UserInfo extends SYUser {

    private String account;
    private String phoneNumber;
    private String passWord;
    private String createTime;
    private String status;
    private SYImage headImage;
    private String nickname;
    private String token;
    private String deviceToken;
    private String isPin;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
        setId(account);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SYImage getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        if (!TextUtils.isEmpty(headImage) && headImage.startsWith("{")) {
            this.headImage = JSON.parseObject(headImage, SYImage.class);
            return;
        }
        this.headImage = new SYImage();
        this.headImage.setUrl(headImage);
        this.headImage.setThumbUrl(headImage);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        super.setNickName(nickname);
        this.nickname = nickname;
    }

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

    public String getIsPin() {
        return isPin;
    }

    public void setIsPin(String isPin) {
        this.isPin = isPin;
    }


    public UserInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.account);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.passWord);
        dest.writeString(this.createTime);
        dest.writeString(this.status);
        dest.writeParcelable(this.headImage, flags);
        dest.writeString(this.nickname);
        dest.writeString(this.token);
        dest.writeString(this.deviceToken);
        dest.writeString(this.isPin);
    }

    protected UserInfo(Parcel in) {
        super(in);
        this.account = in.readString();
        this.phoneNumber = in.readString();
        this.passWord = in.readString();
        this.createTime = in.readString();
        this.status = in.readString();
        this.headImage = in.readParcelable(SYImage.class.getClassLoader());
        this.nickname = in.readString();
        this.token = in.readString();
        this.deviceToken = in.readString();
        this.isPin = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
