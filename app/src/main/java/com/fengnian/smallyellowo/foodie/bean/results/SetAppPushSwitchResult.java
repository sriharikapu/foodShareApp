package com.fengnian.smallyellowo.foodie.bean.results;

/**
 * Created by Administrator on 2016-12-21.
 */

public class SetAppPushSwitchResult extends BaseResult {

    private  String isType;
    private  String isOpen;

    public String getIsType() {
        return isType;
    }

    public void setIsType(String isType) {
        this.isType = isType;
    }

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }
}
