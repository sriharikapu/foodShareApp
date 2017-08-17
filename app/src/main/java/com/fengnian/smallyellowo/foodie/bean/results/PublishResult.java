package com.fengnian.smallyellowo.foodie.bean.results;

import android.os.Parcel;
import android.os.Parcelable;

import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;

import java.io.Serializable;

/**
 * Created by Administrator on 2016-10-8.
 */
public class PublishResult extends BaseResult implements Parcelable, Serializable {

    public SYFeed getFeed() {
        return feed;
    }

    public void setFeed(SYFeed feed) {
        this.feed = feed;
    }

    private SYFeed feed;

    private int foodNoteId;
    private boolean sharedToAct;

    public int getFoodNoteId() {
        return foodNoteId;
    }

    public void setFoodNoteId(int foodNoteId) {
        this.foodNoteId = foodNoteId;
    }

    public boolean isSharedToAct() {
        return sharedToAct;
    }

    public void setSharedToAct(boolean sharedToAct) {
        this.sharedToAct = sharedToAct;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.feed, flags);
        dest.writeInt(this.foodNoteId);
        dest.writeByte(this.sharedToAct ? (byte) 1 : (byte) 0);
    }

    public PublishResult() {
    }

    protected PublishResult(Parcel in) {
        this.feed = in.readParcelable(SYFeed.class.getClassLoader());
        this.foodNoteId = in.readInt();
        this.sharedToAct = in.readByte() != 0;
    }

    public static final Parcelable.Creator<PublishResult> CREATOR = new Parcelable.Creator<PublishResult>() {
        @Override
        public PublishResult createFromParcel(Parcel source) {
            return new PublishResult(source);
        }

        @Override
        public PublishResult[] newArray(int size) {
            return new PublishResult[size];
        }
    };
}
