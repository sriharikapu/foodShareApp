package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publics.AddFriends;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;

import java.util.List;

/**
 * 添加朋友
 */

public class AddFriendsResult extends BaseResult {
    private List<AddFriends> followOthers;
    private List<SYUser> invateUser;

    public List<SYUser> getInvateUser() {
        return invateUser;
    }

    public void setInvateUser(List<SYUser> invateUser) {
        this.invateUser = invateUser;
    }

    public List<AddFriends> getFollowOthers() {
        return followOthers;
    }

    public void setFollowOthers(List<AddFriends> followOthers) {
        this.followOthers = followOthers;
    }
}
