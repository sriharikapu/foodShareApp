package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;

import com.fengnian.smallyellowo.foodie.bean.publish.DraftModel;

/**
 * Created by Administrator on 2016-12-18.
 */

public class FastEditIntent extends IntentData {
    public DraftModel getFeed() {
        return feed;
    }

    public void setFeed(DraftModel feed) {
        this.feed = feed;
    }

    DraftModel feed;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.feed, flags);
    }

    public FastEditIntent() {
    }

    protected FastEditIntent(Parcel in) {
        super(in);
        this.feed = in.readParcelable(DraftModel.class.getClassLoader());
    }

    public static final Creator<FastEditIntent> CREATOR = new Creator<FastEditIntent>() {
        @Override
        public FastEditIntent createFromParcel(Parcel source) {
            return new FastEditIntent(source);
        }

        @Override
        public FastEditIntent[] newArray(int size) {
            return new FastEditIntent[size];
        }
    };
}
