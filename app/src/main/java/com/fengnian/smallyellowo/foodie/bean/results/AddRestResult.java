package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publics.SYBusiness;

/**
 * Created by Administrator on 2016-9-20.
 */
public class AddRestResult extends BaseResult {

    public SYBusiness getFoodeat() {
        return foodeat;
    }

    public void setFoodeat(SYBusiness foodeat) {
        this.foodeat = foodeat;
    }

    SYBusiness foodeat;
}
