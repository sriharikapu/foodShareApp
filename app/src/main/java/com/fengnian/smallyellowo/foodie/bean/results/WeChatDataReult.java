package com.fengnian.smallyellowo.foodie.bean.results;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017-3-23.
 */

public class WeChatDataReult extends BaseResult implements Parcelable {

    private SYAccountUser  user;
    private  int nickNameStaus ;
    // 1 字数过长时（大于8个汉字或16个英文字符）
    // 2 字数过短时（小于2个汉字或4个英文字符）
    // 3 昵称重复
    // 4 包含特殊符号
    // 0 是 正常登陆


    public SYAccountUser getUser() {
        return user;
    }

    public void setUser(SYAccountUser user) {
        this.user = user;
    }

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
        dest.writeParcelable(this.user, flags);
        dest.writeInt(this.nickNameStaus);
    }

    public WeChatDataReult() {
    }

    protected WeChatDataReult(Parcel in) {
        this.user = in.readParcelable(SYAccountUser.class.getClassLoader());
        this.nickNameStaus = in.readInt();
    }

    public static final Parcelable.Creator<WeChatDataReult> CREATOR = new Parcelable.Creator<WeChatDataReult>() {
        @Override
        public WeChatDataReult createFromParcel(Parcel source) {
            return new WeChatDataReult(source);
        }

        @Override
        public WeChatDataReult[] newArray(int size) {
            return new WeChatDataReult[size];
        }
    };
}
