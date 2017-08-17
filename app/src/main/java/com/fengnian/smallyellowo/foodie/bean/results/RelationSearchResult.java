package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publics.SYThinkUser;

import java.util.List;

/**
 * Created by Administrator on 2016-9-9.
 */

public class RelationSearchResult extends BaseResult {
    private List<SYThinkUser> thinkUser;

    public List<SYThinkUser> getThinkUser() {
        return thinkUser;
    }

    public void setThinkUser(List<SYThinkUser> thinkUser) {
        this.thinkUser = thinkUser;
    }
}
