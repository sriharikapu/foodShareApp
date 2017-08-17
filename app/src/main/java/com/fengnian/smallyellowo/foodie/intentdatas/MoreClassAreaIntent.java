package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;

/**
 * Created by Administrator on 2016-9-9.
 */
public class MoreClassAreaIntent extends IntentData {
    public boolean isArea() {
        return isArea;
    }

    public MoreClassAreaIntent setArea(boolean area) {
        isArea = area;
        return this;
    }

    boolean isArea;

    public MoreClassAreaIntent() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte(this.isArea ? (byte) 1 : (byte) 0);
    }

    protected MoreClassAreaIntent(Parcel in) {
        super(in);
        this.isArea = in.readByte() != 0;
    }

    public static final Creator<MoreClassAreaIntent> CREATOR = new Creator<MoreClassAreaIntent>() {
        @Override
        public MoreClassAreaIntent createFromParcel(Parcel source) {
            return new MoreClassAreaIntent(source);
        }

        @Override
        public MoreClassAreaIntent[] newArray(int size) {
            return new MoreClassAreaIntent[size];
        }
    };
}
