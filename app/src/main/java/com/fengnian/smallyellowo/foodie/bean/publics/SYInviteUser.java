package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

import com.fan.framework.base.XData;

import java.io.Serializable;

/**
 * Created by Administrator on 2016-9-13.
 */

public class SYInviteUser extends XData implements Parcelable, Serializable {
     private String joinTime;

    private SYUser inviteUser;

    public String getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }

    public SYUser getInviteUser() {
        return inviteUser;
    }

    public void setInviteUser(SYUser inviteUser) {
        this.inviteUser = inviteUser;
    }

    public SYInviteUser() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.joinTime);
        dest.writeParcelable(this.inviteUser, flags);
    }

    protected SYInviteUser(Parcel in) {
        super(in);
        this.joinTime = in.readString();
        this.inviteUser = in.readParcelable(SYUser.class.getClassLoader());
    }

    public static final Creator<SYInviteUser> CREATOR = new Creator<SYInviteUser>() {
        @Override
        public SYInviteUser createFromParcel(Parcel source) {
            return new SYInviteUser(source);
        }

        @Override
        public SYInviteUser[] newArray(int size) {
            return new SYInviteUser[size];
        }
    };
}
