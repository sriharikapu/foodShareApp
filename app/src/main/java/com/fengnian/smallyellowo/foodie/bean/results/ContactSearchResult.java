package com.fengnian.smallyellowo.foodie.bean.results;

import android.os.Parcel;
import android.os.Parcelable;

import com.fengnian.smallyellowo.foodie.bean.publics.ContactSearchBean;

/**
 * Created by Administrator on 2016-9-12.
 */

public class ContactSearchResult extends BaseResult implements Parcelable {

    private String  message;
    private String  restate;
    private ContactSearchBean redata;

    private int userType;

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRestate() {
        return restate;
    }

    public void setRestate(String restate) {
        this.restate = restate;
    }

    public ContactSearchBean getRedata() {
        return redata;
    }

    public void setRedata(ContactSearchBean redata) {
        this.redata = redata;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeString(this.restate);
        dest.writeParcelable(this.redata, flags);
    }

    public ContactSearchResult() {
    }

    protected ContactSearchResult(Parcel in) {
        this.message = in.readString();
        this.restate = in.readString();
        this.redata = in.readParcelable(ContactSearchBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ContactSearchResult> CREATOR = new Parcelable.Creator<ContactSearchResult>() {
        @Override
        public ContactSearchResult createFromParcel(Parcel source) {
            return new ContactSearchResult(source);
        }

        @Override
        public ContactSearchResult[] newArray(int size) {
            return new ContactSearchResult[size];
        }
    };
}
