package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.utils.FileUitl;
import com.fengnian.smallyellowo.foodie.bean.publish.CachImageManager;

import java.io.File;
import java.io.Serializable;

public class SYRichTextPhotoModel extends SYBaseContentModel implements Parcelable, Serializable {
    private String dishesName;//	富文本菜品，只有富文本有作用	否
    private SYFoodPhotoModel photo;//	美食图片，可能包含多张图片	否

    public String getDishesName() {
        return dishesName;
    }

    public String pullDishesName() {
        if (FFUtils.isStringEmpty(dishesName)) {
            return photo.pullDishName();
        }
        return dishesName;
    }

    public void setDishesName(String dishesName) {
        this.dishesName = dishesName;
    }

    public SYFoodPhotoModel getPhoto() {
        return photo;
    }

    @JSONField(serialize = false)
    public boolean isTextPhotoModel() {
        return photo != null;
    }

    public void setPhoto(SYFoodPhotoModel photo) {
        this.photo = photo;
    }

    public SYRichTextPhotoModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.dishesName);
        dest.writeParcelable(this.photo, flags);
    }

    protected SYRichTextPhotoModel(Parcel in) {
        super(in);
        this.dishesName = in.readString();
        this.photo = in.readParcelable(SYFoodPhotoModel.class.getClassLoader());
    }

    public static final Creator<SYRichTextPhotoModel> CREATOR = new Creator<SYRichTextPhotoModel>() {
        @Override
        public SYRichTextPhotoModel createFromParcel(Parcel source) {
            return new SYRichTextPhotoModel(source);
        }

        @Override
        public SYRichTextPhotoModel[] newArray(int size) {
            return new SYRichTextPhotoModel[size];
        }
    };

    public void delete() {
        String path = CachImageManager.getImageDirWithId(photo.getImageAsset().getPublishId(), getId(), false);
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            FileUitl.delete(file);
        }
    }
}
