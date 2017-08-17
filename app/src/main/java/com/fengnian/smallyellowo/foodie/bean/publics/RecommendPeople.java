package com.fengnian.smallyellowo.foodie.bean.publics;

import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Created by lanbiao on 16/8/15.
 */
public class RecommendPeople extends BaseResult implements Serializable {

    private List<SYFriendRecommend>  SYFriendRecommendModel;

    public List<SYFriendRecommend> getSYFriendRecommendModel() {
        return SYFriendRecommendModel;
    }

    public void setSYFriendRecommendModel(List<SYFriendRecommend> SYFriendRecommendModel) {
        this.SYFriendRecommendModel = SYFriendRecommendModel;
    }

    public class SYFriendRecommend{

    int commonFriends;//共同好友数量	是
    int readStatu;
    SYUser user;

        public int getCommonFriends() {
            return commonFriends;
        }

        public void setCommonFriends(int commonFriends) {
            this.commonFriends = commonFriends;
        }

        public int getReadStatu() {
            return readStatu;
        }

        public void setReadStatu(int readStatu) {
            this.readStatu = readStatu;
        }

        public SYUser getUser() {
            return user;
        }

        public void setUser(SYUser user) {
            this.user = user;
        }
    }

}
