package com.fengnian.smallyellowo.foodie.bean.results;

import android.os.Parcel;
import android.os.Parcelable;

import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;

/**
 * Created by Administrator on 2017-3-24.
 */

public class BangDingPhoneResult extends BaseResult implements Parcelable {

    private SYUser  user;
    private String thirdNickname;

    public SYUser getUser() {
        return user;
    }

    public void setUser(SYUser user) {
        this.user = user;
    }

    public String getThirdNickname() {
        return thirdNickname;
    }

    public void setThirdNickname(String thirdNickname) {
        this.thirdNickname = thirdNickname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.thirdNickname);
    }

    public BangDingPhoneResult() {
    }

    protected BangDingPhoneResult(Parcel in) {
        this.user = in.readParcelable(SYUser.class.getClassLoader());
        this.thirdNickname = in.readString();
    }

    public static final Parcelable.Creator<BangDingPhoneResult> CREATOR = new Parcelable.Creator<BangDingPhoneResult>() {
        @Override
        public BangDingPhoneResult createFromParcel(Parcel source) {
            return new BangDingPhoneResult(source);
        }

        @Override
        public BangDingPhoneResult[] newArray(int size) {
            return new BangDingPhoneResult[size];
        }
    };
}
