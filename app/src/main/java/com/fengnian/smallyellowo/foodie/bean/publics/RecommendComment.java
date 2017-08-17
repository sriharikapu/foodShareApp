package com.fengnian.smallyellowo.foodie.bean.publics;

import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lanbiao on 16/8/15.
 */
public class RecommendComment extends BaseResult implements Serializable {
    public int errorCode;
    public String serverMsg;
    public ArrayList<SYUser> list;

    public RecommendComment() {
    }

    public RecommendComment(String serverMsg, int errorCode, ArrayList<SYUser> commentuser) {
        this.serverMsg = serverMsg;
        this.errorCode = errorCode;
        list = commentuser;
    }

    public RecommendComment(ArrayList<SYUser> commentuser) {
        list = commentuser;
    }

    public RecommendComment(int errorCode) {
        this.errorCode = errorCode;
    }

    public RecommendComment(String serverMsg) {
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

    @Override
    public String getServerMsg() {
        return serverMsg;
    }

    @Override
    public void setServerMsg(String serverMsg) {
        this.serverMsg = serverMsg;
    }

    public ArrayList<SYUser> getCommentuser() {
        return list;
    }

    public void setCommentuser(ArrayList<SYUser> commentuser) {
        list = commentuser;
    }
}
