package com.fengnian.smallyellowo.foodie.bean.publics;

import java.io.Serializable;

/**
 * Created by Administrator on 2016-8-30.
 */

public class AttentionRecord implements Serializable {
    private String id;
    private String concernedAccount;
    private String beConcernedAccount;
    private String attentionTime;
    private String attentionTimeStr;
    private String status;
    private User user;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConcernedAccount() {
        return concernedAccount;
    }

    public void setConcernedAccount(String concernedAccount) {
        this.concernedAccount = concernedAccount;
    }

    public String getBeConcernedAccount() {
        return beConcernedAccount;
    }

    public void setBeConcernedAccount(String beConcernedAccount) {
        this.beConcernedAccount = beConcernedAccount;
    }

    public String getAttentionTime() {
        return attentionTime;
    }

    public void setAttentionTime(String attentionTime) {
        this.attentionTime = attentionTime;
    }

    public String getAttentionTimeStr() {
        return attentionTimeStr;
    }

    public void setAttentionTimeStr(String attentionTimeStr) {
        this.attentionTimeStr = attentionTimeStr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
