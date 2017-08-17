package com.fan.framework.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.fan.framework.base.FFApplication;
import com.fengnian.smallyellowo.foodie.appbase.APP;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.receivers.SYSchemeData;
import com.fengnian.smallyellowo.foodie.utils.Parser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.lasque.tusdk.core.TuSdkContext.getPackageName;


public class FFUtils {
    private static int avatartHight;
    private static Handler mHandler;

    public static <T> Class<T> getTClass(Object obj) {
        return getTClass(obj, 0);
    }

    public static <T> Class<T> getTClass(Object obj, int index) {
        Type[] actualTypeArguments = ((ParameterizedType) obj.getClass().getGenericSuperclass()).getActualTypeArguments();
        Class<T> tClass = (Class<T>) (actualTypeArguments[index]);
        return tClass;
    }

    /**
     * 获得app的打包时间
     *
     * @return
     */
    public static String getAppBuildTime() {
        String result = "";
        try {
            ApplicationInfo ai = APP.app.getPackageManager().getApplicationInfo(getPackageName(), 0);
            ZipFile zf = new ZipFile(ai.sourceDir);
            ZipEntry ze = zf.getEntry("META-INF/MANIFEST.MF");
            long time = ze.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
            result = formatter.format(new java.util.Date(time));
            zf.close();
        } catch (Exception e) {
        }

        return result;
    }

    //获取是否存在NavigationBar
    public static boolean isHasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = APP.app.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }

    //获取屏幕原始尺寸高度，包括虚拟功能键高度
    public static int getDpi() {
        int dpi = getDisHight();
        WindowManager windowManager = (WindowManager) APP.app.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    /**
     * 获得屏幕高度
     *
     * @return
     */
    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) APP.app
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获得屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) APP.app
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static void setSoftInputVis(View view, boolean vis) {
        InputMethodManager imm = (InputMethodManager) FFApplication.app.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (vis) {
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        } else {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void setSoftInputInvis(IBinder view) {
        InputMethodManager imm = (InputMethodManager) FFApplication.app.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view, 0);
    }

    // 计算出该TextView中文字的长度(像素)
    public static float getTextViewLength(TextView textView, String text) {
        if (text == null) {
            return 0;
        }
        TextPaint paint = textView.getPaint();
        // 得到使用该paint写上text的时候,像素为多少
        float textLength = paint.measureText(text);
        return textLength;
    }

    public static int getTextLine(TextView tv, String workingText, int width) {
        if (workingText == null) {
            return 0;
        }
        return new StaticLayout(workingText, tv.getPaint(), width - tv.getPaddingLeft() - tv.getPaddingRight(),
                Layout.Alignment.ALIGN_NORMAL, 1, 0, false).getLineCount();
    }

//    public static int getTextLine(TextView tv, String txt, int width) {
//        if (txt == null) {
//            return 0;
//        }
//        int length = (int) getTextViewLength(tv, txt);
//        return length / width + (length % width == 0 ? 0 : 1);
//    }

    public static void setText(TextView tv, String text) {
        if (FFUtils.isStringEmpty(text)) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
        }
    }

    public static void setText(TextView tv, String prefix, String text) {
        if (FFUtils.isStringEmpty(text)) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(prefix + text);
        }
    }

    public static void setText(TextView tv, String text, long number) {
        if (number == 0) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(text + number);
        }
    }

    public static void setText(TextView tv, long number, String text) {
        if (number == 0) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(number + text);
        }
    }

    /**
     * 得到当前的手机网络类型
     *
     * @return
     */
    public static int getCurrentNetType() {
        int type = -1;
        ConnectivityManager cm = (ConnectivityManager) FFApplication.app.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            type = -1;
        } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            type = 1;
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int subType = info.getSubtype();
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_UMTS:// 标准3G
                case TelephonyManager.NETWORK_TYPE_HSDPA:// 384kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:// 电信6Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_CDMA:// 电信3G
                case TelephonyManager.NETWORK_TYPE_EHRPD:// 3.9G
                case TelephonyManager.NETWORK_TYPE_HSPAP:// 5.76Mbps
                    type = 3;
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    type = 4;
                    break;
                case TelephonyManager.NETWORK_TYPE_GPRS:// 联通2G
                case TelephonyManager.NETWORK_TYPE_1xRTT:// 伪3G
                case TelephonyManager.NETWORK_TYPE_EDGE:// 移动2G
                case TelephonyManager.NETWORK_TYPE_IDEN:// 9.6Kbps
                case TelephonyManager.NETWORK_TYPE_HSPA:// 上下行速率分别为64kbps和384kbps
                default:
                    type = 2;
                    break;
            }
        }
        return type;
    }

    /**
     * 获取给定时间戳当天0点的时间戳
     *
     * @param timestamp
     * @return
     */
    public static long get0HourTime(long timestamp) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

//    /**
//     * 连接wifi
//     *
//     * @param ssid
//     * @param password
//     */
//    public static void connectWifi(String ssid, String password) {
//        if (isStringEmpty(ssid) || isStringEmpty(password)) {
//            return;
//        }
//        try {
//            WifiManager wifiManager = (WifiManager) FFApplication.app.getSystemService(Context.WIFI_SERVICE);
//            WifiConfiguration config = new WifiConfiguration();
//            config.SSID = "\"" + ssid + "\"";
//            config.preSharedKey = "\"" + password + "\"";
//            config.hiddenSSID = false;
//            config.status = WifiConfiguration.Status.ENABLED;
//            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
//            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
//            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
//            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
//            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
//            wifiManager.enableNetwork(wifiManager.addNetwork(config), false);
//        } catch (Exception e) {
//
//        }
//    }

    /**
     * 保留两位小数，如果为整数返回整数
     *
     * @param f
     * @return
     */
    public static String getSubFloat(double f) {
        return getSubFloat(f, 2, false);
    }

    /**
     * @param f
     * @param max      小数点后保留位数
     * @param needFill 是否补位
     * @return
     */
    public static String getSubFloat(double f, int max, boolean needFill) {
        return getSubFloat(f, max, needFill, true);
    }

    /**
     * @param f
     * @param max      小数点后保留位数
     * @param needFill 是否补位
     * @param isCut    直接截取还是四舍五入
     * @return
     */
    public static String getSubFloat(double f, int max, boolean needFill, boolean isCut) {
        if (max <= 0) {
            return String.valueOf((int) f);
        }
        char[] chars = new char[max];
        for (int i = 0; i < max; i++) {
            chars[i] = needFill ? '0' : '#';
        }
        DecimalFormat fnum = new DecimalFormat("##0." + new String(chars));
        if (isCut) {
            fnum.setRoundingMode(RoundingMode.DOWN);
        } else {
            fnum.setRoundingMode(RoundingMode.HALF_UP);
        }
        String string = fnum.format(f);
        return string.equals("-0") ? "0" : string;
    }

    /**
     * 保留小数点后n位
     *
     * @param f     小数值
     * @param index 保留几位
     * @return 返回一个string， 注：不进行四舍五入，  直接去掉多余的位数
     */
    public static String keepFloat(double f, int index) {
        if (f <= 0) {
            return "0.0";
        }
        if (index <= 0) {
            index = 0;
        }
        BigDecimal fnum = new BigDecimal(f);
        fnum = fnum.setScale(index, BigDecimal.ROUND_DOWN);

        return fnum.toString();
    }

    /**
     * 将单位sp转换为像素
     *
     * @param size
     * @return
     */
    public static float spToPx(float size) {
        Resources r = FFApplication.app.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, r.getDisplayMetrics());
    }

    /**
     * 获取客户端程序版本号
     *
     * @return
     */
    public static String getVerName() {
        String verCode = null;
        try {
            PackageInfo info = FFApplication.app.getPackageManager().getPackageInfo(FFApplication.app.getPackageName(), 0);
            verCode = info.versionName;

        } catch (NameNotFoundException e) {
        }
        return verCode;
    }

    public static int getTextLength(int sp) {
        Paint pFont = new Paint();
        Rect rect = new Rect();
        pFont.setTextSize(spToPx(sp));
        pFont.getTextBounds("中", 0, 1, rect);
        return rect.width();
    }

    /**
     * 获取内容高度，屏幕宽度减去状态栏高度
     *
     * @param activity
     * @return
     */
    public static int getContentHight(Activity activity) {
        return getDisHight() - getStatusbarHight(activity);
    }

    /**
     * 获取 虚拟按键的高度
     *
     * @return
     */
    public static int getBottomStatusHeight() {
        int totalHeight = getDpi();

        int contentHeight = getScreenHeight();

        return totalHeight - contentHeight;
    }

    static int statusBarHight = 0;

    public static int getStatusbarHight(Activity activity) {
        if (statusBarHight != 0) {
            return statusBarHight;
        }
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        statusBarHight = statusHeight;
        return statusHeight;
    }

    public static boolean checkNet() {
        ConnectivityManager manager = (ConnectivityManager) FFApplication.app.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isAvailable()) {
            return false;
        }
        return true;
    }

    public static String Md5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static int getPx(float dp) {
        return (int) (FFApplication.app.getResources().getDisplayMetrics().density * dp);
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getDisHight() {
        return FFApplication.app.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getDisWidth() {
        return FFApplication.app.getResources().getDisplayMetrics().widthPixels;
    }

    public static void vibrate(long milliseconds) {
        Vibrator vib = (Vibrator) FFApplication.app.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }
    public static boolean containsEmoji(String source) {
        int len = source.length();
        boolean isEmoji = false;
        for (int i = 0; i < len; i++) {
            char hs = source.charAt(i);
            if (0xd800 <= hs && hs <= 0xdbff) {
                if (source.length() > 1) {
                    char ls = source.charAt(i+1);
                    int uc = ((hs - 0xd800) * 0x400) + (ls - 0xdc00) + 0x10000;
                    if (0x1d000 <= uc && uc <= 0x1f77f) {
                        return true;
                    }
                }
            } else {
                // non surrogate
                if (0x2100 <= hs && hs <= 0x27ff && hs != 0x263b) {
                    return true;
                } else if (0x2B05 <= hs && hs <= 0x2b07) {
                    return true;
                } else if (0x2934 <= hs && hs <= 0x2935) {
                    return true;
                } else if (0x3297 <= hs && hs <= 0x3299) {
                    return true;
                } else if (hs == 0xa9 || hs == 0xae || hs == 0x303d || hs == 0x3030 || hs == 0x2b55 || hs == 0x2b1c || hs == 0x2b1b || hs == 0x2b50|| hs == 0x231a ) {
                    return true;
                }
                if (!isEmoji && source.length() > 1 && i < source.length() -1) {
                    char ls = source.charAt(i+1);
                    if (ls == 0x20e3) {
                        return true;
                    }
                }
            }
        }
        return  isEmoji;
    }
    public static boolean isStringEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean isListEmpty(List<?> list) {
        if (list == null) {
            return true;
        }
        return list.isEmpty();
    }

    public static int getListSize(List<?> list) {
        if (isListEmpty(list)) {
            return 0;
        }
        return list.size();
    }

    public static String getMyUUID() {
        try {
            TelephonyManager tm = (TelephonyManager) APP.app
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (imei == null) {
                final String tmDevice, tmSerial, androidId;
                tmDevice = "" + tm.getDeviceId();
                tmSerial = "" + tm.getSimSerialNumber();
                androidId = ""
                        + android.provider.Settings.Secure.getString(
                        APP.app.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
                UUID deviceUuid = new UUID(androidId.hashCode(),
                        ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
                String uniqueId = deviceUuid.toString();
                String uniqueIdmd5 = Md5(uniqueId);
                Log.d("debug", "uuid=" + uniqueIdmd5);
                return uniqueIdmd5;
            }
            return Md5(imei);
        } catch (Throwable t) {
            return System.currentTimeMillis() + "";
        }
    }

    public static Bitmap getBitmapViewByMeasure(View view, int width, int height) {
        //打开图像缓存
        view.setDrawingCacheEnabled(true);
        //必须调用measure和layout方法才能成功保存可视组件的截图到png图像文件
        //测量View大小
        if (height <= 0) {
            view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        } else if (height > 0) {
            view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
        }
        //发送位置和尺寸到View及其所有的子View
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap bitmap = null;
        try {
            //获得可视组件的截图
            bitmap = view.getDrawingCache();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static boolean isLowMem() {
        ActivityManager am = (ActivityManager) FFApplication.app.getSystemService(Context.ACTIVITY_SERVICE);
        boolean largeHeap = (FFApplication.app.getApplicationInfo().flags & ApplicationInfo.FLAG_LARGE_HEAP) != 0;
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(info);
        long availMem = info.availMem;

        long l = Runtime.getRuntime().freeMemory();
        return info.lowMemory || l < (5 * 1024 * 1024);
    }

    public static Bitmap getBitmapViewByMeasure1(View view, int width, int height) {
        //打开图像缓存
        view.setDrawingCacheEnabled(true);
        //必须调用measure和layout方法才能成功保存可视组件的截图到png图像文件
        //测量View大小
        if (height <= 0) {
            view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        } else if (height > 0) {
            view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
        }
        //发送位置和尺寸到View及其所有的子View
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap bitmap = null;
        try {
            //获得可视组件的截图
            bitmap = view.getDrawingCache();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void measureView(View view, int width, int height) {
        //打开图像缓存
        view.setDrawingCacheEnabled(true);
        //必须调用measure和layout方法才能成功保存可视组件的截图到png图像文件
        //测量View大小
        if (height <= 0) {
            view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        } else if (height > 0) {
            view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
        }
        //发送位置和尺寸到View及其所有的子View
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    /**
     * 获取客户端程序版本号
     *
     * @return
     */
    public static int getVerCode() {
        int verCode = -1;
        try {
            verCode = FFApplication.app.getPackageManager().getPackageInfo(FFApplication.app.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
        }
        return verCode;
    }

    public static SYSchemeData getSchemeData(Uri url) {
        if (url == null)
            return null;

        SYSchemeData schemeData = new SYSchemeData();
        String host = url.getHost();
        if (!isStringEmpty(host)) {
            try {
                schemeData.setHost(Integer.parseInt(host));
            } catch (Exception e) {
                return null;
            }
        }

        List<String> list = url.getPathSegments();
        ArrayList<String> paramList = new ArrayList<>(list);
        schemeData.setParams(paramList);
        return schemeData;
    }

    /**
     * 创建一个全局Handler，可以用来执行一些post任务等 by chenglin
     */
    public static Handler getHandler() {
        if (mHandler == null) {
            synchronized (FFUtils.class) {
                if (mHandler == null) {
                    mHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        return mHandler;
    }

    //利用BigDecimal做除法
    public static double divide(double value1, double value2, int scale) {
        if (value2 == 0) {
            return 0;
        }
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_DOWN).doubleValue();
    }

    /**
     * 四舍五入保留指定位数的小数
     */
    public static double formatDouble(double d, int scale) {
        BigDecimal b = new BigDecimal(d);
        double myNum3 = b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        return myNum3;
    }


    /**
     * 根据关键字来把字符串标记为不同颜色
     *
     * @param myStr   传入的字符串
     * @param keyword 要被标记的关键字
     * @param color   要被标记的颜色
     * @return SpannableString
     */
    public static SpannableString getSpanByKeyword(final String myStr, final String keyword, final int color) {
        int redColor = APP.app.getResources().getColor(color);
        SpannableString textSpan = new SpannableString(myStr);
        if (TextUtils.isEmpty(keyword)) {
            return textSpan;
        }

        int index = myStr.indexOf(keyword);
        while (index >= 0) {
            textSpan.setSpan(new ForegroundColorSpan(redColor), index, index + keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            int nextIndex = index + keyword.length();

            if (nextIndex <= myStr.length()) {
                index = myStr.indexOf(keyword, nextIndex);
            }

        }
        return textSpan;
    }

    /**
     * 得到数组中的最小值
     */
    public static long getMinNum(long[] numbers) {
        if (numbers == null || numbers.length <= 0) {
            return 0;
        }

        int i;
        long min, max;
        min = max = numbers[0];
        for (i = 0; i < numbers.length; i++) {
            if (numbers[i] > max) {
                max = numbers[i];
            }
            if (numbers[i] < min) {
                min = numbers[i];
            }
        }
        return min;
    }

    public static boolean isPermissions(String permission) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        int permissionCode = ActivityCompat.checkSelfPermission(FFApplication.app, permission);
        if (permissionCode != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    /**
     * 得到数组中的最大值
     */
    public static long getMaxNum(long[] numbers) {
        if (numbers == null || numbers.length <= 0) {
            return 0;
        }

        int i;
        long min, max;
        min = max = numbers[0];
        for (i = 0; i < numbers.length; i++) {
            if (numbers[i] > max) {
                max = numbers[i];
            }
            if (numbers[i] < min) {
                min = numbers[i];
            }
        }
        return max;
    }

    public static String getMore999(String number) {
        if (Parser.parseInt(number) > 999) {
            return "999+";
        }

        return number;
    }

    /**
     * 只能输入数字。字母和汉字
     */
    public static String LetterAndChinese(String text) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            if (!((letter >= 'a' && letter <= 'z') || (letter >= 'A' && letter <= 'Z') || (letter >= '0' && letter <= '9') || letter > 128)) {
                str.append(letter + "");
            }
        }
        return str.toString();
    }

    public static boolean checkString(String nickName) {
        for (int i = 0; i < nickName.length(); i++) { //循环遍历字符串
            if (!Character.isDigit(nickName.charAt(i)) // 判断是否为数字
                    && !Character.isLetter(nickName.charAt(i))) {     //判断是否为中英文
                return false;
            }
        }

        return true;
    }

    // 根据Unicode编码完美的判断中文汉字和符号
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    /**
     * 获取应用详情页面intent
     */
    public static void openAppDetailSetting(BaseActivity activity) {
        try {
            Intent localIntent = new Intent();
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.setAction(Intent.ACTION_VIEW);
                localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
            }
            activity.startActivity(localIntent);
        } catch (Exception e) {

        }
    }

    /**
     * 截取字符串长度
     *
     * @param str
     * @return
     */
    public static String subStr10(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }

        if (str.length() <= 10) {
            return str;
        }

        return str.substring(0, 10) + "...";
    }

    public static String readFileFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(APP.app.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null){
                Result += line;
            }
            inputReader.close();
            bufReader.close();
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
