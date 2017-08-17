package com.fengnian.smallyellowo.foodie.bean.results;

import android.os.Parcel;
import android.os.Parcelable;

import com.fengnian.smallyellowo.foodie.bean.publics.SYRelationTalentModel;

import java.util.List;

/**
 * Created by Administrator on 2017-2-21.
 */

public class SYRelationTalentResult extends BaseResult implements Parcelable {


    private List<SYRelationTalentModel> talent;

    public List<SYRelationTalentModel> getTalent() {
        return talent;
    }

    public void setTalent(List<SYRelationTalentModel> talent) {
        this.talent = talent;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.talent);
    }

    public SYRelationTalentResult() {
    }

    protected SYRelationTalentResult(Parcel in) {
        this.talent = in.createTypedArrayList(SYRelationTalentModel.CREATOR);
    }

    public static final Creator<SYRelationTalentResult> CREATOR = new Creator<SYRelationTalentResult>() {
        @Override
        public SYRelationTalentResult createFromParcel(Parcel source) {
            return new SYRelationTalentResult(source);
        }

        @Override
        public SYRelationTalentResult[] newArray(int size) {
            return new SYRelationTalentResult[size];
        }
    };
}
