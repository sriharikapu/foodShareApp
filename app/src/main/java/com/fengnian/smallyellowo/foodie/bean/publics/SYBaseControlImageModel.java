package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

import com.fan.framework.base.XData;

import java.io.Serializable;

/**
 * 图片操作
 *
 * @author Administrator
 */
public class SYBaseControlImageModel extends XData implements Parcelable,Serializable {
    private int type;// 0-剪裁 1-更换滤镜 2-翻转 3-评价 是
    private String zoomedCropRect;// 0 -有效 否
    private String rotation;// 0- 有效 否
    private String filterName;// 1-有效 否
    private float ratio;// 2-有效 否
    private int foodAssetComment;// 3-有效 否

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getZoomedCropRect() {
        return zoomedCropRect;
    }

    public void setZoomedCropRect(String zoomedCropRect) {
        this.zoomedCropRect = zoomedCropRect;
    }

    public String getRotation() {
        return rotation;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public int getFoodAssetComment() {
        return foodAssetComment;
    }

    public void setFoodAssetComment(int foodAssetComment) {
        this.foodAssetComment = foodAssetComment;
    }


    public SYBaseControlImageModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.type);
        dest.writeString(this.zoomedCropRect);
        dest.writeString(this.rotation);
        dest.writeString(this.filterName);
        dest.writeFloat(this.ratio);
        dest.writeInt(this.foodAssetComment);
    }

    protected SYBaseControlImageModel(Parcel in) {
        super(in);
        this.type = in.readInt();
        this.zoomedCropRect = in.readString();
        this.rotation = in.readString();
        this.filterName = in.readString();
        this.ratio = in.readFloat();
        this.foodAssetComment = in.readInt();
    }

    public static final Creator<SYBaseControlImageModel> CREATOR = new Creator<SYBaseControlImageModel>() {
        @Override
        public SYBaseControlImageModel createFromParcel(Parcel source) {
            return new SYBaseControlImageModel(source);
        }

        @Override
        public SYBaseControlImageModel[] newArray(int size) {
            return new SYBaseControlImageModel[size];
        }
    };
}
