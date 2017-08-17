package com.fengnian.smallyellowo.foodie.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;

/**
 * Created by Elaine on 2015/11/4.
 */
public class Parser {
    public static float parseFloat(String value){
        if(!isNumber(value)){
            return 0;
        }
        return Float.parseFloat(value);
    }

    public static double parseDouble(String value){
        if(!isNumber(value)){
            return 0;
        }
        return Double.parseDouble(value);
    }

    public static int parseInt(String value){
        if(!isInteger(value)){
            return 0;
        }
       return Integer.parseInt(value);
    }

    public static long parseLong(String value){
        if(!isInteger(value)){
            return 0;
        }
        return Long.parseLong(value);
    }



    /**
     * 判断是否是数字
     *
     * @param num
     * @return
     */
    public static boolean isNumber(String num) {
        if (TextUtils.isEmpty(num)) {
            return false;
        }
        int countPoint = 0;
        char [] chars = num.toCharArray();
        for(char c :chars){
            if ((c < Character.valueOf('0') || c > Character.valueOf('9')) && c !='.' ) {
                return false;
            }
            if(c == '.'){
                countPoint++;
            }

        }

        if (num.contains(".") && num.indexOf(".") <= 0){
            return false;
        }

        if(countPoint > 1){
            return false;
        }

        return true;
    }

    public static boolean parseBoolean(String boo){
        if(TextUtils.isEmpty(boo)){
            return false;
        }
        return boo.equals("1");
    }

    public static boolean isInteger(String num) {
        if (TextUtils.isEmpty(num)) {
            return false;
        }
        char [] chars = num.toCharArray();
        for(char c :chars){
            if (c < Character.valueOf('0') && c > Character.valueOf('9')) {
                return false;
            }
        }
        return true;
    }

}

