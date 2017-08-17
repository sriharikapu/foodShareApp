package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;

import com.fengnian.smallyellowo.foodie.bean.publics.SYFindMerchantInfo;

/**
 * Created by Administrator on 2016-9-7.
 */
public class RestInfoIntent extends IntentData {
    private float star;

    public SYFindMerchantInfo getInfo() {
        return info;
    }

    public void setInfo(SYFindMerchantInfo info) {
        this.info = info;
    }

    SYFindMerchantInfo info;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public RestInfoIntent() {
    }

    public void setStar(float star) {
        this.star = star;
    }

    public float getStar() {
        if (info != null) {
            return info.getStartLevel();
        }
        return star;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeFloat(this.star);
        dest.writeParcelable(this.info, flags);
        dest.writeString(this.id);
    }

    protected RestInfoIntent(Parcel in) {
        super(in);
        this.star = in.readFloat();
        this.info = in.readParcelable(SYFindMerchantInfo.class.getClassLoader());
        this.id = in.readString();
    }

    public static final Creator<RestInfoIntent> CREATOR = new Creator<RestInfoIntent>() {
        @Override
        public RestInfoIntent createFromParcel(Parcel source) {
            return new RestInfoIntent(source);
        }

        @Override
        public RestInfoIntent[] newArray(int size) {
            return new RestInfoIntent[size];
        }
    };
}
