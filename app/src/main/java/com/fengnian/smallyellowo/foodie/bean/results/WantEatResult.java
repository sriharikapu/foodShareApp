package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publics.SYBusiness;

import java.util.List;

/**
 * Created by Administrator on 2016-8-26.
 */
public class WantEatResult extends BaseResult {
    private String unEatNumber;
    private String eatNumber;
    private List<SYBusiness> list;

    public String getUnEatNumber() {
        return unEatNumber;
    }

    public void setUnEatNumber(String unEatNumber) {
        this.unEatNumber = unEatNumber;
    }

    public String getEatNumber() {
        return eatNumber;
    }

    public void setEatNumber(String eatNumber) {
        this.eatNumber = eatNumber;
    }

    public List<SYBusiness> getList() {
        return list;
    }

    public void setList(List<SYBusiness> list) {
        this.list = list;
    }
}
