package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.utils.TimeUtils;

import java.io.Serializable;
import java.util.List;

public class SYRichTextFood extends SYBaseTextFood implements Parcelable, Serializable {
    private int foodType = 0;// 食物类型 否
    private double totalPrice;// 总价 否
    private int numberOfPeople;// 就餐人数 否

    public String getTitle() {
        String frontCoverContent = getFrontCoverModel().getFrontCoverContent().getContent();
        if(FFUtils.isStringEmpty(frontCoverContent)){
            return TimeUtils.getTime("yyyyMMdd", System.currentTimeMillis());
        }
        return frontCoverContent;
    }
//
//    public void setTitle(String title) {
//        this.title = title;
//        getFrontCoverModel().getFrontCoverContent().setContent(title);
//        if (getHeadImage() != null) {
//            getHeadImage().setContent(title);
//        }
//    }

    /**
     * 服务器需要的title
     */
//    private String title;

    /**
     * 只有速记有
     */
    private String content = "";
    private SYRichTextPhotoModel headImage;// 头图 是
    private SYFrontCoverModel frontCoverModel;// 封面对象 否
    private List<String> dishesNameList;// 水单列表 否

//    public String headImage() {
//        if (headImage != null) {
//            return headImage.getPhoto().getImageAsset().getOriginalImage().getImage().getUrl();
//        }
//        for (SYRichTextPhotoModel mode : getRichTextLists()) {
//            if (mode.isTextPhotoModel()) {
//                return mode.getPhoto().getImageAsset().getOriginalImage().getImage().getUrl();
//            }
//        }
//        return "";
//    }

    public boolean wasMeishiBianji() {
        return isMeishiBianji;
    }

    private boolean isMeishiBianji = false;

    public int getFoodType() {
        return foodType;
    }

    public void setFoodType(int foodType) {
        this.foodType = foodType;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public SYRichTextPhotoModel getHeadImage() {
        return headImage;
    }

    public String pullHeadImage() {
        SYRichTextPhotoModel headImage = getHeadImage();
        if (headImage == null || headImage.getPhoto().getImageAsset().pullProcessedImageUrl() == null) {
            if (getFrontCoverModel() != null && getFrontCoverModel().getFrontCoverContent().getPhoto().getImageAsset().pullProcessedImageUrl() != null) {
                headImage = getFrontCoverModel().getFrontCoverContent();
            } else {
                for (int i = 0; i < getRichTextLists().size(); i++) {
                    SYFoodPhotoModel photo = getRichTextLists().get(i).getPhoto();
                    if (photo != null) {
                        headImage = getRichTextLists().get(i);
                        break;
                    }
                }
            }
        }
        if (headImage == null) {
            return null;
        }
        return headImage.getPhoto().getImageAsset().pullProcessedImageUrl();
    }


    public void setHeadImage(SYRichTextPhotoModel headImage) {
        this.isMeishiBianji = headImage != null;
        this.headImage = headImage;
    }

    public SYFrontCoverModel getFrontCoverModel() {
        if (frontCoverModel == null) {
            frontCoverModel = new SYFrontCoverModel();
        }
        return frontCoverModel;
    }

    public void setFrontCoverModel(SYFrontCoverModel frontCoverModel) {
        this.frontCoverModel = frontCoverModel;
    }

    public List<String> getDishesNameList() {
        return dishesNameList;
    }

    public void setDishesNameList(List<String> dishesNameList) {
        this.dishesNameList = dishesNameList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static final int TYPE_NONE = 0;
    public static final int TYPE_ZC = 1;
    public static final int TYPE_ZWC = 2;
    public static final int TYPE_WUC = 3;
    public static final int TYPE_XWC = 4;
    public static final int TYPE_WANC = 5;
    public static final int TYPE_YX = 6;

    @JSONField(serialize = false)
    public String getFoodTypeString() {
        switch (foodType) {
            case 1:
                return "早餐";
            case 2:
                return "早午餐";
            case 3:
                return "午餐";
            case 4:
                return "下午茶";
            case 5:
                return "晚餐";
            case 6:
                return "夜宵";
        }
        return "";
    }

    public SYRichTextFood() {
    }


    /**
     * 是否存在封面
     *
     * @return true存在 否则不存在
     */
    public boolean haveCoverImage() {
        if (getFrontCoverModel() == null)
            return false;

        if (getFrontCoverModel().getFrontCoverContent() == null)
            return false;

        SYRichTextPhotoModel richTextPhotoModel = getFrontCoverModel().getFrontCoverContent();
        if (richTextPhotoModel.getPhoto() != null) {
            return true;
        }
        return false;
    }

    /**
     * 是否存在封面标题
     *
     * @return true存在 否则不存在
     */
    public boolean haveCoverTitle() {
        if (getFrontCoverModel() == null)
            return false;

        if (getFrontCoverModel().getFrontCoverContent() == null)
            return false;

        SYRichTextPhotoModel richTextPhotoModel = getFrontCoverModel().getFrontCoverContent();
        if (TextUtils.isEmpty(richTextPhotoModel.getContent())) {
            return false;
        }
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.foodType);
        dest.writeDouble(this.totalPrice);
        dest.writeInt(this.numberOfPeople);
//        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeParcelable(this.headImage, flags);
        dest.writeParcelable(this.frontCoverModel, flags);
        dest.writeStringList(this.dishesNameList);
        dest.writeByte(this.isMeishiBianji ? (byte) 1 : (byte) 0);
    }

    protected SYRichTextFood(Parcel in) {
        super(in);
        this.foodType = in.readInt();
        this.totalPrice = in.readDouble();
        this.numberOfPeople = in.readInt();
//        this.title = in.readString();
        this.content = in.readString();
        this.headImage = in.readParcelable(SYRichTextPhotoModel.class.getClassLoader());
        this.frontCoverModel = in.readParcelable(SYFrontCoverModel.class.getClassLoader());
        this.dishesNameList = in.createStringArrayList();
        this.isMeishiBianji = in.readByte() != 0;
    }

    public static final Creator<SYRichTextFood> CREATOR = new Creator<SYRichTextFood>() {
        @Override
        public SYRichTextFood createFromParcel(Parcel source) {
            return new SYRichTextFood(source);
        }

        @Override
        public SYRichTextFood[] newArray(int size) {
            return new SYRichTextFood[size];
        }
    };

    public String pullCoverImage() {
        SYImage img = pullCoverImg();
        if (img == null) {
            return null;
        }

        return img.getUrl();
    }

    public SYImage pullCoverImg() {
        SYRichTextPhotoModel headImage = null;
        if (getFrontCoverModel() != null && getFrontCoverModel().getFrontCoverContent().getPhoto().getImageAsset().getOriginalImage().getImage().getUrl() != null) {
            headImage = getFrontCoverModel().getFrontCoverContent();
        } else {
            for (int i = 0; i < getRichTextLists().size(); i++) {
                SYFoodPhotoModel photo = getRichTextLists().get(i).getPhoto();
                if (photo != null) {
                    headImage = getRichTextLists().get(i);
                    break;
                }
            }
        }
        if (headImage == null) {
            return null;
        }
        return headImage.getPhoto().getImageAsset().pullProcessedImage().getImage();
    }
}
