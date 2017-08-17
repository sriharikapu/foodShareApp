package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;

import com.fengnian.smallyellowo.foodie.taskmanager.task.PublishModel;

/**
 * Created by Administrator on 2017-1-22.
 */

public class PublishedSuccesskIndata extends IntentData {

    private PublishModel model;

    public PublishModel getModel() {
        return model;
    }

    public void setModel(PublishModel model) {
        this.model = model;
    }




    public PublishedSuccesskIndata() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.model, flags);
    }

    protected PublishedSuccesskIndata(Parcel in) {
        super(in);
        this.model = in.readParcelable(PublishModel.class.getClassLoader());
    }

    public static final Creator<PublishedSuccesskIndata> CREATOR = new Creator<PublishedSuccesskIndata>() {
        @Override
        public PublishedSuccesskIndata createFromParcel(Parcel source) {
            return new PublishedSuccesskIndata(source);
        }

        @Override
        public PublishedSuccesskIndata[] newArray(int size) {
            return new PublishedSuccesskIndata[size];
        }
    };
}
