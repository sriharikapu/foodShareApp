package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;

import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;

/**
 * Created by Administrator on 2016-10-19.
 */

public class GalleryInfo extends IntentData {

    private int num;//已经选择相片的张数

    private String ismemo;  // 1  水单    other 不是

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private int index = -1;  // 1  水单    other 不是

    public static final String Is_memo = "1"; //是水单

    public static final String Gallery_pic = "2";//发布相册

    public String getIsmemo() {
        return ismemo;
    }

    public void setIsmemo(String ismemo) {
        this.ismemo = ismemo;
    }

    public static Creator<GalleryInfo> getCREATOR() {
        return CREATOR;
    }

    public GalleryInfo() {
    }

    /**
     * 模板样式
     */
    private  int template_type;

    public int getTemplate_type() {
        return template_type;
    }

    public void setTemplate_type(int template_type) {
        this.template_type = template_type;
    }


    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.num);
        dest.writeString(this.ismemo);
        dest.writeInt(this.index);
        dest.writeInt(this.template_type);
    }

    protected GalleryInfo(Parcel in) {
        super(in);
        this.num = in.readInt();
        this.ismemo = in.readString();
        this.index = in.readInt();
        this.template_type = in.readInt();
    }

    public static final Creator<GalleryInfo> CREATOR = new Creator<GalleryInfo>() {
        @Override
        public GalleryInfo createFromParcel(Parcel source) {
            return new GalleryInfo(source);
        }

        @Override
        public GalleryInfo[] newArray(int size) {
            return new GalleryInfo[size];
        }
    };
}
