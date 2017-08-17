package com.fengnian.smallyellowo.foodie.bean.publics;

import java.util.List;

/**
 * Created by Administrator on 2016-11-14.
 */

public class ContactPeople {

    private String name;//联系人姓名

    private List<String> nummber_list;//联系人信息

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNummber_list() {
        return nummber_list;
    }

    public void setNummber_list(List<String> nummber_list) {
        this.nummber_list = nummber_list;
    }
}
