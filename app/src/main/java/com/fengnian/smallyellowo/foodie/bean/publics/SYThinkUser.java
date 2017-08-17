package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

import com.fan.framework.base.XData;

import java.io.Serializable;

/**
 * Created by Administrator on 2016-9-9.
 */

public class SYThinkUser extends XData implements Parcelable ,Serializable {

    private   SYUser  user;
    private   String  type;

    public SYUser getUser() {
        return user;
    }

    public void setUser(SYUser user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SYThinkUser() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.type);
    }

    protected SYThinkUser(Parcel in) {
        super(in);
        this.user = in.readParcelable(SYUser.class.getClassLoader());
        this.type = in.readString();
    }

    public static final Creator<SYThinkUser> CREATOR = new Creator<SYThinkUser>() {
        @Override
        public SYThinkUser createFromParcel(Parcel source) {
            return new SYThinkUser(source);
        }

        @Override
        public SYThinkUser[] newArray(int size) {
            return new SYThinkUser[size];
        }
    };
}
