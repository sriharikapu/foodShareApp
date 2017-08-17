package com.fengnian.smallyellowo.foodie.bean;

import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;

/**
 * Created by Administrator on 2016-11-30.
 */

public class PersionalSYFeed {
    public boolean sharedToAct; //是否分享
    public PersionSYfeedC syNewFeed;
    public boolean isSharedToAct() {
        return sharedToAct;
    }

    public void setSharedToAct(boolean sharedToAct) {
        this.sharedToAct = sharedToAct;
    }

    public SYFeed getSyNewFeed() {
        syNewFeed.setSharedToAct(this.isSharedToAct());
        return syNewFeed;
    }

    public void setSyNewFeed(PersionSYfeedC syNewFeed) {
        this.syNewFeed = syNewFeed;
    }
}
