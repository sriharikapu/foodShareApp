package com.fengnian.smallyellowo.foodie.bean.results;

/**
 * Created by Administrator on 2016-12-20.
 */

public class IsPushResult extends BaseResult {

    private boolean isReview;//评论推送
    private boolean isAppPush;//是否允许APP推送
    private boolean isPGC;//是否接受app通知
    private boolean isAppNotice;//是否接受美食志内容更新

    public boolean isReview() {
        return isReview;
    }

    public void setReview(boolean review) {
        isReview = review;
    }

    public boolean isAppPush() {
        return isAppPush;
    }

    public void setAppPush(boolean appPush) {
        isAppPush = appPush;
    }

    public boolean isPGC() {
        return isPGC;
    }

    public void setPGC(boolean PGC) {
        isPGC = PGC;
    }

    public boolean isAppNotice() {
        return isAppNotice;
    }

    public void setAppNotice(boolean appNotice) {
        isAppNotice = appNotice;
    }
}
