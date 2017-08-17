package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;

import com.fan.framework.base.XData;

import java.util.List;

/**
 * Created by Administrator on 2016-11-10.
 */

public class SYSecondLevelcomment extends XData {
    private SYComment firstLevelComment;
    private List<SYComment> childrenCommentsList;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<SYComment> getChildrenCommentsList() {
        return childrenCommentsList;
    }

    public void setChildrenCommentsList(List<SYComment> childrenCommentsList) {
        this.childrenCommentsList = childrenCommentsList;
    }

    public SYComment getFirstLevelComment() {
        return firstLevelComment;
    }

    public void setFirstLevelComment(SYComment firstLevelComment) {
        this.firstLevelComment = firstLevelComment;
    }

    private boolean deleted;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.firstLevelComment, flags);
        dest.writeTypedList(this.childrenCommentsList);
        dest.writeByte(this.deleted ? (byte) 1 : (byte) 0);
    }

    public SYSecondLevelcomment() {
    }

    protected SYSecondLevelcomment(Parcel in) {
        super(in);
        this.firstLevelComment = in.readParcelable(SYComment.class.getClassLoader());
        this.childrenCommentsList = in.createTypedArrayList(SYComment.CREATOR);
        this.deleted = in.readByte() != 0;
    }

    public static final Creator<SYSecondLevelcomment> CREATOR = new Creator<SYSecondLevelcomment>() {
        @Override
        public SYSecondLevelcomment createFromParcel(Parcel source) {
            return new SYSecondLevelcomment(source);
        }

        @Override
        public SYSecondLevelcomment[] newArray(int size) {
            return new SYSecondLevelcomment[size];
        }
    };
}
