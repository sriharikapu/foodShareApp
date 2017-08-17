package com.fengnian.smallyellowo.foodie.bean.results;

import android.os.Parcel;
import android.os.Parcelable;

import com.fengnian.smallyellowo.foodie.bean.publics.SYMyFansModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016-8-30.
 */
public class MyfansAttionResult extends  BaseResult implements Serializable, Parcelable {

    private List<SYMyFansModel>  user;
    private boolean lastPage;//是否是最后一页


    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public List<SYMyFansModel> getUser() {
        return user;
    }

    public void setUser(List<SYMyFansModel> user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.user);
        dest.writeByte(this.lastPage ? (byte) 1 : (byte) 0);
    }

    public MyfansAttionResult() {
    }

    protected MyfansAttionResult(Parcel in) {
        this.user = in.createTypedArrayList(SYMyFansModel.CREATOR);
        this.lastPage = in.readByte() != 0;
    }

    public static final Parcelable.Creator<MyfansAttionResult> CREATOR = new Parcelable.Creator<MyfansAttionResult>() {
        @Override
        public MyfansAttionResult createFromParcel(Parcel source) {
            return new MyfansAttionResult(source);
        }

        @Override
        public MyfansAttionResult[] newArray(int size) {
            return new MyfansAttionResult[size];
        }
    };
}
