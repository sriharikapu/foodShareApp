package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;

/**
 * Created by Administrator on 2016-11-10.
 */

public class SearchRestIntent extends IntentData {
    public SearchRestIntent(int requestCode, String keyword) {
        setRequestCode(requestCode);
        this.word = keyword;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    private String word;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.word);
    }

    public SearchRestIntent() {
    }

    protected SearchRestIntent(Parcel in) {
        super(in);
        this.word = in.readString();
    }

    public static final Creator<SearchRestIntent> CREATOR = new Creator<SearchRestIntent>() {
        @Override
        public SearchRestIntent createFromParcel(Parcel source) {
            return new SearchRestIntent(source);
        }

        @Override
        public SearchRestIntent[] newArray(int size) {
            return new SearchRestIntent[size];
        }
    };
}
