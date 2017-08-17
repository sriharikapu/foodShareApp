package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.foodClub.ClubItem;
import com.fengnian.smallyellowo.foodie.bean.foodClub.ClubItemsAD;

import java.util.ArrayList;

/**
 * Created by lanbiao on 2017/5/5.
 * 美食俱乐部专栏
 */
public class SYFoodClubResult extends BaseResult {

    public ArrayList<ClubItem> getData() {
        return data;
    }

    public void setData(ArrayList<ClubItem> data) {
        this.data = data;
    }

    private ArrayList<ClubItem> data;
}
