package com.fengnian.smallyellowo.foodie.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;

/**
 * Created by Administrator on 2016-11-30.
 */

public class PersionSYfeedC extends SYFeed implements Parcelable {

    public boolean sharedToAct; //是否分享

    protected PersionSYfeedC(Parcel in) {
        super(in);
        sharedToAct = in.readByte() != 0;
    }

    public PersionSYfeedC() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte((byte) (sharedToAct ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PersionSYfeedC> CREATOR = new Creator<PersionSYfeedC>() {
        @Override
        public PersionSYfeedC createFromParcel(Parcel in) {
            return new PersionSYfeedC(in);
        }

        @Override
        public PersionSYfeedC[] newArray(int size) {
            return new PersionSYfeedC[size];
        }
    };

    public boolean isSharedToAct() {
        return sharedToAct;
    }

    public void setSharedToAct(boolean sharedToAct) {
        this.sharedToAct = sharedToAct;
    }
}
