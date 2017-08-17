package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;

import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;

public class DynamicDetailIntent extends com.fengnian.smallyellowo.foodie.intentdatas.IntentData {
    private String id;
    private boolean preview;
    private boolean mineMode;
    //isResource：想吃来源，0-无来源；1-首页达人荐；2-动态列表；3-动态详情
    // 4.pgc美食志h5详情 5.发现页商户详情 7.朋友聚餐-批量添加想吃 9非计划性推荐
    private String isResource = "3";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public boolean isMineMode() {
        return mineMode;
    }

    public void setMineMode(boolean mineMode) {
        this.mineMode = mineMode;
    }

    public boolean isPreview() {
        return preview;
    }

    public void setPreview(boolean preview) {
        this.preview = preview;
    }


    public String getResource() {
        return isResource;
    }

    public void setResource(String resource) {
        this.isResource = resource;
    }

    public DynamicDetailIntent(SYFeed syNewFeed, boolean mineMode, boolean preview) {
        super();
        this.feed = syNewFeed;
        this.mineMode = mineMode;
        this.preview = preview;
    }

    public DynamicDetailIntent(String id) {
        super();
        this.id = id;
    }

    public DynamicDetailIntent() {
    }

    public SYFeed getFeed() {
        return feed;
    }

    public void setFeed(SYFeed feed) {
        this.feed = feed;
    }

    private SYFeed feed;

    public static Creator<DynamicDetailIntent> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte(this.mineMode ? (byte) 1 : (byte) 0);
        dest.writeString(this.id);
        dest.writeString(this.isResource);
        dest.writeByte(this.preview ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.feed, flags);
    }

    protected DynamicDetailIntent(Parcel in) {
        super(in);
        this.mineMode = in.readByte() != 0;
        this.id = in.readString();
        this.isResource = in.readString();
        this.preview = in.readByte() != 0;
        this.feed = in.readParcelable(SYFeed.class.getClassLoader());
    }

    public static final Creator<DynamicDetailIntent> CREATOR = new Creator<DynamicDetailIntent>() {
        @Override
        public DynamicDetailIntent createFromParcel(Parcel source) {
            return new DynamicDetailIntent(source);
        }

        @Override
        public DynamicDetailIntent[] newArray(int size) {
            return new DynamicDetailIntent[size];
        }
    };
}