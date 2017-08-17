package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publics.SendVerificationCodeTwo;

/**
 * Created by Administrator on 2016-9-19.
 */

public class SendVerificationCodeResult extends BaseResult {



    private  String   message ;
    private  String   restate ;
    private SendVerificationCodeTwo redata ;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRestate() {
        return restate;
    }

    public void setRestate(String restate) {
        this.restate = restate;
    }

    public SendVerificationCodeTwo getRedata() {
        return redata;
    }

    public void setRedata(SendVerificationCodeTwo redata) {
        this.redata = redata;
    }
}
