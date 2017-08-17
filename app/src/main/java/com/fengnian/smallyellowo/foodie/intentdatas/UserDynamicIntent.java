package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;

/**
 * Created by Administrator on 2016-9-1.
 */
public class UserDynamicIntent extends IntentData {
    String userId;

    public String getUserName() {
        return userName;
    }

    public UserDynamicIntent setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    String userName;

    public String getUserId() {
        return userId;
    }

    public UserDynamicIntent setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public UserDynamicIntent() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.userId);
        dest.writeString(this.userName);
    }

    protected UserDynamicIntent(Parcel in) {
        super(in);
        this.userId = in.readString();
        this.userName = in.readString();
    }

    public static final Creator<UserDynamicIntent> CREATOR = new Creator<UserDynamicIntent>() {
        @Override
        public UserDynamicIntent createFromParcel(Parcel source) {
            return new UserDynamicIntent(source);
        }

        @Override
        public UserDynamicIntent[] newArray(int size) {
            return new UserDynamicIntent[size];
        }
    };
}
