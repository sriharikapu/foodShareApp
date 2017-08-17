package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017-2-21.
 */

public class SYRelationTalentModel implements Parcelable{
    private SYUser  user;
    private String jingXuanCount="";
    private String fans="";
    private String masteryFood="";

    public SYUser getUser() {
        if (user == null){
            user = new SYUser();
        }
        return user;
    }

    public void setUser(SYUser user) {
        this.user = user;
    }

    public String getJingXuanCount() {
        return jingXuanCount;
    }

    public void setJingXuanCount(String jingXuanCount) {
        this.jingXuanCount = jingXuanCount;
    }

    public String getFans() {
        return fans;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }

    public String getMasteryFood() {
        return masteryFood;
    }

    public void setMasteryFood(String masteryFood) {
        this.masteryFood = masteryFood;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.jingXuanCount);
        dest.writeString(this.fans);
        dest.writeString(this.masteryFood);
    }

    public SYRelationTalentModel() {
    }

    protected SYRelationTalentModel(Parcel in) {
        this.user = in.readParcelable(SYUser.class.getClassLoader());
        this.jingXuanCount = in.readString();
        this.fans = in.readString();
        this.masteryFood = in.readString();
    }

    public static final Creator<SYRelationTalentModel> CREATOR = new Creator<SYRelationTalentModel>() {
        @Override
        public SYRelationTalentModel createFromParcel(Parcel source) {
            return new SYRelationTalentModel(source);
        }

        @Override
        public SYRelationTalentModel[] newArray(int size) {
            return new SYRelationTalentModel[size];
        }
    };
}
