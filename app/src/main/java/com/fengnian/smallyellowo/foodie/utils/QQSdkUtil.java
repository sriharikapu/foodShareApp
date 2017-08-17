package com.fengnian.smallyellowo.foodie.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.fan.framework.base.FFContext;
import com.fan.framework.base.Test.StringUtils;
import com.fan.framework.config.Tool;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWork;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.appbase.PlatformEngine;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.WeChatDataReult;
import com.fengnian.smallyellowo.foodie.login.LoginOneActivity;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by elaine on 2017/6/6.
 * qq登录及分享sdkManager
 */

public class QQSdkUtil implements ThreeLoginSDKUtil {
    public static final String NAME_STATUS = "name_status";
    public static final String QQ_TOKEN = "token";
    public static final String QQ_OPEN_ID = "open_id";
    private Tencent mTencent;
    private Context context;
    private FFNetWork mNet;
    private boolean isBind = false;

    public QQSdkUtil(Context context) {
        this.context = context;
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, context);
        mListener = new LoginListener();
        mNet = new FFNetWork((FFContext) context);
    }

    @Override
    public void login() {
        if (mTencent == null) {
            showToast("QQ sdk初始化失败");
            return;
        }

        if (context == null) {
            return;
        }

        isBind = false;

        mTencent.login((Activity) context, "all", mListener);
    }

    public void onOauth(IUiListener listener) {
        if (mTencent == null) {
            showToast("QQ sdk初始化失败");
            return;
        }

        if (context == null) {
            return;
        }

        mTencent.login((Activity) context, "all", listener);
    }

    @Override
    public void logout() {

    }

    @Override
    public void share() {

    }

    @Override
    public void bindAccount() {
        if (mTencent == null) {
            showToast("QQ sdk初始化失败");
            return;
        }

        if (context == null) {
            return;
        }
        isBind = true;
        mTencent.login((Activity) context, "all", mListener);
    }

    private LoginListener mListener;

    // qq登录sdk 回调接口
    private class LoginListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            if (null == o) {
                showToast("登录失败");
                return;
            }
            String token = null;
            String openId = null;
            try {
                JSONObject jsonResponse = (JSONObject) o;
                if (null != jsonResponse && jsonResponse.length() == 0) {
                    showToast("登录失败");
                    return;
                }
                token = jsonResponse.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
                openId = jsonResponse.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
            } catch (Exception e) {
                showToast(e.getMessage());
            }

//            if (isBind) {
                Intent intent = new Intent(IUrlUtils.Constans.LOAGIN_CODE_QQ);
                intent.putExtra(QQ_TOKEN, token);
                intent.putExtra(QQ_OPEN_ID, openId);
                context.sendBroadcast(intent);
//                return;
//            }
//            getQQUserInfo(token, openId);
        }

        @Override
        public void onError(UiError uiError) {
            showToast(uiError.errorMessage);
        }

        @Override
        public void onCancel() {
            if (isBind) {
                showToast("用户取消绑定账号");
                return;
            }
            showToast("用户取消登录");
        }
    }

    // 获取qq登录后的用户信息
    private void getQQUserInfo(String token, String openId) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
        mNet.post(IUrlUtils.Login.qqLogin, "", extra, new FFNetWorkCallBack<WeChatDataReult>() {
            @Override
            public void onSuccess(WeChatDataReult response, FFExtraParams extra) {
                if (response.judge()) {
                    SYUser us = response.getUser().getUser();
                    us.setToken(response.getUser().getToken());
                    SP.setUser(us);
                    SP.setLoginType("QQ");
                    HashMap<String, String> event = new HashMap<String, String>();
                    event.put("account", us.getId());
                    event.put("channel", Tool.getChannelName(context));
                    event.put("platform", "QQ");
                    MobclickAgent.onEvent(context, "Yellow_001", event);//统计新注册用户个数
                    sendBroadcast(response.getNickNameStaus());

                } else {
                    showToast(response.getServerMsg());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "openId", openId, "accessToken", token);
    }

    // 发送消息至UI层   更新UI
    private void sendBroadcast(int nickNameStatus) {
        Intent intent = new Intent(IUrlUtils.Constans.LOAGIN_CODE_QQ);
        intent.putExtra(NAME_STATUS, nickNameStatus);
        context.sendBroadcast(intent);
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public LoginListener getIUiListener() {
        return mListener;
    }

    public void onDestory() {
        if (mNet != null) {
            mNet.onDestory();
        }

        if (context != null) {
            context = null;
        }

        if (mListener != null){
            mListener = null;
        }
    }
}
