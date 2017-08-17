package com.fengnian.smallyellowo.foodie.utils;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Administrator on 2016-8-3.
 */
public class ContextUtils {
    private ContextUtils() {
    }


    public static String getFriendlyTime(long millis, boolean long_timeString) {
        if (millis == 0) {
            return "";
        }
        if (millis < 1000000000000l) {
            millis *= 1000;
        }
        long nowmillis = System.currentTimeMillis();
        final long diff = nowmillis - millis;// 这样得到的差值是微秒级别
        long time = 1000;
        if(diff < (time*=60)){//不到一分钟
            return "刚刚";
        }else if(diff < (time*=60)){//不到一小时
            return (diff*60/time)+"分钟前";
        }else if(diff < (time*=24)){//不到一个天
            return (diff*24/time)+"小时前";
        }else if(diff < (time*=30)){//不到一个月
            return (diff*30/time)+"天前";
        }else if(diff < (time*=365)){//不到一年
            return (diff*365/time)+"月前";
        }else{//一年以上
            return (diff/time)+"年前";
        }



//        Calendar c = Calendar.getInstance(Locale.CHINA);
//        c.setTimeInMillis(millis);
//        int M = c.get(Calendar.MONTH) + 1;
//        int d = c.get(Calendar.DATE);
////        String POD = getPOD(c.get(Calendar.HOUR_OF_DAY));
//        int h = c.get(Calendar.HOUR_OF_DAY);
//        int m = c.get(Calendar.MINUTE);
//        c.setTimeInMillis(nowmillis);
//        int M1 = c.get(Calendar.MONTH) + 1;
//        int d1 = c.get(Calendar.DATE);
//        String formatedDate_date = M + "月" + d + "日";
//        String formatedDate_time = h + ":" + (m < 10 ? "0" + m : m);
//        if (diff < 0) {
//            if (long_timeString) {
//                return formatedDate_date + " " + POD + " " + formatedDate_time;
//            } else {
//                return formatedDate_date + " " + POD;
//            }
//        }
//        if (M == M1 && d == d1) {// 今天
//            return POD + " " + formatedDate_time;
//        }
//        if (M == M1 && d1 - d == 1) {
//            if (long_timeString) {
//                return "昨天 " + POD + " " + formatedDate_time;
//            } else {
//                return "昨天 " + POD;
//            }
//        }
//        if (long_timeString) {
//            return formatedDate_date + " " + formatedDate_time;
//        } else {
//            return formatedDate_date;
//        }
    }

//    private static String getPOD(int i) {
//        if (i < 1) {
//            return "午夜";
//        }
//        if (i < 5) {
//            return "凌晨";
//        }
//        if (i < 8) {
//            return "早上";
//        }
//        if (i < 11) {
//            return "上午";
//        }
//        if (i < 13) {
//            return "中午";
//        }
//        if (i < 17) {
//            return "下午";
//        }
//        if (i < 19) {
//            return "傍晚";
//        }
//        if (i < 23) {
//            return "晚上";
//        }
//        return "午夜";
//    }

    public static String getUserDynamicTime(long millis) {

        if (millis == 0) {
            return "";
        }
        if (millis < 1000000000000l) {
            millis *= 1000;
        }
        long nowmillis = System.currentTimeMillis();
        Calendar c = Calendar.getInstance(Locale.CHINA);
        c.setTimeInMillis(millis);
        int Y = c.get(Calendar.YEAR);
        int M = c.get(Calendar.MONTH) + 1;
        int d = c.get(Calendar.DATE);
        int h = c.get(Calendar.HOUR_OF_DAY);
        int m = c.get(Calendar.MINUTE);
        c.setTimeInMillis(nowmillis);
        int Y1 = c.get(Calendar.YEAR);
        int M1 = c.get(Calendar.MONTH) + 1;
        int d1 = c.get(Calendar.DATE);
        if (Y == Y1) {
            if (M == M1) {
                if (d == d1) {
                    return "今天";
                } else {
                    return d + "日";
                }
            } else {
                return M + "月" + d + "日";
            }
        } else {
            return Y + "年" + M + "月" + d + "日";
        }
    }
}
