package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;

import java.util.List;

/**
 * 关注或者粉丝
 */

public class AttionOrFansResult extends BaseResult {

    private List<SYUser> users;

    private  Long   lastId;

    public List<SYUser> getUsers() {
        return users;
    }

    public void setUsers(List<SYUser> users) {
        this.users = users;
    }

    public Long getLastid() {
        return lastId;
    }

    public void setLastid(Long lastId) {
        this.lastId = lastId;
    }
}
