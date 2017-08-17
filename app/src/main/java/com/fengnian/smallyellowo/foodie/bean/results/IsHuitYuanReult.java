package com.fengnian.smallyellowo.foodie.bean.results;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017-2-23.
 */

public class IsHuitYuanReult extends BaseResult implements Parcelable {
    private int userType; //0普通用户1俱乐部会员

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userType);
    }

    public IsHuitYuanReult() {
    }

    protected IsHuitYuanReult(Parcel in) {
        this.userType = in.readInt();
    }

    public static final Parcelable.Creator<IsHuitYuanReult> CREATOR = new Parcelable.Creator<IsHuitYuanReult>() {
        @Override
        public IsHuitYuanReult createFromParcel(Parcel source) {
            return new IsHuitYuanReult(source);
        }

        @Override
        public IsHuitYuanReult[] newArray(int size) {
            return new IsHuitYuanReult[size];
        }
    };
}
