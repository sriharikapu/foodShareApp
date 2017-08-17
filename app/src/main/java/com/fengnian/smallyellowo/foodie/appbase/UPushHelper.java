package com.fengnian.smallyellowo.foodie.appbase;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.fan.framework.utils.FFLogUtil;
import com.fengnian.smallyellowo.foodie.MainActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chenglin on 2017-5-11.
 */

public class UPushHelper {
    Application application;

    public UPushHelper(Application app) {
        application = app;
    }

    public void init() {
        //初始化友盟推送---请勿在调用register方法时做进程判断处理（主进程和channel进程均需要调用register方法才能保证长连接的正确建立）
        //文档地址：http://dev.umeng.com/push/android/integration
        PushAgent mPushAgent = PushAgent.getInstance(application);
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                Log.v("友盟推送", "deviceToken = " + deviceToken);
                if (!TextUtils.isEmpty(deviceToken) && SP.isLogin()) {
                    UPushHelper.setPushAlias(application, SP.getUid());
                }
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });

        //通知栏通知处理
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                processCustomMessage(context, msg.custom);
            }

            @Override
            public void openActivity(Context context, UMessage msg) {
                super.openActivity(context, msg);
            }

            @Override
            public void launchApp(Context context, UMessage msg) {
                super.launchApp(context, msg);
                UTrack.getInstance(application).trackMsgClick(msg);//统计
                processCustomMessage(context, JSON.toJSONString(msg.extra));
            }

            @Override
            public void openUrl(Context context, UMessage uMessage) {
                super.openUrl(context, uMessage);
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        //透传消息
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                UTrack.getInstance(application).trackMsgClick(msg);//统计
                processCustomMessage(context, msg.custom);
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        MobclickAgent.openActivityDurationTrack(false);
    }

    /**
     * 设置用户id和device_token的一一映射关系，确保同一个alias只对应一台设备
     */
    public static void setPushAlias(final Context context, final String uid) {
        if (TextUtils.isEmpty(uid)) {
            return;
        }

        //设置Alias之前一定要先remove
        final PushAgent mPushAgent = PushAgent.getInstance(context);
        mPushAgent.removeAlias(uid, "uid", new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean isSuccess, String message) {
                mPushAgent.addAlias(uid, "uid", new UTrack.ICallBack() {
                    @Override
                    public void onMessage(boolean isSuccess, String message) {
                    }
                });
            }
        });
    }

    /**
     * 移除Alias
     */
    public static void removePushAlias(Context context, String uid) {
        PushAgent mPushAgent = PushAgent.getInstance(context);
        mPushAgent.removeAlias(uid, "uid", new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean isSuccess, String message) {

            }
        });
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, String extras) {
//        extras = "{\"pushType\":\"7\",\"userinfo\":\"{\\\"target\\\":\\\"http://web-fzfiles.tinydonuts.cn/record/Hunan_cuisine.html?pgcId=78\\\",\\\"pgcId\\\":78}\"}";
//        Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//        if (!TextUtils.isEmpty(extras)) {
//            try {
//                JSONObject extraJson = new JSONObject(extras);
//                if (null != extraJson && extraJson.length() > 0) {
//                    msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//                }
//            } catch (JSONException e) {
//                FFLogUtil.e("发送广播失败.....", "");
//            }
//
//        }
//        context.sendBroadcast(msgIntent);
    }
}
