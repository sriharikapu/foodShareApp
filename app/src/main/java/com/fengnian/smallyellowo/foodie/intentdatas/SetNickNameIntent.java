package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017-3-24.
 */

public class SetNickNameIntent extends IntentData implements Parcelable{
    private int nickNameStaus;

    public int getNickNameStaus() {
        return nickNameStaus;
    }

    public void setNickNameStaus(int nickNameStaus) {
        this.nickNameStaus = nickNameStaus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.nickNameStaus);
    }

    public SetNickNameIntent() {
    }

    protected SetNickNameIntent(Parcel in) {
        super(in);
        this.nickNameStaus = in.readInt();
    }

    public static final Creator<SetNickNameIntent> CREATOR = new Creator<SetNickNameIntent>() {
        @Override
        public SetNickNameIntent createFromParcel(Parcel source) {
            return new SetNickNameIntent(source);
        }

        @Override
        public SetNickNameIntent[] newArray(int size) {
            return new SetNickNameIntent[size];
        }
    };
}
