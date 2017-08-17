package com.fengnian.smallyellowo.foodie.bean.publics;

import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lanbiao on 16/8/15.
 */
public class RecommendPeople2 extends BaseResult implements Serializable {

    public String serverMsg;

    @Override
    public String getServerMsg() {
        return serverMsg;
    }

    @Override
    public void setServerMsg(String serverMsg) {
        this.serverMsg = serverMsg;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<RecommendUser> getList() {
        return list;
    }

    public void setList(List<RecommendUser> list) {
        this.list = list;
    }

    public int errorCode;
    public List<RecommendUser> list;

    public class RecommendUser {
        public int commonFriends;
        public int readStatu;
        public SYUser user;

        public SYUser getUser() {
            return user;
        }

        public void setUser(SYUser user) {
            this.user = user;
        }

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
    }


}
