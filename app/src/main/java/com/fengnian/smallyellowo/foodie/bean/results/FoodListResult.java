package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publics.FoodListBean;

import java.util.List;

/**
 * Created by Administrator on 2016-9-7.
 */

public class FoodListResult extends BaseResult {

    private List<FoodListBean> syFoodNotesClassifications;

    public List<FoodListBean> getSyFoodNotesClassifications() {
        return syFoodNotesClassifications;
    }

    public void setSyFoodNotesClassifications(List<FoodListBean> syFoodNotesClassifications) {
        this.syFoodNotesClassifications = syFoodNotesClassifications;
    }
}
