package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;

import com.fan.framework.base.XData;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-6-26.
 */

public class SYPhotoPrecisionModel extends XData implements Serializable{
    private String content;
    private float precision;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getPrecision() {
        return precision;
    }

    public void setPrecision(float precision) {
        this.precision = precision;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.content);
        dest.writeFloat(this.precision);
    }

    public SYPhotoPrecisionModel() {
    }

    protected SYPhotoPrecisionModel(Parcel in) {
        super(in);
        this.content = in.readString();
        this.precision = in.readFloat();
    }

    public static final Creator<SYPhotoPrecisionModel> CREATOR = new Creator<SYPhotoPrecisionModel>() {
        @Override
        public SYPhotoPrecisionModel createFromParcel(Parcel source) {
            return new SYPhotoPrecisionModel(source);
        }

        @Override
        public SYPhotoPrecisionModel[] newArray(int size) {
            return new SYPhotoPrecisionModel[size];
        }
    };
}
