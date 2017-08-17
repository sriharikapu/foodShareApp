package com.fan.framework.base;


import android.app.Activity;

import com.fan.framework.http.FFBaseBean;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.http.FFNetWorkRequest;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;

import java.util.ArrayList;


public interface FFContext {
    ArrayList<Activity> allActivis = new ArrayList<>();
    /**
     * 判断当前activity是否finish了
     *
     * @return
     */
    boolean getDestroyed();

    /**
     * 显示吐司debug状态下会显示debug信息
     *
     * @param msg
     * @param debug
     */
    void showToast(CharSequence msg, CharSequence debug);

    /**
     * 显示ProgressDialog
     *
     * @param word
     */
    int showProgressDialog(CharSequence word);

    /**
     * 获取当前context
     *
     * @return
     */
    FFContext context();

    /**
     * 显示ProgressDialog
     *
     * @param word
     */
    int showProgressDialog(CharSequence word, boolean cancelAble);

    /**
     * 使ProgressDialog消失
     */
    void dismissProgressDialog(int id);


    void showToast(int resId, CharSequence text);

    void showToast(CharSequence text);

    void showToast(int resId, CharSequence text, CharSequence debugMsg);


    <T extends FFBaseBean> void post(String url, String words,
                                     FFExtraParams extra, FFNetWorkCallBack<T> callBack,
                                     Object... params);


    ArrayList<String> getTags();


    ArrayList<String> getChildTags();

    void onPageInitFail(FFNetWorkRequest request);

    void onPageInitNoNet(FFNetWorkRequest request);

    void onPageInitNoData(FFNetWorkRequest request);

    void onPageInitHasData(FFNetWorkRequest request);

    void onPageInitRetry(FFNetWorkRequest request);

}
