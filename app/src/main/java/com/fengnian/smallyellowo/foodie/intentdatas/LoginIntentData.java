package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;

/**
 * Created by Administrator on 2017-1-11.
 */

public class LoginIntentData extends IntentData {
    private String code;

    public String getCode() {
        return code;
    }

    public  String   phone="";
    public  int type;  //1000是验证码 登录    2000是忘记密码

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public void setCode(String code) {
        this.code = code;
    }

    private int nickNameStaus;

    public int getNickNameStaus() {
        return nickNameStaus;
    }

    public void setNickNameStaus(int nickNameStaus) {
        this.nickNameStaus = nickNameStaus;
    }

    public LoginIntentData(){
        super();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.code);
        dest.writeString(this.phone);
        dest.writeInt(this.type);
        dest.writeInt(this.nickNameStaus);
    }

    protected LoginIntentData(Parcel in) {
        super(in);
        this.code = in.readString();
        this.phone = in.readString();
        this.type = in.readInt();
        this.nickNameStaus = in.readInt();
    }

    public static final Creator<LoginIntentData> CREATOR = new Creator<LoginIntentData>() {
        @Override
        public LoginIntentData createFromParcel(Parcel source) {
            return new LoginIntentData(source);
        }

        @Override
        public LoginIntentData[] newArray(int size) {
            return new LoginIntentData[size];
        }
    };
}
