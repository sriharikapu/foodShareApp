package com.fengnian.smallyellowo.foodie.bean.results;

import android.os.Parcel;
import android.os.Parcelable;

import com.fengnian.smallyellowo.foodie.bean.publics.SYFriendRecommendModel;

import java.util.List;

/**
 * Created by Administrator on 2017-2-21.
 */

public class SYFriendRecommendResult extends BaseResult implements Parcelable {

    private String userFansCount="";
    private String  userAttentionCount="";

    private List<SYFriendRecommendModel> common;

    private boolean hasMoreFriends;//是否有更多好友推荐（true：是，false：否）


    public String getUserFansCount() {
        return userFansCount;
    }

    public void setUserFansCount(String userFansCount) {
        this.userFansCount = userFansCount;
    }

    public String getUserAttentionCount() {
        return userAttentionCount;
    }

    public void setUserAttentionCount(String userAttentionCount) {
        this.userAttentionCount = userAttentionCount;
    }

    public List<SYFriendRecommendModel> getCommon() {
        return common;
    }

    public void setCommon(List<SYFriendRecommendModel> common) {
        this.common = common;
    }

    public boolean isHasMoreFriends() {
        return hasMoreFriends;
    }

    public void setHasMoreFriends(boolean hasMoreFriends) {
        this.hasMoreFriends = hasMoreFriends;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userFansCount);
        dest.writeString(this.userAttentionCount);
        dest.writeTypedList(this.common);
        dest.writeByte(this.hasMoreFriends ? (byte) 1 : (byte) 0);
    }

    public SYFriendRecommendResult() {
    }

    protected SYFriendRecommendResult(Parcel in) {
        this.userFansCount = in.readString();
        this.userAttentionCount = in.readString();
        this.common = in.createTypedArrayList(SYFriendRecommendModel.CREATOR);
        this.hasMoreFriends = in.readByte() != 0;
    }

    public static final Parcelable.Creator<SYFriendRecommendResult> CREATOR = new Parcelable.Creator<SYFriendRecommendResult>() {
        @Override
        public SYFriendRecommendResult createFromParcel(Parcel source) {
            return new SYFriendRecommendResult(source);
        }

        @Override
        public SYFriendRecommendResult[] newArray(int size) {
            return new SYFriendRecommendResult[size];
        }
    };
}
