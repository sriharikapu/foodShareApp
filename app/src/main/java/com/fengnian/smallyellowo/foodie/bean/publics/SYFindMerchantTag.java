package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-7-25.
 */

public class SYFindMerchantTag implements Serializable, Parcelable {
    private String foodSceneTypeName;
    private int number;

    public String getFoodSceneTypeName() {
        return foodSceneTypeName;
    }

    public void setFoodSceneTypeName(String foodSceneTypeName) {
        this.foodSceneTypeName = foodSceneTypeName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.foodSceneTypeName);
        dest.writeInt(this.number);
    }

    public SYFindMerchantTag() {
    }

    protected SYFindMerchantTag(Parcel in) {
        this.foodSceneTypeName = in.readString();
        this.number = in.readInt();
    }

    public static final Parcelable.Creator<SYFindMerchantTag> CREATOR = new Parcelable.Creator<SYFindMerchantTag>() {
        @Override
        public SYFindMerchantTag createFromParcel(Parcel source) {
            return new SYFindMerchantTag(source);
        }

        @Override
        public SYFindMerchantTag[] newArray(int size) {
            return new SYFindMerchantTag[size];
        }
    };
}
