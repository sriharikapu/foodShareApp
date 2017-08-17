package com.fengnian.smallyellowo.foodie.bean.publics;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class SYShorthandFood extends SYBaseTextFood implements Parcelable ,Serializable {
    private String content;// 速记描述 是

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SYShorthandFood() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.content);
    }

    protected SYShorthandFood(Parcel in) {
        super(in);
        this.content = in.readString();
    }

    public static final Creator<SYShorthandFood> CREATOR = new Creator<SYShorthandFood>() {
        @Override
        public SYShorthandFood createFromParcel(Parcel source) {
            return new SYShorthandFood(source);
        }

        @Override
        public SYShorthandFood[] newArray(int size) {
            return new SYShorthandFood[size];
        }
    };
}
