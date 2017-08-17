package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;

/**
 * Created by Administrator on 2017-1-16.
 */

public class RegisterIntent extends IntentData {
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.code);
    }

    public RegisterIntent() {
    }

    protected RegisterIntent(Parcel in) {
        super(in);
        this.code = in.readString();
    }

    public static final Creator<RegisterIntent> CREATOR = new Creator<RegisterIntent>() {
        @Override
        public RegisterIntent createFromParcel(Parcel source) {
            return new RegisterIntent(source);
        }

        @Override
        public RegisterIntent[] newArray(int size) {
            return new RegisterIntent[size];
        }
    };
}
