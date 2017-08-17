package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

import com.fan.framework.base.XData;

import java.io.Serializable;

public class SYBusiness extends XData implements Parcelable ,Serializable {
    private String merchantId;
    private String distance;
    private String category;
    private int eatId;
    private String detailAddress;
    private String eatNumber;
    private String shopId;
    private int shopType;
    private double perAverage;
    private String merTeleNumber;
    private SYPoi poi;
    private SYImage image;
    public int isOnline;//是否商户在线，1-在线 0-下架


    private String starLevel;

    public String getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(String starLevel) {
        this.starLevel = starLevel;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getEatId() {
        return eatId;
    }

    public void setEatId(int eatId) {
        this.eatId = eatId;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getEatNumber() {
        return eatNumber;
    }

    public void setEatNumber(String eatNumber) {
        this.eatNumber = eatNumber;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public int getShopType() {
        return shopType;
    }

    public void setShopType(int shopType) {
        this.shopType = shopType;
    }

    public double getPerAverage() {
        return perAverage;
    }

    public void setPerAverage(double perAverage) {
        this.perAverage = perAverage;
    }

    public String getMerTeleNumber() {
        return merTeleNumber;
    }

    public void setMerTeleNumber(String merTeleNumber) {
        this.merTeleNumber = merTeleNumber;
    }

    public SYPoi getPoi() {
        return poi;
    }

    public void setPoi(SYPoi poi) {
        this.poi = poi;
    }

    public SYImage getImage() {
        if(image==null) image=new SYImage();
        return image;
    }

    public void setImage(SYImage image) {
        this.image = image;
    }


//    private String id;// 商家id，唯一标示一个商家 是
//    private String name;// 商家名称 否
//    private SYImage headImage;// 商家头像 否
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public SYImage getHeadImage() {
//        return headImage;
//    }
//
//    public void setHeadImage(SYImage headImage) {
//        this.headImage = headImage;
//    }

    public SYBusiness() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.merchantId);
        dest.writeString(this.distance);
        dest.writeString(this.category);
        dest.writeInt(this.eatId);
        dest.writeString(this.detailAddress);
        dest.writeString(this.eatNumber);
        dest.writeString(this.shopId);
        dest.writeInt(this.shopType);
        dest.writeDouble(this.perAverage);
        dest.writeString(this.merTeleNumber);
        dest.writeParcelable(this.poi, flags);
        dest.writeParcelable(this.image, flags);
        dest.writeInt(this.isOnline);
        dest.writeString(this.starLevel);
    }

    protected SYBusiness(Parcel in) {
        super(in);
        this.merchantId = in.readString();
        this.distance = in.readString();
        this.category = in.readString();
        this.eatId = in.readInt();
        this.detailAddress = in.readString();
        this.eatNumber = in.readString();
        this.shopId = in.readString();
        this.shopType = in.readInt();
        this.perAverage = in.readDouble();
        this.merTeleNumber = in.readString();
        this.poi = in.readParcelable(SYPoi.class.getClassLoader());
        this.image = in.readParcelable(SYImage.class.getClassLoader());
        this.isOnline = in.readInt();
        this.starLevel = in.readString();
    }

    public static final Creator<SYBusiness> CREATOR = new Creator<SYBusiness>() {
        @Override
        public SYBusiness createFromParcel(Parcel source) {
            return new SYBusiness(source);
        }

        @Override
        public SYBusiness[] newArray(int size) {
            return new SYBusiness[size];
        }
    };
}
