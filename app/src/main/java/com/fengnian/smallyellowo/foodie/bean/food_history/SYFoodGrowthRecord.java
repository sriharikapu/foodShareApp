package com.fengnian.smallyellowo.foodie.bean.food_history;

import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-3-24.
 *2017-5-24最后同步
 * 富文本美食或速记美食历程对象
 */

public class SYFoodGrowthRecord  extends SYCompleteRecord{
    public String feedID;//	动态id	是
    public String foodNoteID;//	个人记录id 否
    public int type;//	美食样式 1速记 2富文本	是

    public int releaseTemplateType;//	富文本模板样式	是
    //	模板类型（0：标准 1：现代 2:泼墨 3:中式 4:中式2  5:简短  6:简短2  7：简短2-2）

    public boolean bShowCompleteEat;//	是否完成想吃	是
    public String content;//	历程文本描述	否
    public ArrayList<SYImage> coverImages;//	图片集合	是
    public float integral;//	奖励积分，	否

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

    public boolean isbShowCompleteEat() {
        return bShowCompleteEat;
    }

    public void setbShowCompleteEat(boolean bShowCompleteEat) {
        this.bShowCompleteEat = bShowCompleteEat;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<SYImage> getCoverImages() {
        return coverImages;
    }

    public void setCoverImages(ArrayList<SYImage> coverImages) {
        this.coverImages = coverImages;
    }

    public float getIntegral() {
        return integral;
    }

    public void setIntegral(float integral) {
        this.integral = integral;
    }
}
