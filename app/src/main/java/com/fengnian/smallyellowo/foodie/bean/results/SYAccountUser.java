package com.fengnian.smallyellowo.foodie.bean.results;

import android.os.Parcel;
import android.os.Parcelable;

import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;

/**
 * Created by Administrator on 2016-9-18.
 */

public class SYAccountUser extends BaseResult implements Parcelable {


    public String token;
    public String deviceToken;
    public SYUser user;

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
        dest.writeString(this.deviceToken);
        dest.writeParcelable(this.user, flags);
    }

    public SYAccountUser() {
    }

    protected SYAccountUser(Parcel in) {
        this.token = in.readString();
        this.deviceToken = in.readString();
        this.user = in.readParcelable(SYUser.class.getClassLoader());
    }

    public static final Parcelable.Creator<SYAccountUser> CREATOR = new Parcelable.Creator<SYAccountUser>() {
        @Override
        public SYAccountUser createFromParcel(Parcel source) {
            return new SYAccountUser(source);
        }

        @Override
        public SYAccountUser[] newArray(int size) {
            return new SYAccountUser[size];
        }
    };
}
