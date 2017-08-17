package com.fengnian.smallyellowo.foodie.receivers;

import com.fan.framework.base.XData;

import java.util.ArrayList;

/**
 * Created by lanbiao on 2017/1/10.
 */

public class SYSchemeData extends XData {
    private int host;
    private ArrayList<String> params;

    public void setHost(int host) {
        this.host = host;
    }

    public int getHost() {
        return host;
    }

    public ArrayList<String> getParams() {
        return params;
    }

    public void setParams(ArrayList<String> params) {
        this.params = params;
    }
}
