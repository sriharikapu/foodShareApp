package com.fengnian.smallyellowo.foodie.bean.publish;

import android.os.Parcel;
import android.os.Parcelable;

import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-2-27.
 */

public class SYClubVipUser implements Parcelable {
    private SYUser user;
    private String clubNum=""; //用户编号
    private float vipUserFeats;//用户功勋值
    private int vipUserRank;//用户排行
    private ArrayList<String> vipUserImage;//图片信息

    public SYUser getUser() {
        return user;
    }

    public void setUser(SYUser user) {
        this.user = user;
    }

    public String getClubNum() {
        return clubNum;
    }

    public void setClubNum(String clubNum) {
        this.clubNum = clubNum;
    }

    public float getVipUserFeats() {
        return vipUserFeats;
    }

    public void setVipUserFeats(float vipUserFeats) {
        this.vipUserFeats = vipUserFeats;
    }

    public int getVipUserRank() {
        return vipUserRank;
    }

    public void setVipUserRank(int vipUserRank) {
        this.vipUserRank = vipUserRank;
    }

    public ArrayList<String> getVipUserImage() {
        return vipUserImage;
    }

    public void setVipUserImage(ArrayList<String> vipUserImage) {
        this.vipUserImage = vipUserImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.clubNum);
        dest.writeFloat(this.vipUserFeats);
        dest.writeInt(this.vipUserRank);
        dest.writeStringList(this.vipUserImage);
    }

    public SYClubVipUser() {
    }

    protected SYClubVipUser(Parcel in) {
        this.user = in.readParcelable(SYUser.class.getClassLoader());
        this.clubNum = in.readString();
        this.vipUserFeats = in.readFloat();
        this.vipUserRank = in.readInt();
        this.vipUserImage = in.createStringArrayList();
    }

    public static final Parcelable.Creator<SYClubVipUser> CREATOR = new Parcelable.Creator<SYClubVipUser>() {
        @Override
        public SYClubVipUser createFromParcel(Parcel source) {
            return new SYClubVipUser(source);
        }

        @Override
        public SYClubVipUser[] newArray(int size) {
            return new SYClubVipUser[size];
        }
    };
}
