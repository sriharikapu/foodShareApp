package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publics.DotInfo;

import java.util.List;

/**
 * Created by Administrator on 2016-12-28.
 */

public class DeliciousFoodListResult extends BaseResult {


    private List<DotInfo> list;
//    DeliciousFoodMapBean
    public List<DotInfo> getList() {
        return list;
    }

    public void setList(List<DotInfo> list) {
        this.list = list;
    }

    private  int hasMore;//  -1 没有数据了

    public int getHasMore() {
        return hasMore;
    }

    public void setHasMore(int hasMore) {
        this.hasMore = hasMore;
    }
}
