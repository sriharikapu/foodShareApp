package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;

/**
 * Created by elaine on 2017/7/24.
 */

public class BindIntentData extends IntentData {

    public int nickNameStatus;

    public  String   phone="";

    public String userId = "";
    public String token = "";

    public int getNickNameStatus() {
        return nickNameStatus;
    }

    public void setNickNameStatus(int nickNameStatus) {
        this.nickNameStatus = nickNameStatus;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.nickNameStatus);
        dest.writeString(this.phone);
        dest.writeString(this.userId);
        dest.writeString(this.token);
    }

    public BindIntentData() {
    }

    protected BindIntentData(Parcel in) {
        super(in);
        this.nickNameStatus = in.readInt();
        this.phone = in.readString();
        this.userId = in.readString();
        this.token = in.readString();
    }

    public static final Creator<BindIntentData> CREATOR = new Creator<BindIntentData>() {
        @Override
        public BindIntentData createFromParcel(Parcel source) {
            return new BindIntentData(source);
        }

        @Override
        public BindIntentData[] newArray(int size) {
            return new BindIntentData[size];
        }
    };
}
