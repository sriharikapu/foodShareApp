package com.fan.framework.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/7/25.
 */
public class TimeUtils {
    public static String getTime(String format, long time){
        if(time < 14685794430l){
            time*=1000;
        }
        return new SimpleDateFormat(format, Locale.CHINA).format(new Date(time));
    }

}
