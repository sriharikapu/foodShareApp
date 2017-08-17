package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;

import java.util.List;

/**
 * Created by Administrator on 2016-9-13.
 */

public class InviteFoodListDetailResult extends BaseResult {

    private List<SYFeed> feed;

    public List<SYFeed> getFeed() {
        return feed;
    }

    public void setFeed(List<SYFeed> feed) {
        this.feed = feed;
    }
}
