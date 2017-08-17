package com.fengnian.smallyellowo.foodie.bean.publish.apiparams;

import com.fengnian.smallyellowo.foodie.bean.publish.GoodFriendWantEatBean;

/**
 * Created by Administrator on 2016-11-14.
 */

public class GoodFriendWantListBean {

    private GoodFriendWantEatBean user;
    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public GoodFriendWantEatBean getUser() {
        return user;
    }

    public void setUser(GoodFriendWantEatBean user) {
        this.user = user;
    }
}
