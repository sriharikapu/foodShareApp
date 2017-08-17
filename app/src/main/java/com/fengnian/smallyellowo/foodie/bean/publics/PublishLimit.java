package com.fengnian.smallyellowo.foodie.bean.publics;

import java.io.Serializable;

/**
 * Created by Administrator on 2016-12-5.
 */

public class PublishLimit implements Serializable {
    //0可以得分需要提示 1不能得分，提示0分
    private int limitScore;
    private  String msg;

    public int getLimitScore() {
        return limitScore;
    }

    public void setLimitScore(int limitScore) {
        this.limitScore = limitScore;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
