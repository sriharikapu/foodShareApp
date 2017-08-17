package com.fengnian.smallyellowo.foodie.usercenter.bean;

import android.text.TextUtils;

/**
 * Created by elaine on 2017/6/6.
 */

public class AccountBean {
    private String accountType;
    private String accountName;
    private int accountImage;
    private String accountTypeName;
    private boolean isBinded = false;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountName() {
        if (TextUtils.isEmpty(accountName)) {
            accountName = "马上绑定";
        }
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getAccountImage() {
        return accountImage;
    }

    public void setAccountImage(int accountImage) {
        this.accountImage = accountImage;
    }

    public String getAccountTypeName() {
        return accountTypeName;
    }

    public void setAccountTypeName(String accountTypeName) {
        this.accountTypeName = accountTypeName;
    }

    public boolean isBinded() {
        return isBinded;
    }

    public void setBinded(boolean binded) {
        isBinded = binded;
    }
}
