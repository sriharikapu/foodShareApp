package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;

/**
 * Created by Administrator on 2016-9-11.
 */
public class DynamicDetailResult extends BaseResult {
    public SYFeed getFeed() {
        return feed;
    }

    public void setFeed(SYFeed feed) {
        this.feed = feed;
    }

    private SYFeed feed;
}
