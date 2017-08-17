package com.fengnian.smallyellowo.foodie;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import android.widget.FrameLayout;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.appbase.AppHelper;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.receivers.SYSchemeData;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.seles.tusdk.FilterManager;
import org.lasque.tusdk.core.utils.TLog;

/**
 * Created by Administrator on 2016-11-2.
 */

public class GuidePageActivity extends PermissionActivity
{
    private WebView webView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences gui_page_sp = getSharedPreferences("gui", Context.MODE_PRIVATE);
        boolean need_zhanshan = gui_page_sp.getBoolean("iszhanshan", false);
        setNotitle(true);


        if (!need_zhanshan)
        {
            start();

        }else
        {
//            initTuSdk();
            startAct();
            return;
        }

        setContentView(R.layout.activity_gui_page);
        webView = (WebView) findView(R.id.web_view);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.LOW);
        String url = "file:///android_asset/index.html";
//        String url = "http://172.16.10.19/Carousel/index.html";
        loadLocalHtml(url);
//        SharedPreferences gui_page_sp =getSharedPreferences("gui", Context.MODE_PRIVATE);
        gui_page_sp.edit().putBoolean("iszhanshan",true).commit();


//        initTuSdk();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null)
        {
            webView.freeMemory();
            webView.destroy();
            Log.i("tag","webView.destroy");
            FrameLayout layout = (FrameLayout) getWindow().getDecorView().findViewById(android.R.id.content);
            layout.removeView(webView);
            webView.removeAllViews();

        }
        webView = null;
    }

    private void initTuSdk()
    {
        if (AppHelper.isAppMainProcess(this))
        {
            TLog.i("isAppMainProcess");

            TuSdk.init(this, "1faa2d433fef2f60-03-glpcq1");
            TuSdk.checkFilterManager(mFilterManagerDelegate);
        }
    }

    /** 滤镜管理器委托 */
    private FilterManager.FilterManagerDelegate mFilterManagerDelegate = new FilterManager.FilterManagerDelegate()
    {
        @Override
        public void onFilterManagerInited(FilterManager manager)
        {
            TLog.i("init success");
        }
    };

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    public void loadLocalHtml(String url) {
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);//开启JavaScript支持
        ws.setJavaScriptEnabled(true);
        ws.setUseWideViewPort(true);
        ws.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if(FFUtils.isStringEmpty(url))
                    return false;
                Uri uri = Uri.parse(url);
                SYSchemeData schemeData = FFUtils.getSchemeData(uri);
                if(schemeData.getHost() == 2){
                    allActivis.remove(this);
//                    LoginIntentData loginIntentData=new LoginIntentData();
//                    loginIntentData.setCode("");
//                    startActivity(LoginOneActivity.class, loginIntentData);
//                    startActivity(WelcomActivity.class,new IntentData());
                    startAct();
//                    startSelectActivity();
                    return true;
                }
                return false;
            }
        });
        webView.loadUrl(url);
    }

   void startAct(){
       startActivity(WelcomActivity.class,new IntentData());
       finish();
   }

   private void startSelectActivity(){
       IntentData intentData = new IntentData();
       intentData.setRequestCode(8080);
       startActivity(SelectCityActivity.class,intentData);
       finish();
   }

    //主要处理H5内的返回操作
    @Override
    public void onBackPressed() {

    }


    @Override
    void onPermissionsAllowed() {
    }
}
