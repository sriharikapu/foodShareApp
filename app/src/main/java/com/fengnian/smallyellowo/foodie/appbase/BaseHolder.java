package com.fengnian.smallyellowo.foodie.appbase;

import android.view.View;

/**
 * Created by Administrator on 2016-8-11.
 */
public class BaseHolder {
    View v;

    public void setView(View v) {
        this.v = v;
        v.setTag(this);
    }

    public View findViewById(int id) {
        return v.findViewById(id);
    }
}
