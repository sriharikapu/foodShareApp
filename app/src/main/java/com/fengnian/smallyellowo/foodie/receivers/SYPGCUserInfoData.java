package com.fengnian.smallyellowo.foodie.receivers;

/**
 * Created by lanbiao on 2016/12/20.
 * PGC推送消息结构
 */

public class SYPGCUserInfoData extends SYBaseUserInfoData {
    private String target;
    private String pgcId;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getPgcId() {
        return pgcId;
    }

    public void setPgcId(String pgcId) {
        this.pgcId = pgcId;
    }
}
