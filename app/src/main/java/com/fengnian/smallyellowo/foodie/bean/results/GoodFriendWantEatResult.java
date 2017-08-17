package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publish.apiparams.GoodFriendWantListBean;

import java.util.List;

/**
 * Created by Administrator on 2016-11-14.
 */

public class GoodFriendWantEatResult extends BaseResult {
     private List<GoodFriendWantListBean> list;

    public List<GoodFriendWantListBean> getList() {
        return list;
    }

    public void setList(List<GoodFriendWantListBean> list) {
        this.list = list;
    }
}
