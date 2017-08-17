package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

import com.fan.framework.base.XData;

import java.io.Serializable;

/**
 * @author Administrator
 */
public class SYBaseContentModel  extends XData implements Parcelable,Serializable {
    private String content = "";// 说点什么 是

    public static final Creator<SYBaseContentModel> CREATOR = new Creator<SYBaseContentModel>() {
        @Override
        public SYBaseContentModel createFromParcel(Parcel in) {
            return new SYBaseContentModel(in);
        }

        @Override
        public SYBaseContentModel[] newArray(int size) {
            return new SYBaseContentModel[size];
        }
    };

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SYBaseContentModel() {
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

    protected SYBaseContentModel(Parcel in) {
        super(in);
        this.content = in.readString();
    }

}
