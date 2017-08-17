package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publics.SYBusiness;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFindMerchantTag;
import com.fengnian.smallyellowo.foodie.bean.publics.WanDetailDyn;

import java.util.List;

/**
 * Created by Administrator on 2017-4-18.
 */

public class GetWantDetailInfoOne extends BaseResult {

    private SYBusiness wEatModel;
    private List<WanDetailDyn> foodRecords;

    public List<SYFindMerchantTag> getFindMerchantTags() {
        return findMerchantTags;
    }

    public void setFindMerchantTags(List<SYFindMerchantTag> findMerchantTags) {
        this.findMerchantTags = findMerchantTags;
    }

    private List<SYFindMerchantTag> findMerchantTags;


//    private List<WantEatInfoAndfoodRecord>  foodRecords;
//
//    public List<WantEatInfoAndfoodRecord> getFoodRecords() {
//        return foodRecords;
//    }
//
//    public void setFoodRecords(List<WantEatInfoAndfoodRecord> foodRecords) {
//        this.foodRecords = foodRecords;
//    }

    public SYBusiness getwEatModel() {
        return wEatModel;
    }

    public void setwEatModel(SYBusiness wEatModel) {
        this.wEatModel = wEatModel;
    }

    public List<WanDetailDyn> getFoodRecords() {
        return foodRecords;
    }

    public void setFoodRecords(List<WanDetailDyn> foodRecords) {
        this.foodRecords = foodRecords;
    }
}
