package com.fengnian.smallyellowo.foodie.bean.publish;

import android.os.Parcel;
import android.os.Parcelable;

import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;

/**
 * Created by Administrator on 2017-4-18.
 */

public class WantEatInfoAndfoodRecord implements Parcelable {
    private  String publishTime;
    private String foodImage;
    private  String  foodContent;
    private int   recordId;

    private  String  pubType;
    private  String  cusId;
    private  int  elite;
    private  int   attention;
    private  int  starLevel;
    private  int  userType;
    private  int  releaseTemplateType;
    private SYUser  user;

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

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
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

    public int getElite() {
        return elite;
    }

    public void setElite(int elite) {
        this.elite = elite;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public int getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(int starLevel) {
        this.starLevel = starLevel;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.publishTime);
        dest.writeString(this.foodImage);
        dest.writeString(this.foodContent);
        dest.writeInt(this.recordId);
        dest.writeString(this.pubType);
        dest.writeString(this.cusId);
        dest.writeInt(this.elite);
        dest.writeInt(this.attention);
        dest.writeInt(this.starLevel);
        dest.writeInt(this.userType);
        dest.writeInt(this.releaseTemplateType);
        dest.writeParcelable(this.user, flags);
    }

    public WantEatInfoAndfoodRecord() {
    }

    protected WantEatInfoAndfoodRecord(Parcel in) {
        this.publishTime = in.readString();
        this.foodImage = in.readString();
        this.foodContent = in.readString();
        this.recordId = in.readInt();
        this.pubType = in.readString();
        this.cusId = in.readString();
        this.elite = in.readInt();
        this.attention = in.readInt();
        this.starLevel = in.readInt();
        this.userType = in.readInt();
        this.releaseTemplateType = in.readInt();
        this.user = in.readParcelable(SYUser.class.getClassLoader());
    }

    public static final Parcelable.Creator<WantEatInfoAndfoodRecord> CREATOR = new Parcelable.Creator<WantEatInfoAndfoodRecord>() {
        @Override
        public WantEatInfoAndfoodRecord createFromParcel(Parcel source) {
            return new WantEatInfoAndfoodRecord(source);
        }

        @Override
        public WantEatInfoAndfoodRecord[] newArray(int size) {
            return new WantEatInfoAndfoodRecord[size];
        }
    };
}
