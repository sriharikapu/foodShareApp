package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;

import com.fengnian.smallyellowo.foodie.bean.results.PublishLimitResult;

public class CheckScoreIntent extends IntentData {
    PublishLimitResult result;
    String isRichText;

    public PublishLimitResult getResult() {
        return result;
    }

    public void setResult(PublishLimitResult result) {
        this.result = result;
    }

    public String getIsRichText() {
        return isRichText;
    }

    public void setIsRichText(String isRichText) {
        this.isRichText = isRichText;
    }

    public CheckScoreIntent(PublishLimitResult result, String isRichText) {
        this.result = result;
        this.isRichText = isRichText;
    }

    public CheckScoreIntent() {
        this.result = result;
        this.isRichText = isRichText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeSerializable(this.result);
        dest.writeString(this.isRichText);
    }

    protected CheckScoreIntent(Parcel in) {
        super(in);
        this.result = (PublishLimitResult) in.readSerializable();
        this.isRichText = in.readString();
    }

    public static final Creator<CheckScoreIntent> CREATOR = new Creator<CheckScoreIntent>() {
        @Override
        public CheckScoreIntent createFromParcel(Parcel source) {
            return new CheckScoreIntent(source);
        }

        @Override
        public CheckScoreIntent[] newArray(int size) {
            return new CheckScoreIntent[size];
        }
    };
}