package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

import com.fan.framework.base.XData;

import java.io.Serializable;

/**
 * 地理位置坐标信息
 *
 * @author Administrator
 */
public class SYPoi extends XData implements Parcelable,Serializable {
    private String title;// String poi名称 否
    private String address;// String poi地址 否
    private String tel;// String 电话 否
    private String category;// String poi分类 否
    private int type;// Int poi类型，值说明: 0:普通poi 1:公交站 2:地铁站 3:公交线路 4:行政区划 否
    private double latitude;// Float 纬度 否
    private double longitude;// Float 经度 否
    private String province;// String 省 否
    private String city;// String 市 否
    private String region;// String 区域 否
    private String adCode;// String 行政区划代码 否
    private int isCustom;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public SYPoi() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.title);
        dest.writeString(this.address);
        dest.writeString(this.tel);
        dest.writeString(this.category);
        dest.writeInt(this.type);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.region);
        dest.writeString(this.adCode);
        dest.writeInt(this.isCustom);
    }

    protected SYPoi(Parcel in) {
        super(in);
        this.title = in.readString();
        this.address = in.readString();
        this.tel = in.readString();
        this.category = in.readString();
        this.type = in.readInt();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.province = in.readString();
        this.city = in.readString();
        this.region = in.readString();
        this.adCode = in.readString();
        this.isCustom = in.readInt();
    }

    public static final Creator<SYPoi> CREATOR = new Creator<SYPoi>() {
        @Override
        public SYPoi createFromParcel(Parcel source) {
            return new SYPoi(source);
        }

        @Override
        public SYPoi[] newArray(int size) {
            return new SYPoi[size];
        }
    };

    public void setIsCustom(int isCustom) {
        this.isCustom = isCustom;
    }

    public int getIsCustom() {
        return isCustom;
    }
}
