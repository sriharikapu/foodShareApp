package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publics.SYPhotoPrecisionModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-6-26.
 */

public class RecognizeResult extends BaseResult{
    public ArrayList<SYPhotoPrecisionModel> getList() {
        return list;
    }

    public void setList(ArrayList<SYPhotoPrecisionModel> list) {
        this.list = list;
    }

    private ArrayList<SYPhotoPrecisionModel> list;
}
