package com.fengnian.smallyellowo.foodie.appbase;

import android.content.Intent;

import com.fan.framework.base.FFBaseActivity;
import com.fan.framework.base.FFContext;
import com.fan.framework.http.FFNetWorkRequest;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.QQSdkUtil;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by elaine on 2017/6/15.
 */

public class TencentManager extends AbstrcPlatfromManager {
    private QQSdkUtil qqSdkUtil;
    private LoginListener listener;
    private FFBaseActivity activity;

    public TencentManager() {
    }

    public LoginListener getListener() {
        return listener;
    }

    @Override
    void startReQuest(FFNetWorkRequest request) {
        if (!FFUtils.isListEmpty(FFContext.allActivis)) {
            activity = (FFBaseActivity) FFContext.allActivis.get(FFContext.allActivis.size()-1);
            qqSdkUtil = new QQSdkUtil(activity);
        } else {
            return;
        }
        listener = new LoginListener();
        qqSdkUtil.onOauth(listener );
    }

    @Override
    HashMap<String, String> setRequestParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("qqAccessToken", SP.getValue("qqAccessToken"));
        map.put("openId", SP.getValue("openId"));
        return map;
    }

    @Override
    void onDestory() {
        if (listener != null){
            listener = null;
        }
        if (qqSdkUtil != null) {
            qqSdkUtil.onDestory();
        }
    }

    // qq登录sdk 回调接口
    private class LoginListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            if (null == o) {
                return;
            }
            String token = null;
            String openId = null;
            try {
                JSONObject jsonResponse = (JSONObject) o;
                if (null != jsonResponse && jsonResponse.length() == 0) {
                    PlatformEngine.getInstance().getTencentManager().onOssFail();
                    return;
                }
                token = jsonResponse.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
                openId = jsonResponse.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("qqAccessToken", token);
                map.put("openId", openId);
                PlatformEngine.getInstance().getTencentManager().onOssSuccess(map);
            } catch (Exception e) {
                activity.showToast(e.getMessage());
            }
        }

        @Override
        public void onError(UiError uiError) {
            PlatformEngine.getInstance().getTencentManager().onOssFail();
        }

        @Override
        public void onCancel() {
            PlatformEngine.getInstance().getTencentManager().onOssCanceled();
        }
    }
}
