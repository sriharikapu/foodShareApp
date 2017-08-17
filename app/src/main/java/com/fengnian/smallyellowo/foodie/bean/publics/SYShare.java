package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

import com.fan.framework.base.XData;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/25.
 */
public class SYShare extends XData implements Parcelable,Serializable {
    String img;
    String content;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SYShare() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.img);
        dest.writeString(this.content);
    }

    protected SYShare(Parcel in) {
        super(in);
        this.img = in.readString();
        this.content = in.readString();
    }

    public static final Creator<SYShare> CREATOR = new Creator<SYShare>() {
        @Override
        public SYShare createFromParcel(Parcel source) {
            return new SYShare(source);
        }

        @Override
        public SYShare[] newArray(int size) {
            return new SYShare[size];
        }
    };
}
