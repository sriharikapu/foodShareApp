package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

import com.fan.framework.base.XData;

import java.io.Serializable;

public class SYBusinessCircleAreaModel extends XData implements Parcelable, Serializable {
    private String content;
    private SYImage image;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    private String areaId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SYImage getImage() {
        return image;
    }

    public void setImage(SYImage image) {
        this.image = image;
    }

    public SYBusinessCircleAreaModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.content);
        dest.writeParcelable(this.image, flags);
    }

    protected SYBusinessCircleAreaModel(Parcel in) {
        super(in);
        this.content = in.readString();
        this.image = in.readParcelable(SYImage.class.getClassLoader());
    }

}