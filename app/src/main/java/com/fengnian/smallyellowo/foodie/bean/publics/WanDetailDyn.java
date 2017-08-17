package com.fengnian.smallyellowo.foodie.bean.publics;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016-11-12.
 */

public class WanDetailDyn implements Parcelable {
    private String   heardImage ;
    private String   userName ;
    private String   publishTime ;
    private String   foodImage ;
    private String    foodContent;
    private String    recordId;
    private String    pubType;
    private String   cusId ;
    private String   elite ;
    private String    attention;

    private List<String> foodImageArray; //美食速記列表

    private  int releaseTemplateType; //模板类型

    private  SYUser user;

    private  int isOnline;//商户是否在线，1-在线 0-下架

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public int getReleaseTemplateType() {
        return releaseTemplateType;
    }

    public void setReleaseTemplateType(int releaseTemplateType) {
        this.releaseTemplateType = releaseTemplateType;
    }

    public SYUser getUser() {
        return user;
    }

    public void setUser(SYUser user) {
        this.user = user;
    }



    public List<String> getFoodImageArray() {
        return foodImageArray;
    }

    public void setFoodImageArray(List<String> foodImageArray) {
        this.foodImageArray = foodImageArray;
    }

    public int getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(int starLevel) {
        this.starLevel = starLevel;
    }

    private int    starLevel;

    public String getHeardImage() {
        return heardImage;
    }

    public void setHeardImage(String heardImage) {
        this.heardImage = heardImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public String getFoodContent() {
        return foodContent;
    }

    public void setFoodContent(String foodContent) {
        this.foodContent = foodContent;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getPubType() {
        return pubType;
    }

    public void setPubType(String pubType) {
        this.pubType = pubType;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getElite() {
        return elite;
    }

    public void setElite(String elite) {
        this.elite = elite;
    }

    public String getAttention() {
        return attention;
    }

    public String pullStartLevelString() {
        switch (starLevel) {
            case 0:
                return "美食评分";
            case 1:
                return "差评";
            case 2:
                return "一般";
            case 3:
                return "推荐";
            case 4:
                return "极力推荐";
        }
        return "";
    }
    public void setAttention(String attention) {
        this.attention = attention;
    }

    public WanDetailDyn() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.heardImage);
        dest.writeString(this.userName);
        dest.writeString(this.publishTime);
        dest.writeString(this.foodImage);
        dest.writeString(this.foodContent);
        dest.writeString(this.recordId);
        dest.writeString(this.pubType);
        dest.writeString(this.cusId);
        dest.writeString(this.elite);
        dest.writeString(this.attention);
        dest.writeStringList(this.foodImageArray);
        dest.writeInt(this.releaseTemplateType);
        dest.writeParcelable(this.user, flags);
        dest.writeInt(this.isOnline);
        dest.writeInt(this.starLevel);
    }

    protected WanDetailDyn(Parcel in) {
        this.heardImage = in.readString();
        this.userName = in.readString();
        this.publishTime = in.readString();
        this.foodImage = in.readString();
        this.foodContent = in.readString();
        this.recordId = in.readString();
        this.pubType = in.readString();
        this.cusId = in.readString();
        this.elite = in.readString();
        this.attention = in.readString();
        this.foodImageArray = in.createStringArrayList();
        this.releaseTemplateType = in.readInt();
        this.user = in.readParcelable(SYUser.class.getClassLoader());
        this.isOnline = in.readInt();
        this.starLevel = in.readInt();
    }

    public static final Creator<WanDetailDyn> CREATOR = new Creator<WanDetailDyn>() {
        @Override
        public WanDetailDyn createFromParcel(Parcel source) {
            return new WanDetailDyn(source);
        }

        @Override
        public WanDetailDyn[] newArray(int size) {
            return new WanDetailDyn[size];
        }
    };
}
