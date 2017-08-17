package com.fengnian.smallyellowo.foodie.bean.results;

import android.os.Parcel;
import android.os.Parcelable;

import com.fengnian.smallyellowo.foodie.bean.publish.SYExploitValueRank;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-3-1.
 */

public class ClubGongXunPaiHangResult extends BaseResult implements Parcelable {

     private SYExploitValueRank userRank;

    private ArrayList<SYExploitValueRank> syExploitValueRanks;

    public SYExploitValueRank getUserRank() {
        return userRank;
    }

    public void setUserRank(SYExploitValueRank userRank) {
        this.userRank = userRank;
    }

    public ArrayList<SYExploitValueRank> getSyExploitValueRanks() {
        return syExploitValueRanks;
    }

    public void setSyExploitValueRanks(ArrayList<SYExploitValueRank> syExploitValueRanks) {
        this.syExploitValueRanks = syExploitValueRanks;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.userRank, flags);
        dest.writeTypedList(this.syExploitValueRanks);
    }

    public ClubGongXunPaiHangResult() {
    }

    protected ClubGongXunPaiHangResult(Parcel in) {
        this.userRank = in.readParcelable(SYExploitValueRank.class.getClassLoader());
        this.syExploitValueRanks = in.createTypedArrayList(SYExploitValueRank.CREATOR);
    }

    public static final Parcelable.Creator<ClubGongXunPaiHangResult> CREATOR = new Parcelable.Creator<ClubGongXunPaiHangResult>() {
        @Override
        public ClubGongXunPaiHangResult createFromParcel(Parcel source) {
            return new ClubGongXunPaiHangResult(source);
        }

        @Override
        public ClubGongXunPaiHangResult[] newArray(int size) {
            return new ClubGongXunPaiHangResult[size];
        }
    };
}
