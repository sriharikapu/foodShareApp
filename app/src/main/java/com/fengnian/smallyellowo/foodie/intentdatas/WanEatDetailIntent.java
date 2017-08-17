package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;
import android.os.StrictMode;

import com.fengnian.smallyellowo.foodie.bean.publics.SYBusiness;

/**
 * Created by Administrator on 2016-11-11.
 */

public class WanEatDetailIntent extends IntentData  {

    private SYBusiness bus;

    private String BusinessId; //商户id
    private String RecordId;//想吃记录ID
    private String eatId; // 想吃id

    public SYBusiness getBus() {
        return bus;
    }

    public String getBusinessId() {
        return BusinessId;
    }

    public void setBusinessId(String businessId) {
        BusinessId = businessId;
    }

    public String getRecordId() {
        return RecordId;
    }

    public void setRecordId(String recordId) {
        RecordId = recordId;
    }

    public void setBus(SYBusiness bus) {
        this.bus = bus;
    }

    public String getEatId() {
        return eatId;
    }

    public void setEatId(String eatId) {
        this.eatId = eatId;
    }

    public WanEatDetailIntent() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.bus, flags);
        dest.writeString(this.BusinessId);
        dest.writeString(this.RecordId);
        dest.writeString(this.eatId);
    }

    protected WanEatDetailIntent(Parcel in) {
        super(in);
        this.bus = in.readParcelable(SYBusiness.class.getClassLoader());
        this.BusinessId = in.readString();
        this.RecordId = in.readString();
        this.eatId = in.readString();
    }

    public static final Creator<WanEatDetailIntent> CREATOR = new Creator<WanEatDetailIntent>() {
        @Override
        public WanEatDetailIntent createFromParcel(Parcel source) {
            return new WanEatDetailIntent(source);
        }

        @Override
        public WanEatDetailIntent[] newArray(int size) {
            return new WanEatDetailIntent[size];
        }
    };
}
