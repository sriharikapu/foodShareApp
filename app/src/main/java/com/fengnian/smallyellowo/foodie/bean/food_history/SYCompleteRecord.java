package com.fengnian.smallyellowo.foodie.bean.food_history;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-3-29.
 * 2017-5-24最后同步
 */

public class SYCompleteRecord extends BaseGrowthHistoryItem {
    public ArrayList<SYCompleteCount> getCompleteCountList() {
        if(completeCountList == null){
            completeCountList = new ArrayList<>();
        }
        return completeCountList;
    }

    public void setCompleteCountList(ArrayList<SYCompleteCount> completeCountList) {
        this.completeCountList = completeCountList;
    }

    public ArrayList<SYCompleteCount> completeCountList;//	已完成美食显示	否
}
