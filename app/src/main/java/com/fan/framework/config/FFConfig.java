package com.fan.framework.config;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Debug;

import com.alibaba.fastjson.JSON;
import com.fan.framework.base.FFApplication;
import com.fan.framework.http.FFNetWork;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.BuildConfig;
import com.fengnian.smallyellowo.foodie.appbase.CityPref;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.pm.ApplicationInfo.FLAG_LARGE_HEAP;
import static android.os.Build.VERSION_CODES.HONEYCOMB;
import static com.fan.framework.base.FFApplication.app;
import static java.lang.Runtime.getRuntime;

public class FFConfig {
    /**** 是否正式发布版本*/
    public static final boolean IS_OFFICIAL = false && !BuildConfig.DEBUG;
    /**
     * 是否打印控制台日志
     */
    public static final boolean LOG_ENABLE = !IS_OFFICIAL;
    /**
     * 是否弹出调试吐司
     */
    public static final boolean SHOW_DEBUG_TOAST = (!IS_OFFICIAL) && false;
    /**
     * 是否将日志打印到sd卡
     */
    public static boolean LOG2SD_ENABLE = !IS_OFFICIAL;
    /**
     * 数据库名称
     */
    public static final String DATABASE_NAME = "fframework.db";
    /**
     * 图片缓存路径
     */
    public static final String CACHE_DIR = ".ffPic/" + app.getPackageName();
    /**
     * 日志保存路径
     */
    public static final String LOG_DIR = "afLog/" + app.getPackageName();
    /**
     * 日志文件名称
     */
    public static final String LOG_FILE = "runtimeLog";
    /**
     * 图片同时加载线程数
     */
    public final static int THREADS = 3;
    /**
     * 图片sd卡缓存大小
     */
    public final static long maxSDCacheSize = 50 * 1024 * 1024;

    public static Object[] getParams(String url) {
        Object[] publicParams = new Object[]{
                "channel", Tool.getChannel(),
                "command", url.replace(Constants.shareConstants().getNetHeaderAdress(), ""),
                "clientID", FFUtils.getMyUUID(),
                "device", Build.MODEL,
                "osVersion", Build.VERSION.RELEASE,
                "source", "android",
                "cityCode", CityPref.getSelectedCity() != null ? CityPref.getSelectedCity().getId() : "",
                "cityName", CityPref.getSelectedCity() != null ? CityPref.getSelectedCity().getAreaName() : "",
                "visitorAccount", SP.getYoukeToken(),
                "version", FFUtils.getVerName()};
        Object[] params;
        if (!FFUtils.isStringEmpty(SP.getUid())) {
            if (SP.getWeiChatCode() != null) {
                params = new Object[]{
                        "wechatCode", SP.getWeiChatCode(),
                        "token", SP.getToken(),
                        "account", SP.getUid()
                };
            } else {
                params = new Object[]{
                        "token", SP.getToken(),
                        "account", SP.getUid()};
            }
        } else {
            return publicParams;
        }

        Object[] result = new Object[publicParams.length + params.length];
        System.arraycopy(publicParams, 0, result, 0, publicParams.length);
        System.arraycopy(params, 0, result, publicParams.length, params.length);
        return result;
    }

    /**
     * 图片缓存空间
     */
    public static int IMAGE_CATCHS = calculateMemoryCacheSize();
    /**
     * 图片缓存空间
     */
    public static int IMAGE_QUALITY = 60;

    /**
     * 获取超时时间
     */

    public static int getNetTimeOut() {
        switch (FFUtils.getCurrentNetType()) {
            case 1:// wifi
                return NetConfig.CONNECTION_TIMEOUT_WIFI;
            case 2:// 2G
                return NetConfig.CONNECTION_TIMEOUT_2G;
            case 3:// 3G
                return NetConfig.CONNECTION_TIMEOUT_3G;
            case 4:// 4G
                return NetConfig.CONNECTION_TIMEOUT_4G;
            default:
                return NetConfig.CONNECTION_TIMEOUT_2G;
        }
    }

    public static String getWebUA() {
        TreeMap<Object, Object> map = new TreeMap<>();
        FFNetWork.paramAddPublicParams(FFConfig.getParams("XXX"), new Object[0], map);
        map.remove("command");
        HashMap<String, Object> agent = new HashMap<>();
        agent.put("smallYellow", map);
        String s = " nkraHZDi" + JSON.toJSONString(agent) + "nkraHZDi";
        s = "";
        Set<Map.Entry<Object, Object>> entrys = map.entrySet();
        for (Map.Entry<Object, Object> entry : entrys) {
            s += " " + entry.getKey() + "/" + entry.getValue();
        }
        return s;
    }

    private static final class NetConfig {
        /**
         * wifi网络下超时时间
         */
        public static final int CONNECTION_TIMEOUT_WIFI = 30 * 1000;
        /**
         * 4G网络下超时时间
         */
        public static final int CONNECTION_TIMEOUT_4G = 30 * 1000;
        /**
         * 3G网络下超时时间
         */
        public static final int CONNECTION_TIMEOUT_3G = 30 * 1000;
        /**
         * 2G网络下超时时间
         */
        public static final int CONNECTION_TIMEOUT_2G = 60 * 1000;
    }

    public static int calculateMemoryCacheSize() {
        ActivityManager am = (ActivityManager) app.getSystemService(Context.ACTIVITY_SERVICE);
        boolean largeHeap = (app.getApplicationInfo().flags & ApplicationInfo.FLAG_LARGE_HEAP) != 0;
        int memoryClass = am.getMemoryClass();
        if (largeHeap && Build.VERSION.SDK_INT >= HONEYCOMB) {
            memoryClass = am.getLargeMemoryClass();
        }
        return 1024 * 1024 * memoryClass / 7;
    }
}
