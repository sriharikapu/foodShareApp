package com.fengnian.smallyellowo.foodie.bean.results;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class HotSearchWordResult extends BaseResult {

    private List<HotWord> config;

    public List<HotWord> getConfig() {
        return config;
    }

    public void setConfig(List<HotWord> config) {
        this.config = config;
    }

    public static class HotWord implements Parcelable {
        private String id;
        private String name;
        private String code;
        private int type;
        private long updateTime;
        private int count;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public HotWord() {
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.name);
            dest.writeString(this.code);
            dest.writeInt(this.type);
            dest.writeLong(this.updateTime);
            dest.writeInt(this.count);
        }

        protected HotWord(Parcel in) {
            this.id = in.readString();
            this.name = in.readString();
            this.code = in.readString();
            this.type = in.readInt();
            this.updateTime = in.readLong();
            this.count = in.readInt();
        }

        public static final Creator<HotWord> CREATOR = new Creator<HotWord>() {
            @Override
            public HotWord createFromParcel(Parcel source) {
                return new HotWord(source);
            }

            @Override
            public HotWord[] newArray(int size) {
                return new HotWord[size];
            }
        };
    }
}
