package com.fengnian.smallyellowo.foodie.bean.results;

import android.os.Parcel;
import android.os.Parcelable;

import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.publish.SYClubVipUser;
import com.fengnian.smallyellowo.foodie.bean.publish.SYVipUserRecommend;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-2-27.
 */

public class ClubUserinfoResult extends GetUserInfoResult implements Parcelable {
    private SYClubVipUser clubUser;//俱乐部会员

    private int recordState ;//动态状态－0不显示、1显示且new、2显示'
    private int noteState ;//清单状态 0不显示、1显示且new、2显示'
    private int scoreState ;//积分状态  0不显示、1显示且new、2显示'
    private int wantEatState ;//想吃状态  0不显示、1显示且new、2显示'
    private ArrayList<SYVipUserRecommend> vipUserRecommend;//用户推荐的信息


    public SYClubVipUser getClubUser() {
        return clubUser;
    }

    public void setClubUser(SYClubVipUser clubUser) {
        this.clubUser = clubUser;
    }

    public int getRecordState() {
        return recordState;
    }

    public void setRecordState(int recordState) {
        this.recordState = recordState;
    }

    public int getNoteState() {
        return noteState;
    }

    public void setNoteState(int noteState) {
        this.noteState = noteState;
    }

    public int getScoreState() {
        return scoreState;
    }

    public void setScoreState(int scoreState) {
        this.scoreState = scoreState;
    }

    public int getWantEatState() {
        return wantEatState;
    }

    public void setWantEatState(int wantEatState) {
        this.wantEatState = wantEatState;
    }

    public ArrayList<SYVipUserRecommend> getVipUserRecommend() {
        return vipUserRecommend;
    }

    public void setVipUserRecommend(ArrayList<SYVipUserRecommend> vipUserRecommend) {
        this.vipUserRecommend = vipUserRecommend;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.clubUser, flags);
        dest.writeInt(this.recordState);
        dest.writeInt(this.noteState);
        dest.writeInt(this.scoreState);
        dest.writeInt(this.wantEatState);
        dest.writeTypedList(this.vipUserRecommend);
    }

    public ClubUserinfoResult() {
    }

    protected ClubUserinfoResult(Parcel in) {
        this.clubUser = in.readParcelable(SYClubVipUser.class.getClassLoader());
        this.recordState = in.readInt();
        this.noteState = in.readInt();
        this.scoreState = in.readInt();
        this.wantEatState = in.readInt();
        this.vipUserRecommend = in.createTypedArrayList(SYVipUserRecommend.CREATOR);
    }

    public static final Parcelable.Creator<ClubUserinfoResult> CREATOR = new Parcelable.Creator<ClubUserinfoResult>() {
        @Override
        public ClubUserinfoResult createFromParcel(Parcel source) {
            return new ClubUserinfoResult(source);
        }

        @Override
        public ClubUserinfoResult[] newArray(int size) {
            return new ClubUserinfoResult[size];
        }
    };
}
