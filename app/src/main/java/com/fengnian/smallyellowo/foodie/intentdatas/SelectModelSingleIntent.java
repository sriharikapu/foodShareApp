package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;

/**
 * Created by Administrator on 2017-2-28.
 */

public class SelectModelSingleIntent extends IntentData {
    public int getModelIndex() {
        return modelIndex;
    }

    public void setModelIndex(int modelIndex) {
        this.modelIndex = modelIndex;
    }

    int modelIndex;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.modelIndex);
    }

    public SelectModelSingleIntent() {
    }

    public SelectModelSingleIntent(Parcel in) {
        super(in);
        this.modelIndex = in.readInt();
    }

    public SelectModelSingleIntent(int modelIndex, int requestCode) {
        this.modelIndex = modelIndex;
        setRequestCode(requestCode);
    }

    public static final Creator<SelectModelSingleIntent> CREATOR = new Creator<SelectModelSingleIntent>() {
        @Override
        public SelectModelSingleIntent createFromParcel(Parcel source) {
            return new SelectModelSingleIntent(source);
        }

        @Override
        public SelectModelSingleIntent[] newArray(int size) {
            return new SelectModelSingleIntent[size];
        }
    };
}
