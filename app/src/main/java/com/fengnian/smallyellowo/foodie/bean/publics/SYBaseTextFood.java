package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.fan.framework.base.XData;
import com.fan.framework.utils.FFUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SYBaseTextFood extends XData implements Parcelable, Serializable {
    private SYPoi poi;// 餐厅位置信息 否

    public static Creator<SYBaseTextFood> getCREATOR() {
        return CREATOR;
    }

    private String createTime;// 发布时间 是
    private List<SYRichTextPhotoModel> richTextLists = new ArrayList<>();// 富文本集合 是

    public SYPoi getPoi() {
        return poi;
    }

    public void setPoi(SYPoi poi) {
        this.poi = poi;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<SYRichTextPhotoModel> getRichTextLists() {
        return richTextLists;
    }

    public void setRichTextLists(List<SYRichTextPhotoModel> richTextLists) {
        this.richTextLists = richTextLists;
    }

    public SYBaseTextFood() {
    }

    public void addImage(String path, String processedImage, String filterName, int index) {
        synchronized (this) {
            SYRichTextPhotoModel img = new SYRichTextPhotoModel();

            SYFoodPhotoModel photo = new SYFoodPhotoModel();
            img.setPhoto(photo);
            SYBaseImageAsset ass = new SYBaseImageAsset();
            photo.setImageAsset(ass);

            if (filterName != null && filterName.length() == 0) {
                filterName = null;
            }

            photo.setImageFilterName(filterName);
            ass.setPublishId(getId());//发布时的SYBaseImageAsset.publishId需要跟food的id保持一致

            ass.setSYImage(path, true);
            //TODO 删除自动把processedImage同步为OriginalImage
            if (processedImage != null) {
                ass.setSYImage(processedImage, false);
            }
            if (index >= 0) {
                getRichTextLists().add(index, img);
            } else {
                getRichTextLists().add(img);
            }
        }

    }

    public void remove(String id) {
        synchronized (this) {
            if (id == null) {
                return;
            }
            if (!FFUtils.isListEmpty(getRichTextLists())) {
                for (int i = 0; i < getRichTextLists().size(); i++) {
                    SYRichTextPhotoModel rich = getRichTextLists().get(i);
                    if (rich.getId().equals(id)) {
                        break;
                    }
                }
            }
        }
    }

    public void remove(SYRichTextPhotoModel rich) {
        synchronized (this) {
            if (rich == null) {
                return;
            }
            if (rich == null) {

            }
            if (!FFUtils.isListEmpty(getRichTextLists())) {
                getRichTextLists().remove(rich);
            }
        }
    }


    /**
     * 获取所有的富文本集合
     *
     * @return 富文本集合
     */
    public List<SYRichTextPhotoModel> allImageContent() {
        ArrayList<SYRichTextPhotoModel> list = new ArrayList<>();
        synchronized (this) {
            for (SYRichTextPhotoModel model : getRichTextLists()) {
                if (model.getPhoto() != null) {
                    list.add(model);
                }
            }
        }
        return list;
    }

    /**
     * 获取所有的纯文本集合
     *
     * @return 纯文本集合
     */
    public List<SYRichTextPhotoModel> allTextContent() {
        ArrayList<SYRichTextPhotoModel> list = new ArrayList<>();
        synchronized (this) {
            for (SYRichTextPhotoModel model : getRichTextLists()) {
                if (model.getPhoto() == null) {
                    list.add(model);
                }
            }
        }
        return list;
    }

    /**
     * 是否存在文字描述
     *
     * @return true 存在 否则不存在
     */
    public boolean haveWordsDescription() {
        synchronized (this) {
            int index = 0;
            for (SYRichTextPhotoModel model : getRichTextLists()) {
                if (!TextUtils.isEmpty(model.getContent())) {
                    if (model.getContent().length() >= 2) {
                        index += model.getContent().length();
                        break;
                    } else {
                        index += model.getContent().length();
                    }
                }
            }

            if (index >= 2)
                return true;

            return false;
        }
    }

    /**
     * 是否存在餐厅名称
     *
     * @return true 存在 否则不存在
     */
    public boolean haveRestaurantName() {
        synchronized (this) {
            if (getPoi() == null) {
                return false;
            }
            if (TextUtils.isEmpty(getPoi().getTitle()))
                return false;
            else
                return true;
        }
    }

    /**
     * 是否存在评价和菜品
     *
     * @return true存在 否则不存在
     */
    public boolean haveCommentTagAndDishes() {
        synchronized (this) {
            for (SYRichTextPhotoModel model : getRichTextLists()) {
                if (model.getPhoto() != null) {
                    if (!TextUtils.isEmpty(model.getDishesName())) {
                        SYFoodPhotoModel photoModel = model.getPhoto();
                        if (photoModel.getImageComment() > 0)
                            return true;
                    }
                }
            }

            return false;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.poi, flags);
        dest.writeString(this.createTime);
        dest.writeTypedList(this.richTextLists);
    }

    protected SYBaseTextFood(Parcel in) {
        super(in);
        this.poi = in.readParcelable(SYPoi.class.getClassLoader());
        this.createTime = in.readString();
        this.richTextLists = in.createTypedArrayList(SYRichTextPhotoModel.CREATOR);
    }

    public static final Creator<SYBaseTextFood> CREATOR = new Creator<SYBaseTextFood>() {
        @Override
        public SYBaseTextFood createFromParcel(Parcel source) {
            return new SYBaseTextFood(source);
        }

        @Override
        public SYBaseTextFood[] newArray(int size) {
            return new SYBaseTextFood[size];
        }
    };
}
