package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017-3-14.
 */

public class SYMyFansModel implements Parcelable {
    private SYUser user;
    private  String attentionTimeStr="";

    private  String  status="0";//是否已经阅读(1代表未阅读,0代表已阅读)
    private String attentionID="0";//纪录ID

    public SYUser getUser() {
        return user;
    }

    public void setUser(SYUser user) {
        this.user = user;
    }

    public String getAttentionTimeStr() {
        return attentionTimeStr;
    }

    public void setAttentionTimeStr(String attentionTimeStr) {
        this.attentionTimeStr = attentionTimeStr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAttentionID() {
        return attentionID;
    }

    public void setAttentionID(String attentionID) {
        this.attentionID = attentionID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.attentionTimeStr);
        dest.writeString(this.status);
        dest.writeString(this.attentionID);
    }

    public SYMyFansModel() {
    }

    protected SYMyFansModel(Parcel in) {
        this.user = in.readParcelable(SYUser.class.getClassLoader());
        this.attentionTimeStr = in.readString();
        this.status = in.readString();
        this.attentionID = in.readString();
    }

    public static final Parcelable.Creator<SYMyFansModel> CREATOR = new Parcelable.Creator<SYMyFansModel>() {
        @Override
        public SYMyFansModel createFromParcel(Parcel source) {
            return new SYMyFansModel(source);
        }

        @Override
        public SYMyFansModel[] newArray(int size) {
            return new SYMyFansModel[size];
        }
    };
}
