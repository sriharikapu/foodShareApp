package com.fengnian.smallyellowo.foodie.appbase;

import com.fan.framework.http.FFNetWorkRequest;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.wxapi.WeixinOpen;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Administrator on 2017-3-23.
 */

public class WeiChatOverdueManager {
    private static final Vector<FFNetWorkRequest> requests = new Vector<>();

    private static final int status_init = 0;
    private static final int status_executing = 1;
    //    private static final int status_dialog_showing = 2;
    private static final int status_failed = 3;
    private static final int status_success = 4;
    private static final int status_canceled = 5;

    private static final Object LOCK = new Object();

    private static String weiChatToken = null;

    private static int status = status_init;

    public static synchronized boolean onRequestFailed(final FFNetWorkRequest request) {
        request.setHasFailed(true);
        switch (status) {
            case status_init: {
                synchronized (LOCK) {
                    status = status_executing;
                }
                requests.add(request);
                startWeiChatRequest();
            }
            return true;
            case status_executing: {
                requests.add(request);
            }
            return true;
            case status_failed: {
                request.getCallBack().fail(request);
            }
            return false;
            case status_success: {
                reExcuteRequest(request);
            }
            return false;
            case status_canceled: {
                request.getCallBack().fail(request);
            }
            return false;
        }
        return false;
    }

    /**
     * 开启微信登录授权
     */
    private static void startWeiChatRequest() {
        WeixinOpen.getInstance().AuthLgin();
    }

    /**
     * 微信授权失败回调
     */
    public static void onWeiChatOssFail() {
        synchronized (LOCK) {
            status = status_failed;
        }
        while (requests.size() > 0) {
            FFNetWorkRequest request = requests.remove(0);
            request.getCallBack().fail(request);
        }
    }

    private static void reExcuteRequest(FFNetWorkRequest request) {
        boolean bExisit = false;
        ArrayList<String> params = new ArrayList<>();
        for (int i = 0; i < request.getParams().length; i += 2) {
            if (request.getParams()[i].equals("wechatCode")) {
                request.getParams()[i + 1] = SP.getWeiChatCode();
                bExisit = true;
                break;
            }
        }

        if(!bExisit){
            for(int i = 0; i < request.getParams().length ; i+=2){
                params.add(request.getParams()[i].toString());
                params.add(request.getParams()[i+1].toString());
            }
            params.add("wechatCode");
            params.add(SP.getWeiChatCode());
            request.setParams(params.toArray());
        }
        request.getNet().excute(request);
    }


    /**
     * 微信授权成功回调
     */
    public static void onWeiChatOssSuccess(String code) {
//        Activity topActivity = BaseActivity.getTopActivity();
//        String url = "";
//        String words = "";
//        FFNetWorkCallBack<LoginResult> callBack = new FFNetWorkCallBack<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult response, FFExtraParams extra) {
        synchronized (LOCK) {
            status = status_success;
        }
//                SP.setUser(response.getWeChatData().getUser());
        SP.setWeiChatCode(code);
        while (requests.size() > 0) {
            FFNetWorkRequest request = requests.remove(0);
            reExcuteRequest(request);
        }
//            }
//
//            @Override
//            public boolean onFail(FFExtraParams extra) {
//                onWeiChatOssFail();
//                return true;
//            }
//        };
//        Object[] params = {"wechatCode", code};
//        if (topActivity != null) {
//            if (topActivity instanceof BaseActivity) {
//                ((BaseActivity) topActivity).post(url, words, null, callBack, params);
//            } else if (topActivity instanceof BaseFragmentActivity) {
//                ((BaseFragmentActivity) topActivity).post(url, words, null, callBack, params);
//            }
//        } else {
//            APP.post(url, null, callBack, params);
//        }

    }

    /**
     * 微信授权取消回调
     */
    public static void onWeiChatOssCanceled() {
        synchronized (LOCK) {
            status = status_canceled;
        }
        while (requests.size() > 0) {
            FFNetWorkRequest request = requests.remove(0);
            request.getCallBack().fail(request);
        }
    }


    public static class LoginResult extends BaseResult {
        public LoginData getWeChatData() {
            return weChatData;
        }

        public void setWeChatData(LoginData weChatData) {
            this.weChatData = weChatData;
        }

        LoginData weChatData;
    }

    public static class LoginData extends BaseResult {
        public SYUser getUser() {
            return user;
        }

        public void setUser(SYUser user) {
            this.user = user;
        }

        SYUser user;
    }
}
