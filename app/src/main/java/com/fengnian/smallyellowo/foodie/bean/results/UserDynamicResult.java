package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户的动态列表
 * Created by Administrator on 2016-9-1.
 */
public class UserDynamicResult extends BaseResult {

    private ArrayList<UserDynamicGroup> myfeeds;

    public ArrayList<SYFeed> getFeeds() {
        ArrayList<SYFeed> feeds = new ArrayList<>();
        for (UserDynamicGroup group : myfeeds) {
            feeds.addAll(group.getFeeds());
        }
        return feeds;
    }

    public ArrayList<UserDynamicGroup> getMyfeeds() {
        return myfeeds;
    }

    public void setMyfeeds(ArrayList<UserDynamicGroup> myfeeds) {
        this.myfeeds = myfeeds;
    }

    public static class UserDynamicGroup {
        private String createTime;

        private List<SYFeed> feeds;

        public List<SYFeed> getFeeds() {
            return feeds;
        }

        public void setFeeds(List<SYFeed> feeds) {
            this.feeds = feeds;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
