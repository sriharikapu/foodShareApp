package com.fengnian.smallyellowo.foodie.bean.food_history;

import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;

/**
 * Created by Administrator on 2017-3-24.
 * 2017-5-24最后同步
 * 想吃清单历程对象
 */

public class SYWantEatGrowthRecord extends SYCompleteRecord {
    public boolean bWantEatGrowthRecord;//		是
    public int foodtype;//	 1速记	是 2富文本
    public int releaseTemplateType;//	富文本模板样式	是 	模板类型（0：标准 1：现代 2:泼墨 3:中式 4:中式2  5:简短  6:简短2  7：简短2-2）
    public String merchantId;//
    public String eatId;//
    public SYUser user;//	想吃的用户对象	是
    public String title;//	想吃的标题	是
    public String feedID;//	动态的id	是
    public String foodNoteID;//	个人中心id	是
    public SYImage coverImage;//	 封面图对象	是
    public boolean isbWantEatGrowthRecord() {
        return bWantEatGrowthRecord;
    }

    public void setbWantEatGrowthRecord(boolean bWantEatGrowthRecord) {
        this.bWantEatGrowthRecord = bWantEatGrowthRecord;
    }

    public int getFoodtype() {
        return foodtype;
    }

    public void setFoodtype(int foodtype) {
        this.foodtype = foodtype;
    }

    public int getReleaseTemplateType() {
        return releaseTemplateType;
    }

    public void setReleaseTemplateType(int releaseTemplateType) {
        this.releaseTemplateType = releaseTemplateType;
    }

    public SYUser getUser() {
        return user;
    }

    public void setUser(SYUser user) {
        this.user = user;
    }

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

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getEatId() {
        return eatId;
    }

    public void setEatId(String eatId) {
        this.eatId = eatId;
    }
}
