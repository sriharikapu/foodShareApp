package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016-9-7.
 */

public class FoodListBean  implements Parcelable{
    private String imgUrl;
    private String type;
    private String counts;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCounts() {
        return counts;
    }

    public void setCounts(String counts) {
        this.counts = counts;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imgUrl);
        dest.writeString(this.type);
        dest.writeString(this.counts);
    }

    public FoodListBean() {
    }

    protected FoodListBean(Parcel in) {
        this.imgUrl = in.readString();
        this.type = in.readString();
        this.counts = in.readString();
    }

    public static final Creator<FoodListBean> CREATOR = new Creator<FoodListBean>() {
        @Override
        public FoodListBean createFromParcel(Parcel source) {
            return new FoodListBean(source);
        }

        @Override
        public FoodListBean[] newArray(int size) {
            return new FoodListBean[size];
        }
    };
}
