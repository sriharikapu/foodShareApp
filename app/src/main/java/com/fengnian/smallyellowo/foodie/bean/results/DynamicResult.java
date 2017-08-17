package com.fengnian.smallyellowo.foodie.bean.results;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-7-28.
 */
public class DynamicResult extends  BaseResult{

    public ArrayList<SYFeed> getFeeds() {
        return feeds;
    }

    public void setFeeds(ArrayList<SYFeed> feeds) {
        this.feeds = feeds;
    }

    ArrayList<SYFeed> feeds;

    @Override
    public boolean isNoData() {
        return FFUtils.isListEmpty(feeds);
    }
}
