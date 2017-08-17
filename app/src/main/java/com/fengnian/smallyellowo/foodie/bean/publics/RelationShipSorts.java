package com.fengnian.smallyellowo.foodie.bean.publics;

import java.util.ArrayList;

/**
 * Created by lanbiao on 16/8/10.
 */
public class RelationShipSorts {
    private String key;
        private ArrayList<SYUser> users;

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setUsers(ArrayList<SYUser> users) {
        this.users = users;
    }

    public ArrayList<SYUser> getUsers() {
        return users;
    }
}
