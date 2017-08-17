package com.fengnian.smallyellowo.foodie;


import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.fan.framework.config.FFConfig;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.appbase.APP;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseWebViewActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.homepage.HomeChildNearbyFrag;
import com.fengnian.smallyellowo.foodie.homepage.HomeChildSelectedFrag;
import com.fengnian.smallyellowo.foodie.intentdatas.PGCDetailIntent;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.wxapi.SinaOpen;
import com.fengnian.smallyellowo.foodie.wxapi.WeixinOpen;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class PGCDetailActivity extends BaseWebViewActivity<PGCDetailIntent> {

    @Override
    public String getTitleString() {
        if (FFUtils.isStringEmpty(webView.getTitle())) {
            return "详情";
        } else {
            return webView.getTitle();
        }
    }

    public boolean needPublicParams() {
        return isFrom(MainActivity.class, HomeChildSelectedFrag.class) ? false : true;
    }

    @Override
    public String getUrl() {
        return getIntentData().getUrl();
    }

    @Override
    public String[] getParams() {
        if ("Yes".equals(getIntentData().getIsAppns())) {
            return new String[0];
        } else {
            return new String[]{"pgcId", getIntentData().getId()};
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SP.isLogin() && getIntentData().getId() != null) {
//            post(Constants.shareConstants().getNetHeaderAdress() + "/indexNew/addReadPgcIdV230.do", null, null, new FFNetWorkCallBack<BaseResult>() {
            post(IUrlUtils.Search.addReadPgcIdV230, null, null, new FFNetWorkCallBack<BaseResult>() {
                @Override
                public void onSuccess(BaseResult response, FFExtraParams extra) {

                }

                @Override
                public boolean onFail(FFExtraParams extra) {
                    return false;
                }
            }, "pgcId", getIntentData().getId());
        }
//        post(Constants.shareConstants().getNetHeaderAdress() + "/browsePgcV250.do", null, null, new FFNetWorkCallBack<BaseResult>() {
        if (getIntentData().getId() != null) {
            post(IUrlUtils.Search.browsePgcV250, null, null, new FFNetWorkCallBack<BaseResult>() {
                @Override
                public void onSuccess(BaseResult response, FFExtraParams extra) {
                }

                @Override
                public boolean onFail(FFExtraParams extra) {
                    return false;
                }
            }, "pgcId", getIntentData().getId());
        }
    }

}
