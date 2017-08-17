package com.fengnian.smallyellowo.foodie.bean.publics;

import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lanbiao on 16/8/15.
 */
public class UnReadNum extends BaseResult implements Serializable {

    public int getUnReadNum() {
        return unReadNum;
    }

    public void setUnReadNum(int unReadNum) {
        this.unReadNum = unReadNum;
    }

    public int unReadNum;
}
