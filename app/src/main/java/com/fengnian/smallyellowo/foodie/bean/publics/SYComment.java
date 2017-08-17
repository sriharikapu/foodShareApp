package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

import com.fan.framework.base.XData;

import java.io.Serializable;

public class SYComment extends XData implements Parcelable, Serializable {
    private long createTime;// 发布评论的时间戳 否
    private String commentContent;// 评论内容 否
    private SYUser commentUser;// 评论用户对象 否

    //    private SYFeed commentFeed;// 评论的食物对象 否
    private SYComment replyComment;// 被回复的评论对象 否


    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public SYUser getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(SYUser commentUser) {
        this.commentUser = commentUser;
    }

//    public SYFeed getCommentFeed() {
//        return commentFeed;
//    }
//
//    public void setCommentFeed(SYFeed commentFeed) {
//        this.commentFeed = commentFeed;
//    }

    public SYComment getReplyComment() {
        return replyComment;
    }

    public void setReplyComment(SYComment replyComment) {
        this.replyComment = replyComment;
    }

    public SYComment() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.createTime);
        dest.writeString(this.commentContent);
        dest.writeParcelable(this.commentUser, flags);
//        dest.writeParcelable(this.commentFeed, flags);
        dest.writeParcelable(this.replyComment, flags);
    }

    protected SYComment(Parcel in) {
        super(in);
        this.createTime = in.readLong();
        this.commentContent = in.readString();
        this.commentUser = in.readParcelable(SYUser.class.getClassLoader());
//        this.commentFeed = in.readParcelable(SYFeed.class.getClassLoader());
        this.replyComment = in.readParcelable(SYComment.class.getClassLoader());
    }

    public static final Creator<SYComment> CREATOR = new Creator<SYComment>() {
        @Override
        public SYComment createFromParcel(Parcel source) {
            return new SYComment(source);
        }

        @Override
        public SYComment[] newArray(int size) {
            return new SYComment[size];
        }
    };
}
