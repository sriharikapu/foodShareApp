package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017-2-21.
 */

public class SYFriendRecommendModel implements Parcelable {

    private SYUser user;
    private int commonFriends;
    private int readStatu;//0未读 1已读

    public SYUser getUser() {
        return user;
    }

    public void setUser(SYUser user) {
        this.user = user;
    }

    public int getCommonFriends() {
        return commonFriends;
    }

    public void setCommonFriends(int commonFriends) {
        this.commonFriends = commonFriends;
    }

    public int getReadStatu() {
        return readStatu;
    }

    public void setReadStatu(int readStatu) {
        this.readStatu = readStatu;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.user, flags);
        dest.writeInt(this.commonFriends);
        dest.writeInt(this.readStatu);
    }

    public SYFriendRecommendModel() {
    }

    protected SYFriendRecommendModel(Parcel in) {
        this.user = in.readParcelable(SYUser.class.getClassLoader());
        this.commonFriends = in.readInt();
        this.readStatu = in.readInt();
    }

    public static final Parcelable.Creator<SYFriendRecommendModel> CREATOR = new Parcelable.Creator<SYFriendRecommendModel>() {
        @Override
        public SYFriendRecommendModel createFromParcel(Parcel source) {
            return new SYFriendRecommendModel(source);
        }

        @Override
        public SYFriendRecommendModel[] newArray(int size) {
            return new SYFriendRecommendModel[size];
        }
    };
}
