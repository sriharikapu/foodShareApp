package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;

import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;

/**
 * Created by Administrator on 2016-8-15.
 */
public class UserInfoIntent extends IntentData {
    SYUser user;
    String id;

    public SYUser getUser() {
        return user;
    }

    public void setUser(SYUser user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserInfoIntent() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.id);
    }

    protected UserInfoIntent(Parcel in) {
        super(in);
        this.user = in.readParcelable(SYUser.class.getClassLoader());
        this.id = in.readString();
    }

    public static final Creator<UserInfoIntent> CREATOR = new Creator<UserInfoIntent>() {
        @Override
        public UserInfoIntent createFromParcel(Parcel source) {
            return new UserInfoIntent(source);
        }

        @Override
        public UserInfoIntent[] newArray(int size) {
            return new UserInfoIntent[size];
        }
    };
}
