package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;
import android.os.Parcelable;

import com.fan.framework.utils.FFUtils;

/**
 * Created by Administrator on 2016-9-8.
 */

public class WebInfo extends IntentData implements Parcelable {

    private String url;
    private String title;
    private boolean isMenuVisible = true;

    public String getTitle() {
        if (FFUtils.isStringEmpty(title)) {
            title = "详情";
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isMenuVisible() {
        return isMenuVisible;
    }

    public void setMenuVisible(boolean menuVisible) {
        this.isMenuVisible = menuVisible;
    }

    public WebInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.url);
        dest.writeString(this.title);
        dest.writeByte(this.isMenuVisible ? (byte) 1 : (byte) 0);
    }

    protected WebInfo(Parcel in) {
        super(in);
        this.url = in.readString();
        this.title = in.readString();
        this.isMenuVisible = in.readByte() != 0;
    }

    public static final Creator<WebInfo> CREATOR = new Creator<WebInfo>() {
        @Override
        public WebInfo createFromParcel(Parcel source) {
            return new WebInfo(source);
        }

        @Override
        public WebInfo[] newArray(int size) {
            return new WebInfo[size];
        }
    };
}
