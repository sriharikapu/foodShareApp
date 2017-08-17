package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;

/**
 * Created by Administrator on 2016-10-9.
 */
public class RichTextEditIntent extends IntentData {

    public static final int TYPE_EDIT_CONTINUE = 0;
    public static final int TYPE_EDIT_NEW = 1;
    public static final int TYPE_EDIT_FAILED = 2;
    public static final int TYPE_EDIT_ONLINE = 3;

    public int getModelIndex() {
        return modelIndex;
    }

    public void setModelIndex(int modelIndex) {
        this.modelIndex = modelIndex;
    }

    private int modelIndex;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type = TYPE_EDIT_CONTINUE;

    public RichTextEditIntent(int type) {
        this.type = type;
    }

    public RichTextEditIntent() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.modelIndex);
        dest.writeInt(this.type);
    }

    protected RichTextEditIntent(Parcel in) {
        super(in);
        this.modelIndex = in.readInt();
        this.type = in.readInt();
    }

    public static final Creator<RichTextEditIntent> CREATOR = new Creator<RichTextEditIntent>() {
        @Override
        public RichTextEditIntent createFromParcel(Parcel source) {
            return new RichTextEditIntent(source);
        }

        @Override
        public RichTextEditIntent[] newArray(int size) {
            return new RichTextEditIntent[size];
        }
    };
}
