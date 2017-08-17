package com.fengnian.smallyellowo.foodie.bean.results;

import java.io.Serializable;

/**
 * Created by Administrator on 2016-8-31.
 */

public class NeedToFriends implements Serializable {
    private String name;
    private String tel;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
