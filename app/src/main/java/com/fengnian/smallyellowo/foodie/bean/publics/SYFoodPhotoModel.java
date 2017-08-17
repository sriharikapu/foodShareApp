package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;
import com.fan.framework.base.XData;
import com.fan.framework.utils.FFUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class SYFoodPhotoModel extends Object implements Parcelable, Serializable {
    private SYBaseImageAsset imageAsset;// 唯一描述一个美食图片 否
    private int imageComment;// 图片评价 否

    public static final int STATUS_INIT = 0;
    public static final int STATUS_RECOGINZEING = 1;
    public static final int STATUS_SUCCESSED = 2;
    public static final int STATUS_FAIL = 3;
    public static final int STATUS_WAITING = 4;

    @JSONField(serialize = false, deserialize = false)
    public int status = STATUS_INIT;

    public ArrayList<SYPhotoPrecisionModel> getDishesNameList() {
        if (dishesNameList == null) {
            dishesNameList = new ArrayList<>();
        }
        return dishesNameList;
    }

    public void setDishesNameList(ArrayList<SYPhotoPrecisionModel> dishesNameList) {
        this.dishesNameList = dishesNameList;
    }

    public String pullDishName() {
        return pullDishName(false);
    }

    public String pullDishName(boolean needSuggest) {
        if(!FFUtils.isListEmpty(dishesNameList)){
            status = STATUS_SUCCESSED;
        }

        if (status == STATUS_INIT) {
            return null;
        }
        if (status == STATUS_RECOGINZEING) {
            return "识别中...";
        }
        if (status == STATUS_FAIL) {
            return "未识别";
        }
        if (status == STATUS_WAITING) {
            return "待识别";
        }
        if (FFUtils.isListEmpty(dishesNameList)) {
            return "未识别";
        }
        String bestName = "未识别";
        if (needSuggest) {
            float max = 0;
            for (SYPhotoPrecisionModel model : dishesNameList) {
                if (model.getPrecision() > max) {
                    bestName = model.getContent();
                    max = model.getPrecision();
                }
            }
        }
        return bestName;
    }


    private ArrayList<SYPhotoPrecisionModel> dishesNameList;

    private String imageFilterName;// 滤镜名称 否

    public SYBaseImageAsset getImageAsset() {
        return imageAsset;
    }

    public void setImageAsset(SYBaseImageAsset imageAsset) {
        this.imageAsset = imageAsset;
    }

    public int getImageComment() {
        return imageComment;
    }

    public void setImageComment(int imageComment) {
        this.imageComment = imageComment;
    }

    public String getImageFilterName() {
        return imageFilterName;
    }

    public void setImageFilterName(String imageFilterName) {
        this.imageFilterName = imageFilterName;
    }

    public SYFoodPhotoModel() {
        setImageAsset(new SYBaseImageAsset());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.imageAsset, flags);
        dest.writeInt(this.imageComment);
        dest.writeTypedList(this.dishesNameList);
        dest.writeString(this.imageFilterName);
    }

    protected SYFoodPhotoModel(Parcel in) {
        this.imageAsset = in.readParcelable(SYBaseImageAsset.class.getClassLoader());
        this.imageComment = in.readInt();
        this.dishesNameList = in.createTypedArrayList(SYPhotoPrecisionModel.CREATOR);
        this.imageFilterName = in.readString();
    }

    public static final Creator<SYFoodPhotoModel> CREATOR = new Creator<SYFoodPhotoModel>() {
        @Override
        public SYFoodPhotoModel createFromParcel(Parcel source) {
            return new SYFoodPhotoModel(source);
        }

        @Override
        public SYFoodPhotoModel[] newArray(int size) {
            return new SYFoodPhotoModel[size];
        }
    };
}
