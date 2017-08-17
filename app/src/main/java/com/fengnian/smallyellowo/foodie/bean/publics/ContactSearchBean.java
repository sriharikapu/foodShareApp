package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016-9-12.
 */

public class ContactSearchBean implements Parcelable {
    private User  user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.user, flags);
    }

    public ContactSearchBean() {
    }

    protected ContactSearchBean(Parcel in) {
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Parcelable.Creator<ContactSearchBean> CREATOR = new Parcelable.Creator<ContactSearchBean>() {
        @Override
        public ContactSearchBean createFromParcel(Parcel source) {
            return new ContactSearchBean(source);
        }

        @Override
        public ContactSearchBean[] newArray(int size) {
            return new ContactSearchBean[size];
        }
    };
}
