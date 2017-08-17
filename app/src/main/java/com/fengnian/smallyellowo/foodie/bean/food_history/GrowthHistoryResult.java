package com.fengnian.smallyellowo.foodie.bean.food_history;

import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-3-24
 * 2017-5-24最后同步
 */
public class GrowthHistoryResult extends BaseResult {
    SYImage coverImage;

    ArrayList<Object> growthRecordList;

    public SYImage getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(SYImage coverImage) {
        this.coverImage = coverImage;
    }

    public ArrayList<Object> getGrowthRecordList() {
        return growthRecordList;
    }

    public void setGrowthRecordList(ArrayList<Object> growthRecordList) {
        this.growthRecordList = MultiClassParser.parse(growthRecordList, new Class[]{SYFoodGrowthRecord.class,
                SYFulfillGrowthRecord.class,
                SYCommentGrowthRecord.class,
                SYWantEatGrowthRecord.class,
                SYGoodChoiceGrowthRecord.class,
                SYConcernedFansGrowthRecord.class});
    }

}
