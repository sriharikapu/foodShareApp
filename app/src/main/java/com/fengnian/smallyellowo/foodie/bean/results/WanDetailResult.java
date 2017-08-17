package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publics.WanDetailDyn;

import java.util.List;

/**
 * Created by Administrator on 2016-11-12.
 */

public class WanDetailResult extends BaseResult {

    List<WanDetailDyn> foodRecords;

    public List<WanDetailDyn> getFoodRecords() {
        return foodRecords;
    }

    public void setFoodRecords(List<WanDetailDyn> foodRecords) {
        this.foodRecords = foodRecords;
    }
}
