package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;

import com.fengnian.smallyellowo.foodie.bean.publics.SYComment;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;

/**
 * Created by Administrator on 2016-9-11.
 */
public class CommentIntent extends IntentData {
    SYFeed feed;
    SYComment comment;

    public SYComment getComment() {
        return comment;
    }

    public CommentIntent setComment(SYComment comment) {
        this.comment = comment;
        return this;
    }

    public SYFeed getFeed() {

        return feed;
    }

    public CommentIntent setFeed(SYFeed feed) {
        this.feed = feed;
        return this;
    }

    public CommentIntent() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.feed, flags);
        dest.writeParcelable(this.comment, flags);
    }

    protected CommentIntent(Parcel in) {
        super(in);
        this.feed = in.readParcelable(SYFeed.class.getClassLoader());
        this.comment = in.readParcelable(SYComment.class.getClassLoader());
    }

    public static final Creator<CommentIntent> CREATOR = new Creator<CommentIntent>() {
        @Override
        public CommentIntent createFromParcel(Parcel source) {
            return new CommentIntent(source);
        }

        @Override
        public CommentIntent[] newArray(int size) {
            return new CommentIntent[size];
        }
    };
}
