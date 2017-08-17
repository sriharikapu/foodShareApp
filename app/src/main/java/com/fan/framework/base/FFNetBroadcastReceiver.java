package com.fan.framework.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.fan.framework.utils.FFLogUtil;

/**
 * 判断网络连接发送广播
 *
 * @author maidoumi
 */
public class FFNetBroadcastReceiver extends BroadcastReceiver {

    private NetworkInfo info;
    private ConnectivityManager connectivityManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            connectivityManager = (ConnectivityManager) FFApplication.app.getSystemService(Context.CONNECTIVITY_SERVICE);
            info = connectivityManager.getActiveNetworkInfo();
            boolean isConnect = false;
            if (info != null) {
                isConnect = info != null && info.isAvailable();
            }
            FFLogUtil.e("网络状态", "网络状态已经改变info.isAvailable()" + isConnect);
            FFApplication.app.onNetStatusChanged(isConnect);
        }
    }
}
