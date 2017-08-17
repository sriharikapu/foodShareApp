package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

import com.fan.framework.base.XData;

import java.io.Serializable;

public class SYChoiceModel extends XData implements Parcelable, Serializable {

    private SYImage backImage;
    private String title;
    private String detailDescription;


    private long createTime;

    private boolean bFav;

    public int getScanTimes() {
        return scanTimes;
    }

    public void setScanTimes(int scanTimes) {
        this.scanTimes = scanTimes;
    }

    private int scanTimes;
    private String htmlUrl;
    private SYShare share;

    public static Creator<SYChoiceModel> getCREATOR() {
        return CREATOR;
    }
    public SYImage getBackImage() {
        return backImage;
    }

    public void setBackImage(SYImage backImage) {
        this.backImage = backImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetailDescription() {
        return detailDescription;
    }

    public void setDetailDescription(String detailDescription) {
        this.detailDescription = detailDescription;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isbFav() {
        return bFav;
    }

    public void setbFav(boolean bFav) {
        this.bFav = bFav;
    }


    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public SYShare getShare() {
        return share;
    }

    public void setShare(SYShare share) {
        this.share = share;
    }

    public SYChoiceModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.backImage, flags);
        dest.writeString(this.title);
        dest.writeString(this.detailDescription);
        dest.writeLong(this.createTime);
        dest.writeByte(this.bFav ? (byte) 1 : (byte) 0);
        dest.writeInt(this.scanTimes);
        dest.writeString(this.htmlUrl);
        dest.writeParcelable(this.share, flags);
    }

    protected SYChoiceModel(Parcel in) {
        super(in);
        this.backImage = in.readParcelable(SYImage.class.getClassLoader());
        this.title = in.readString();
        this.detailDescription = in.readString();
        this.createTime = in.readLong();
        this.bFav = in.readByte() != 0;
        this.scanTimes = in.readInt();
        this.htmlUrl = in.readString();
        this.share = in.readParcelable(SYShare.class.getClassLoader());
    }

    public static final Creator<SYChoiceModel> CREATOR = new Creator<SYChoiceModel>() {
        @Override
        public SYChoiceModel createFromParcel(Parcel source) {
            return new SYChoiceModel(source);
        }

        @Override
        public SYChoiceModel[] newArray(int size) {
            return new SYChoiceModel[size];
        }
    };
}