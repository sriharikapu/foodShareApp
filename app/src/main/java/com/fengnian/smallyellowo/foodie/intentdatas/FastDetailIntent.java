package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;

import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;

public class FastDetailIntent extends com.fengnian.smallyellowo.foodie.intentdatas.IntentData {

    private boolean mineMode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public boolean isMineMode() {
        return mineMode;
    }

    public void setMineMode(boolean mineMode) {
        this.mineMode = mineMode;
    }

    public FastDetailIntent(SYFeed syNewFeed, boolean mineMode) {
        this();
        this.feed = syNewFeed;
        this.mineMode = mineMode;
    }

    public FastDetailIntent(String id) {
        this();
        this.id = id;
    }

    public FastDetailIntent() {
    }

    public SYFeed getFeed() {
        return feed;
    }

    public void setFeed(SYFeed feed) {
        this.feed = feed;
    }

    private SYFeed feed;

    public static Creator<FastDetailIntent> getCREATOR() {
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
        dest.writeParcelable(this.feed, flags);
    }

    protected FastDetailIntent(Parcel in) {
        super(in);
        this.mineMode = in.readByte() != 0;
        this.id = in.readString();
        this.feed = in.readParcelable(SYFeed.class.getClassLoader());
    }

    public static final Creator<FastDetailIntent> CREATOR = new Creator<FastDetailIntent>() {
        @Override
        public FastDetailIntent createFromParcel(Parcel source) {
            return new FastDetailIntent(source);
        }

        @Override
        public FastDetailIntent[] newArray(int size) {
            return new FastDetailIntent[size];
        }
    };
}