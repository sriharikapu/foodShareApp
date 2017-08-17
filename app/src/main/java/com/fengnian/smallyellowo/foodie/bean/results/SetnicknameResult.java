package com.fengnian.smallyellowo.foodie.bean.results;

/**
 * Created by Administrator on 2016-9-19.
 */

public class SetnicknameResult extends BaseResult {

    private String  message;
    private String  restate;
    private String  redata;

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

    public String getRedata() {
        return redata;
    }

    public void setRedata(String redata) {
        this.redata = redata;
    }
}
