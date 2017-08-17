package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;

import java.util.List;

/**
 * Created by Administrator on 2016-9-11.
 */

public class MyAllAttionResult extends BaseResult  {
   private List<SYUser> users;
    private String  lastId;

    public List<SYUser> getUsers() {
        return users;
    }

    public void setUsers(List<SYUser> users) {
        this.users = users;
    }

    public String getLastId() {
        return lastId;
    }

    public void setLastId(String lastId) {
        this.lastId = lastId;
    }
}
