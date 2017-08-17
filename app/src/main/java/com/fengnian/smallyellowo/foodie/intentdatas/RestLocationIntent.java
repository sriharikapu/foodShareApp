package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;

/**
 * Created by Administrator on 2016-9-22.
 */
public class RestLocationIntent extends  IntentData {
    double lat;
    double lng;
    String address;
    String name;

    public String getAddress() {
        return address;
    }

    public RestLocationIntent setAddress(String address) {
        this.address = address;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public RestLocationIntent setLng(double lng) {
        this.lng = lng;
        return this;
    }

    public String getName() {
        return name;
    }

    public RestLocationIntent setName(String name) {
        this.name = name;
        return this;
    }

    public RestLocationIntent setLat(double lat) {
        this.lat = lat;
        return this;

    }

    public RestLocationIntent() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeString(this.name);
        dest.writeString(this.address);
    }

    protected RestLocationIntent(Parcel in) {
        super(in);
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.name = in.readString();
        this.address = in.readString();
    }

    public static final Creator<RestLocationIntent> CREATOR = new Creator<RestLocationIntent>() {
        @Override
        public RestLocationIntent createFromParcel(Parcel source) {
            return new RestLocationIntent(source);
        }

        @Override
        public RestLocationIntent[] newArray(int size) {
            return new RestLocationIntent[size];
        }
    };
}
