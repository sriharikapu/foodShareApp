package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publics.InviteUserList;

import java.util.List;

/**
 * Created by Administrator on 2016-9-11.
 */

public class InviteStusResult extends BaseResult {
    private List<InviteUserList> inviteUsers;

    private String numOfRemainderInvite;

    public List<InviteUserList> getInviteUsers() {
        return inviteUsers;
    }

    public void setInviteUsers(List<InviteUserList> inviteUsers) {
        this.inviteUsers = inviteUsers;
    }

    public String getNumOfRemainderInvite() {
        return numOfRemainderInvite;
    }

    public void setNumOfRemainderInvite(String numOfRemainderInvite) {
        this.numOfRemainderInvite = numOfRemainderInvite;
    }
}
