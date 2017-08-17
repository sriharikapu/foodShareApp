package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;

/**
 * Created by Administrator on 2016-8-12.
 */
public class PGCDetailIntent extends IntentData {
    private String url;
    private String title;

    private String isAppns;

    public String getIsAppns() {
        return isAppns;
    }

    public void setIsAppns(String isAppns) {
        this.isAppns = isAppns;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    private String imgUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String account;
    private String token;
    private String version;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public PGCDetailIntent() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(getUrl());
        dest.writeString(getImgUrl());
        dest.writeString(getTitle());
        dest.writeString(getId());
        dest.writeString(getToken());
        dest.writeString(getVersion());
        dest.writeString(getAccount());
        dest.writeString(getIsAppns());
    }

    protected PGCDetailIntent(Parcel in) {
        super(in);
        setUrl(in.readString());
        setImgUrl(in.readString());
        setTitle(in.readString());
        setId(in.readString());
        setToken(in.readString());
        setVersion(in.readString());
        setAccount(in.readString());
        setIsAppns(in.readString());
    }

    public static final Creator<PGCDetailIntent> CREATOR = new Creator<PGCDetailIntent>() {
        @Override
        public PGCDetailIntent createFromParcel(Parcel source) {
            return new PGCDetailIntent(source);
        }

        @Override
        public PGCDetailIntent[] newArray(int size) {
            return new PGCDetailIntent[size];
        }
    };
}
