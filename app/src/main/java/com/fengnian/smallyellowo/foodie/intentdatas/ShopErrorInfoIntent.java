package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;

/**
 * Created by Administrator on 2017-3-28.
 */

public class ShopErrorInfoIntent extends IntentData {

    private String  address="";
    private  String phone="";
    private String  name="";
    private double  lat;
    private double  lng;
    private  String shopid;

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    private  int  error_type;//1商户营业问题2商户位置问题3商户信息问题

    private  String  error_type_childe;

    public String getError_type_childe() {
        return error_type_childe;
    }

    public void setError_type_childe(String error_type_childe) {
        this.error_type_childe = error_type_childe;
    }

    public int getError_type() {
        return error_type;
    }

    public void setError_type(int error_type) {
        this.error_type = error_type;
    }

    public static Creator<ShopErrorInfoIntent> getCREATOR() {
        return CREATOR;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }


    public ShopErrorInfoIntent() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.address);
        dest.writeString(this.phone);
        dest.writeString(this.name);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeString(this.shopid);
        dest.writeInt(this.error_type);
        dest.writeString(this.error_type_childe);
    }

    protected ShopErrorInfoIntent(Parcel in) {
        super(in);
        this.address = in.readString();
        this.phone = in.readString();
        this.name = in.readString();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.shopid = in.readString();
        this.error_type = in.readInt();
        this.error_type_childe = in.readString();
    }

    public static final Creator<ShopErrorInfoIntent> CREATOR = new Creator<ShopErrorInfoIntent>() {
        @Override
        public ShopErrorInfoIntent createFromParcel(Parcel source) {
            return new ShopErrorInfoIntent(source);
        }

        @Override
        public ShopErrorInfoIntent[] newArray(int size) {
            return new ShopErrorInfoIntent[size];
        }
    };
}
