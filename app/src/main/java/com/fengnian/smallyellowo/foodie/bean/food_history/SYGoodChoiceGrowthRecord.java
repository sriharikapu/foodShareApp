package com.fengnian.smallyellowo.foodie.bean.food_history;

import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;

/**
 * Created by Administrator on 2017-3-24.
 * 2017-5-24最后同步
 * 被设置为精选的历程对象
 */

public class SYGoodChoiceGrowthRecord  extends SYCompleteRecord{
    public String title;//	被设置为精选的内容	是
    public String feedID;//	被设置为精选内容的动态id	是
    public int type;//	美食样式 1速记 2富文本	是
    public int releaseTemplateType;//	富文本模板样式	是
    //	模板类型（0：标准 1：现代 2:泼墨 3:中式 4:中式2  5:简短  6:简短2  7：简短2-2）
    public String foodNoteID;//	被设置为精选内容的个人中心id	是
    public SYImage coverImage;//	封面对象	是
    public int imageCount;//	图片张数	是
    public double integral;//	奖励积分	是

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFeedID() {
        return feedID;
    }

    public void setFeedID(String feedID) {
        this.feedID = feedID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getReleaseTemplateType() {
        return releaseTemplateType;
    }

    public void setReleaseTemplateType(int releaseTemplateType) {
        this.releaseTemplateType = releaseTemplateType;
    }

    public String getFoodNoteID() {
        return foodNoteID;
    }

    public void setFoodNoteID(String foodNoteID) {
        this.foodNoteID = foodNoteID;
    }

    public SYImage getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(SYImage coverImage) {
        this.coverImage = coverImage;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public double getIntegral() {
        return integral;
    }

    public void setIntegral(double integral) {
        this.integral = integral;
    }

}
