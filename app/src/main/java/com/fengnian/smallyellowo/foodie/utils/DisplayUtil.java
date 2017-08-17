package com.fengnian.smallyellowo.foodie.utils;


import com.fengnian.smallyellowo.foodie.appbase.APP;

/**
 * Created by TaurusXi on 2014/6/5.
 */
public class DisplayUtil {

    public static int screenWidth; //屏幕宽 px
    public static int screenHeight; //屏幕高 px
//    public static float density;//屏幕密度
//    public static int densityDPI;//屏幕密度
//    public static float screenWidthDip;//  dp单位
//    public static float screenHightDip;//  dp单位
//    public static int statusBarHight;

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = APP.app.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = APP.app.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(float pxValue) {
        final float fontScale = APP.app.getResources().getDisplayMetrics()
                .scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = APP.app.getResources().getDisplayMetrics()
                .scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
