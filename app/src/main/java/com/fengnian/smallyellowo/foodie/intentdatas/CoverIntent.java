package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;

/**
 * Created by Administrator on 2016-11-24.
 */

public class CoverIntent extends IntentData {
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public CoverIntent() {
    }

    protected CoverIntent(Parcel in) {
        super(in);
    }

    public static final Creator<CoverIntent> CREATOR = new Creator<CoverIntent>() {
        @Override
        public CoverIntent createFromParcel(Parcel source) {
            return new CoverIntent(source);
        }

        @Override
        public CoverIntent[] newArray(int size) {
            return new CoverIntent[size];
        }
    };
}
