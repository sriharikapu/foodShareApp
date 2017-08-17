package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;

import com.fan.framework.base.XData;

/**
 * Created by Administrator on 2017-4-17.
 */

public class SYChoicenessModel extends XData {
    String title;//	标题	是
    String type;//	0-pgc 1-活动	是
    String content;//	描述	是
    String imageUrl;//	图片地址	是
    String htmlUrl;//	H5链接	是
    String messageId;//	消息ID	是

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.title);
        dest.writeString(this.type);
        dest.writeString(this.content);
        dest.writeString(this.imageUrl);
        dest.writeString(this.htmlUrl);
        dest.writeString(this.messageId);
    }

    public SYChoicenessModel() {
    }

    protected SYChoicenessModel(Parcel in) {
        super(in);
        this.title = in.readString();
        this.type = in.readString();
        this.content = in.readString();
        this.imageUrl = in.readString();
        this.htmlUrl = in.readString();
        this.messageId = in.readString();
    }

    public static final Creator<SYChoicenessModel> CREATOR = new Creator<SYChoicenessModel>() {
        @Override
        public SYChoicenessModel createFromParcel(Parcel source) {
            return new SYChoicenessModel(source);
        }

        @Override
        public SYChoicenessModel[] newArray(int size) {
            return new SYChoicenessModel[size];
        }
    };
}
