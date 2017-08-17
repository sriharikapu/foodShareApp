package com.fan.framework.config;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.fan.framework.utils.FFLogUtil;
import com.fengnian.smallyellowo.foodie.appbase.APP;
import com.fengnian.smallyellowo.foodie.utils.Parser;

public class Tool {
//    private static String channel = null;

    /**
     * 获取渠道名称
     *
     * @param ctx
     * @return
     */
    public static String getChannelName(Context ctx) {
        if (ctx == null) {
            return null;
        }
        String channelName = "cn_guangwang";
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        Object channel = applicationInfo.metaData.getString("UMENG_CHANNEL");
                        if (channel instanceof String) {
                            channelName = (String) channel;
                        } else {
                            channelName = String.valueOf(channel);
                        }

                        FFLogUtil.e("UMENG_CHANNEL", channelName);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelName;
    }


    /*public static String getChannelName(){
        if (getChannelAllName() != null){
            return getChannelAllName()[0];
        }

        return "guanwang";
    }*/

    /*public static String getChannel() {
        if (getChannelAllName() != null && getChannelAllName().length > 1){
            return getChannelAllName()[1];
        }

        return "9998";
    }*/

    public static Object getChannel() {
        try {
            return APP.app.getPackageManager().getApplicationInfo(APP.app.getPackageName(), PackageManager.GET_META_DATA).metaData.get("CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "cn_guangwang";
//        if (channel != null) {
//            return channel;
//        }
//
//        final String start_flag = "META-INF/channel_";
//        ApplicationInfo appinfo = FFApplication.app.getApplicationInfo();
//        String sourceDir = appinfo.sourceDir;
//        ZipFile zipfile = null;
//        try {
//            zipfile = new ZipFile(sourceDir);
//            Enumeration<?> entries = zipfile.entries();
//            while (entries.hasMoreElements()) {
//                ZipEntry entry = ((ZipEntry) entries.nextElement());
//                String entryName = entry.getName();
//                if (entryName.contains(start_flag)) {
//                    channel = entryName.replace(start_flag, "");
//                    break;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (zipfile != null) {
//                try {
//                    zipfile.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        if (channel == null || channel.length() <= 0) {
//            channel = "cn_guangwang";//读不到渠道号就默认是官方渠道
//        }
//        return channel;
    }
}