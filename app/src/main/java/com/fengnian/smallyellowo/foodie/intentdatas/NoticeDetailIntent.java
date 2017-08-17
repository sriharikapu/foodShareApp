package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;

import com.fengnian.smallyellowo.foodie.bean.results.NoticeResult;

/**
 * Created by Administrator on 2016-9-19.
 */
public class NoticeDetailIntent extends IntentData{
    public NoticeResult.NotificationMessagesBean getBean() {
        return bean;
    }

    public void setBean(NoticeResult.NotificationMessagesBean bean) {
        this.bean = bean;
    }

    NoticeResult.NotificationMessagesBean bean;

    public NoticeDetailIntent() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.bean, flags);
    }

    protected NoticeDetailIntent(Parcel in) {
        super(in);
        this.bean = in.readParcelable(NoticeResult.NotificationMessagesBean.class.getClassLoader());
    }

    public static final Creator<NoticeDetailIntent> CREATOR = new Creator<NoticeDetailIntent>() {
        @Override
        public NoticeDetailIntent createFromParcel(Parcel source) {
            return new NoticeDetailIntent(source);
        }

        @Override
        public NoticeDetailIntent[] newArray(int size) {
            return new NoticeDetailIntent[size];
        }
    };
}
