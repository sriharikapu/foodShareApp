package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publics.PersionInfo;

/**
 * Created by Administrator on 2016-9-3.
 */

public class UpdateUserInfoResult extends BaseResult{
    private PersionInfo user;
    public PersionInfo getUser() {
        return user;
    }

    public void setUser(PersionInfo user) {
        this.user = user;
    }

    private String  result;
    private String returnMessage;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }
}
