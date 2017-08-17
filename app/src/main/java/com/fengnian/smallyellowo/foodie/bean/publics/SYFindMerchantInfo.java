package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

import com.fan.framework.base.XData;

import java.io.Serializable;

/**
 * Created by Administrator on 2016-9-2.
 */
public class SYFindMerchantInfo extends XData implements Parcelable, Serializable {
    private String merchantUid;// String 店铺id 是
    private SYImage merchantImage;// SYImage 类型 店铺图片信息 否
    private String merchantName;// String 店铺名称 否
    private String merchantDistance;// String 距离 否
    private boolean merchantIsWant;// Bool 是否想吃 否
    private boolean merchantIsRelation;// Bool 是否在关系人里边 否
    private boolean merchantIsDa;// Bool 是否在达人荐里边 否
    private String merchantKind;// String 店铺类型 否
    private float merchantPrice;// String 店铺人均（价格） 否
    private String merchantAddress;// String 店铺详细地址 否
    private String friendShares;// String 多少个圈友分享 否
    private String merchantArea;// String 商圈 否
    private String merchantPhone;// String 商户电话 否
    private MPoi merchantPoi;

    private String feedId;
    private boolean merchantIsTu;// 小黄推荐
    private boolean merchantIsXiang;//圈友推荐
    private boolean merchantIsPin;//评分高
    private boolean merchantIsJin;//距离最近
    private SYImage userHeaderImage;//分享人的头像
    private String createTime;//分享时间
    private String userName;//分享人昵称


    public float getStartLevel() {
        return startLevel;
    }

    public void setStartLevel(float startLevel) {
        this.startLevel = startLevel;
    }

    private float startLevel;

    public MPoi getMerchantPoi() {
        if(merchantPoi==null)
            merchantPoi=new MPoi();
        return merchantPoi;
    }

    public void setMerchantPoi(MPoi merchantPoi) {
        this.merchantPoi = merchantPoi;
    }

    public boolean isMerchantIsTu() {
        return merchantIsTu;
    }

    public void setMerchantIsTu(boolean merchantIsTu) {
        this.merchantIsTu = merchantIsTu;
    }

    public boolean isMerchantIsXiang() {
        return merchantIsXiang;
    }

    public void setMerchantIsXiang(boolean merchantIsXiang) {
        this.merchantIsXiang = merchantIsXiang;
    }

    public boolean isMerchantIsPin() {
        return merchantIsPin;
    }

    public void setMerchantIsPin(boolean merchantIsPin) {
        this.merchantIsPin = merchantIsPin;
    }

    public boolean isMerchantIsJin() {
        return merchantIsJin;
    }

    public void setMerchantIsJin(boolean merchantIsJin) {
        this.merchantIsJin = merchantIsJin;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public SYImage getUserHeaderImage() {
        return userHeaderImage;
    }

    public void setUserHeaderImage(SYImage userHeaderImaged) {
        this.userHeaderImage = userHeaderImaged;
    }

    public static class MPoi implements Parcelable, Serializable {

        /**
         * type : 0
         * latitude : 40.010679
         * longitude : 116.466871
         */

        private int type;
        private double latitude;
        private double longitude;

        public int getType() {
            return type;
        }

        public static Creator<MPoi> getCREATOR() {
            return CREATOR;
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

        public void setType(int type) {
            this.type = type;
        }

        public MPoi() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.type);
            dest.writeDouble(this.latitude);
            dest.writeDouble(this.longitude);
        }

        protected MPoi(Parcel in) {
            this.type = in.readInt();
            this.latitude = in.readDouble();
            this.longitude = in.readDouble();
        }

        public static final Creator<MPoi> CREATOR = new Creator<MPoi>() {
            @Override
            public MPoi createFromParcel(Parcel source) {
                return new MPoi(source);
            }

            @Override
            public MPoi[] newArray(int size) {
                return new MPoi[size];
            }
        };
    }


    public SYImage getMerchantImage() {
        return merchantImage;
    }

    public void setMerchantImage(SYImage merchantImage) {
        this.merchantImage = merchantImage;
    }

    public static Creator<SYFindMerchantInfo> getCREATOR() {
        return CREATOR;
    }

    public String getMerchantUid() {
        return merchantUid;
    }

    public void setMerchantUid(String merchantUid) {
        this.merchantUid = merchantUid;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantDistance() {
        return merchantDistance;
    }

    public void setMerchantDistance(String merchantDistance) {
        this.merchantDistance = merchantDistance;
    }

    public boolean isMerchantIsWant() {
        return merchantIsWant;
    }

    public void setMerchantIsWant(boolean merchantIsWant) {
        this.merchantIsWant = merchantIsWant;
    }

    public boolean isMerchantIsRelation() {
        return merchantIsRelation;
    }

    public void setMerchantIsRelation(boolean merchantIsRelation) {
        this.merchantIsRelation = merchantIsRelation;
    }

    public boolean isMerchantIsDa() {
        return merchantIsDa;
    }

    public void setMerchantIsDa(boolean merchantIsDa) {
        this.merchantIsDa = merchantIsDa;
    }

    public String getMerchantKind() {
        return merchantKind;
    }

    public void setMerchantKind(String merchantKind) {
        this.merchantKind = merchantKind;
    }

    public float getMerchantPrice() {
        return merchantPrice;
    }

    public void setMerchantPrice(float merchantPrice) {
        this.merchantPrice = merchantPrice;
    }

    public String getMerchantAddress() {
        return merchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
        this.merchantAddress = merchantAddress;
    }

    public String getMerchantArea() {
        return merchantArea;
    }

    public void setMerchantArea(String merchantArea) {
        this.merchantArea = merchantArea;
    }

    public String getMerchantPhone() {
        return merchantPhone;
    }

    public void setMerchantPhone(String merchantPhone) {
        this.merchantPhone = merchantPhone;
    }

    public SYFindMerchantInfo() {
    }

    public String getFriendShares() {
        return friendShares;
    }

    public void setFriendShares(String friendShares) {
        this.friendShares = friendShares;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.merchantUid);
        dest.writeParcelable(this.merchantImage, flags);
        dest.writeString(this.merchantName);
        dest.writeString(this.merchantDistance);
        dest.writeByte(this.merchantIsWant ? (byte) 1 : (byte) 0);
        dest.writeByte(this.merchantIsRelation ? (byte) 1 : (byte) 0);
        dest.writeByte(this.merchantIsDa ? (byte) 1 : (byte) 0);
        dest.writeString(this.merchantKind);
        dest.writeFloat(this.merchantPrice);
        dest.writeString(this.merchantAddress);
        dest.writeString(this.friendShares);
        dest.writeString(this.merchantArea);
        dest.writeString(this.merchantPhone);
        dest.writeParcelable(this.merchantPoi, flags);
        dest.writeFloat(this.startLevel);
        dest.writeString(this.feedId);
        dest.writeByte(this.merchantIsTu ? (byte) 1 : (byte) 0);
        dest.writeByte(this.merchantIsXiang ? (byte) 1 : (byte) 0);
        dest.writeByte(this.merchantIsPin ? (byte) 1 : (byte) 0);
        dest.writeByte(this.merchantIsJin ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.userHeaderImage, flags);
        dest.writeString(this.createTime);
        dest.writeString(this.userName);
    }

    protected SYFindMerchantInfo(Parcel in) {
        super(in);
        this.merchantUid = in.readString();
        this.merchantImage = in.readParcelable(SYImage.class.getClassLoader());
        this.merchantName = in.readString();
        this.merchantDistance = in.readString();
        this.merchantIsWant = in.readByte() != 0;
        this.merchantIsRelation = in.readByte() != 0;
        this.merchantIsDa = in.readByte() != 0;
        this.merchantKind = in.readString();
        this.merchantPrice = in.readFloat();
        this.merchantAddress = in.readString();
        this.friendShares = in.readString();
        this.merchantArea = in.readString();
        this.merchantPhone = in.readString();
        this.merchantPoi = in.readParcelable(MPoi.class.getClassLoader());
        this.startLevel = in.readFloat();
        this.feedId = in.readString();
        this.merchantIsTu = in.readByte() != 0;
        this.merchantIsXiang = in.readByte() != 0;
        this.merchantIsPin = in.readByte() != 0;
        this.merchantIsJin = in.readByte() != 0;
        this.userHeaderImage = in.readParcelable(SYImage.class.getClassLoader());
        this.createTime = in.readString();
        this.userName = in.readString();
    }

    public static final Creator<SYFindMerchantInfo> CREATOR = new Creator<SYFindMerchantInfo>() {
        @Override
        public SYFindMerchantInfo createFromParcel(Parcel source) {
            return new SYFindMerchantInfo(source);
        }

        @Override
        public SYFindMerchantInfo[] newArray(int size) {
            return new SYFindMerchantInfo[size];
        }
    };
}
