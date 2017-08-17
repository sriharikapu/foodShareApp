package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;

/**
 * Created by Administrator on 2016-11-23.
 */

public class RichTextEditImageIntent extends IntentData {
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    int index;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.index);
    }

    public RichTextEditImageIntent() {
    }

    public RichTextEditImageIntent(int index) {
        this.index = index;
    }

    protected RichTextEditImageIntent(Parcel in) {
        super(in);
        this.index = in.readInt();
    }

    public static final Creator<RichTextEditImageIntent> CREATOR = new Creator<RichTextEditImageIntent>() {
        @Override
        public RichTextEditImageIntent createFromParcel(Parcel source) {
            return new RichTextEditImageIntent(source);
        }

        @Override
        public RichTextEditImageIntent[] newArray(int size) {
            return new RichTextEditImageIntent[size];
        }
    };
}
