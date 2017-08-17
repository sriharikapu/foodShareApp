package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;

import java.util.List;

/**
 * Created by Administrator on 2016-9-30.
 */

public class PhotoInfo extends IntentData implements Parcelable {

    private List<ImageItem>  list;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private int index = -1;

    private int pos;//专辑列表 的pos

    private String ismemo;

    private int num;//已经选择几张了

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

    public static Creator<PhotoInfo> getCREATOR() {
        return CREATOR;
    }

    public String getIsmemo() {
        return ismemo;
    }

    public void setIsmemo(String ismemo) {
        this.ismemo = ismemo;
    }


    public int  sty;

    /**
     * 模板类型
     * @return
     */
    public int getSty() {
        return sty;
    }

    public void setSty(int sty) {
        this.sty = sty;
    }

    public List<ImageItem> getList() {
        return list;
    }

    public void setList(List<ImageItem> list) {
        this.list = list;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }


    public PhotoInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(this.list);
        dest.writeInt(this.index);
        dest.writeInt(this.pos);
        dest.writeString(this.ismemo);
        dest.writeInt(this.num);
        dest.writeInt(this.template_type);
        dest.writeInt(this.sty);
    }

    protected PhotoInfo(Parcel in) {
        super(in);
        this.list = in.createTypedArrayList(ImageItem.CREATOR);
        this.index = in.readInt();
        this.pos = in.readInt();
        this.ismemo = in.readString();
        this.num = in.readInt();
        this.template_type = in.readInt();
        this.sty = in.readInt();
    }

    public static final Creator<PhotoInfo> CREATOR = new Creator<PhotoInfo>() {
        @Override
        public PhotoInfo createFromParcel(Parcel source) {
            return new PhotoInfo(source);
        }

        @Override
        public PhotoInfo[] newArray(int size) {
            return new PhotoInfo[size];
        }
    };
}
