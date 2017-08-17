package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;

import com.fan.framework.base.XData;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-6-26.
 */

public class SYFoodTagModel extends XData implements Serializable {
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.content);
    }

    public SYFoodTagModel() {
    }

    public SYFoodTagModel(String content) {
        this.content = content;
        setId("0");
    }

    protected SYFoodTagModel(Parcel in) {
        super(in);
        this.content = in.readString();
    }

    public static final Creator<SYFoodTagModel> CREATOR = new Creator<SYFoodTagModel>() {
        @Override
        public SYFoodTagModel createFromParcel(Parcel source) {
            return new SYFoodTagModel(source);
        }

        @Override
        public SYFoodTagModel[] newArray(int size) {
            return new SYFoodTagModel[size];
        }
    };
}
