package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;

import java.io.Serializable;

public class SYFoodKindModel extends SYBusinessCircleAreaModel implements Serializable {
    private String code;
    private String regionCode;
    private String parentCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public static Creator<SYFoodKindModel> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.code);
        dest.writeString(this.regionCode);
        dest.writeString(this.parentCode);
    }

    public SYFoodKindModel() {
    }

    protected SYFoodKindModel(Parcel in) {
        super(in);
        this.code = in.readString();
        this.regionCode = in.readString();
        this.parentCode = in.readString();
    }

    public static final Creator<SYFoodKindModel> CREATOR = new Creator<SYFoodKindModel>() {
        @Override
        public SYFoodKindModel createFromParcel(Parcel source) {
            return new SYFoodKindModel(source);
        }

        @Override
        public SYFoodKindModel[] newArray(int size) {
            return new SYFoodKindModel[size];
        }
    };
}