package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016-8-5.
 */
public class IntentData implements Parcelable {
    private int requestCode = -1;

    public int getRequestCode() {
        return requestCode;
    }

    public IntentData setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public IntentData() {
        super();
    }

    public IntentData(int requestCode) {
        this.requestCode = requestCode;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.requestCode);
    }

    protected IntentData(Parcel in) {
        this.requestCode = in.readInt();
    }

    public static final Creator<IntentData> CREATOR = new Creator<IntentData>() {
        @Override
        public IntentData createFromParcel(Parcel source) {
            return new IntentData(source);
        }

        @Override
        public IntentData[] newArray(int size) {
            return new IntentData[size];
        }
    };
}
