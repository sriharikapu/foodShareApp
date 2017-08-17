package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016-11-15.
 */
public class AddRichEdtextContentIntent extends  IntentData  implements Parcelable {

    public boolean isdishname() {
        return isdishname;
    }

    public void setIsdishname(boolean isdishname) {
        this.isdishname = isdishname;
    }

    private  boolean isdishname;  //2 是菜品名字   3 content内容

    private  int   pos;

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }


    public AddRichEdtextContentIntent() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte(this.isdishname ? (byte) 1 : (byte) 0);
        dest.writeInt(this.pos);
    }

    protected AddRichEdtextContentIntent(Parcel in) {
        super(in);
        this.isdishname = in.readByte() != 0;
        this.pos = in.readInt();
    }

    public static final Creator<AddRichEdtextContentIntent> CREATOR = new Creator<AddRichEdtextContentIntent>() {
        @Override
        public AddRichEdtextContentIntent createFromParcel(Parcel source) {
            return new AddRichEdtextContentIntent(source);
        }

        @Override
        public AddRichEdtextContentIntent[] newArray(int size) {
            return new AddRichEdtextContentIntent[size];
        }
    };
}
