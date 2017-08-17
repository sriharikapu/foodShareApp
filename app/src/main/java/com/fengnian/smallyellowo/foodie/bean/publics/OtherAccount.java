package com.fengnian.smallyellowo.foodie.bean.publics;

import android.text.TextUtils;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elaine on 2017/6/9.
 * 第三方账号
 */

public class OtherAccount extends BaseResult {
    public List<Account> accountDataList;

    public List<Account> getAccountDataList() {
        if (accountDataList == null){
            accountDataList = new ArrayList<>();
        }
        return accountDataList;
    }

    public void setAccountDataList(List<Account> accountDataList) {
        this.accountDataList = accountDataList;
    }

    public static class Account {
        public int accountType; // 1:微信； 2：qq； 3：微博
        public String accountName; // 账号昵称
        public String platformName; // 第三方平台名称 （备用字段, 平台数量比较少，后台可以不返回）
        public String platformImageUrl; // 第三方平台图片 （备用字段, 平台数量比较少，后台可以不返回）
        public boolean binded; // 是否绑定

        public int getType() {
            return accountType;
        }

        public void setType(int type) {
            this.accountType = type;
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

        public String getPlatformName() {
            return platformName;
        }

        public void setPlatformName(String platformName) {
            this.platformName = platformName;
        }

        public String getPlatformImageUrl() {
            return platformImageUrl;
        }

        public void setPlatformImageUrl(String platformImageUrl) {
            this.platformImageUrl = platformImageUrl;
        }

        public boolean isBinded() {
            return binded;
        }

        public void setBinded(boolean binded) {
            this.binded = binded;
        }

        public int getPlatformImage() {
            int image = -1;
            switch (accountType) {

                case 1:
                    image = R.mipmap.icon_login_wechat;
                    break;

                case 2:
                    image = R.mipmap.icon_login_qq;
                    break;

                case 3:
                    image = R.mipmap.icon_login_sina;
                    break;
            }

            return image;
        }

        public String getPlatform() {
            String name = "";
            switch (accountType) {

                case 1:
                    name = "微信";
                    break;

                case 2:
                    name = "QQ";
                    break;

                case 3:
                    name = "微博";
                    break;
            }

            return name;
        }
    }
}
