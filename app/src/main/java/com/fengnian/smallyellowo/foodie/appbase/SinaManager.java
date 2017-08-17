package com.fengnian.smallyellowo.foodie.appbase;

import com.fan.framework.base.FFBaseActivity;
import com.fan.framework.base.FFContext;
import com.fan.framework.http.FFNetWorkRequest;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.wxapi.SinaOpen;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import java.util.HashMap;

/**
 * Created by elaine on 2017/6/15.
 */

public class SinaManager extends AbstrcPlatfromManager {

    private SsoHandler ssoHandler;

    public SsoHandler getSsoHandler() {
        return ssoHandler;
    }

    @Override
    void startReQuest(FFNetWorkRequest request) {
        if (!FFUtils.isListEmpty(FFContext.allActivis)) {
            AuthInfo info = new AuthInfo((FFBaseActivity) FFContext.allActivis.get(FFContext.allActivis.size() - 1), SinaOpen.APP_KEY, SinaOpen.REDIRECT_URL, SinaOpen.SCOPE);
            ssoHandler = new SsoHandler((FFBaseActivity) FFContext.allActivis.get(FFContext.allActivis.size() - 1), info);
            SinaOpen.sinaOauth(ssoHandler);
        } else {
            return;
        }

    }

    @Override
    HashMap<String, String> setRequestParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("weiboAccessToken", SP.getValue("weiboAccessToken"));
        map.put("refreshToken", SP.getValue("refreshToken"));
        return map;
    }

    @Override
    void onDestory() {
        if (ssoHandler != null) {
            ssoHandler = null;
        }
    }
}
