package com.fengnian.smallyellowo.foodie.appbase;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fan.framework.config.FFConfig;
import com.fan.framework.imageloader.FFImageCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFLogUtil;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.contact.ShareUrlTools;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.login.LoginOneActivity;
import com.fengnian.smallyellowo.foodie.receivers.SYSchemeData;
import com.fengnian.smallyellowo.foodie.wxapi.SinaOpen;
import com.fengnian.smallyellowo.foodie.wxapi.WeixinOpen;
import com.j256.ormlite.stmt.query.In;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2017-5-22.
 */

public abstract class BaseWebViewActivity<T extends IntentData> extends BaseActivity<T> {
    private View menuView;
    private Button menuText;

    public static class ShareContent {
        public String link;
        public Object img;
        public String title;
        public String content;

        public void init(BaseWebViewActivity activity) {
            if (FFUtils.isStringEmpty(link)) {
                link = activity.getUrl();
            }
            link = ShareUrlTools.getHtml5Url(initParams(link, true, new String[]{}));
            if (FFUtils.isStringEmpty(title)) {
                title = activity.getTitleString();
            }
            if (FFUtils.isStringEmpty(content)) {
                content = "";
            }
            if (img == null || (img.toString().length() == 0)) {
                img = R.mipmap.ic_launcher;
            }
        }
    }


    protected WebView webView;
    private ProgressBar progressBar;
    private String url;
    ImageView iv_close;
    protected MyWebViewClient webViewClient;

    public abstract String getTitleString();

    public abstract String getUrl();

    public abstract String[] getParams();

    @Override
    public void refreshAfterLogin() {
        super.refreshAfterLogin();
        String url = getUrl();
        webView.loadUrl(initParams(url));
        webView.clearHistory();
    }


    @Override
    public void close(View v) {
        if (webView.canGoBack()) {
            finish();
            return;
        }
        super.close(v);
    }

    long lastPress = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastPress < 500) {
            return;
        }
        lastTime = System.currentTimeMillis();
        hasBack = false;
        webView.loadUrl("javascript:client.back()");
        FFUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (hasBack) {//本地处理右键点击事件
                    return;
                }
                realBack();
            }
        }, 100);
    }

    private void realBack() {
        if (webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_search);
        iv_close = (ImageView) findViewById(R.id.iv_actionbar_close);
        progressBar = (ProgressBar) findViewById(R.id.find_jbzc_progressBar);
        webView = (WebView) findViewById(R.id.wv_find_jbzc);
        url = getUrl().trim();
        initView();
        setTitle(getTitleString());

        menuText = addMenu("", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasRbtn = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript:client.clickRbtn()");
                        FFUtils.getHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (hasRbtn) {//本地处理右键点击事件

                                }
                            }
                        }, 100);
                    }
                });
            }
        });
        menuText.setVisibility(View.INVISIBLE);
        menuView = addMenu(R.mipmap.ic_three_point_black, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendInfoToJs();
            }
        });

    }

    public void setMenuView(int visible) {
        if (menuView != null) {
            menuView.setVisibility(visible);
        }
    }

    boolean hasRbtn;
    boolean hasBack;

    public void initView() {
        // 通过WebChromeClient可以处理JS对话框，titles, 进度，等
        // ，我们将websit下载的进度同步到acitity的进度条上。
        WebSettings settings = webView.getSettings();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            settings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setJavaScriptEnabled(true); //设置支持Javascript
        settings.setDefaultTextEncodingName("utf-8");
        String s1 = settings.getUserAgentString() + FFConfig.getWebUA();
        settings.setUserAgentString(s1);
        settings.setBuiltInZoomControls(true); //页面添加缩放按钮
        settings.setAllowFileAccess(true);// 设置允许访问文件数据
        settings.setSupportZoom(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setUseWideViewPort(true);
        settings.setBlockNetworkImage(false);
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        webView.requestFocus(); //触摸焦点起作用.如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件。
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); //取消滚动条
        webView.setWebViewClient(webViewClient);
        webView.addJavascriptInterface(new JsToJava(), "stub");
        webView.addJavascriptInterface(new _Client(), "_client");
        // 设置在app中打开，而不是在浏览器中
        webViewClient = new MyWebViewClient(context(), iv_close) {

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                if (webView.canGoBack() || showClose()) {
                    iv_close.setVisibility(View.VISIBLE);
                } else {
                    webView.clearHistory();
                    iv_close.setVisibility(View.GONE);
                }
            }

            //
//
//            public WebResourceResponse shouldInterceptRequest(WebView view,
//                                                              String url) {
//                WebResourceResponse response = null;
//                if (url.endsWith(".jpg")||url.endsWith(".jpeg")) {
//                    try {
//
//                        URL u = new URL(url);
//                        URLConnection conn = u.openConnection();
//                        conn.setConnectTimeout(10000);
//                        conn.connect();
//                        InputStream is = conn.getInputStream();
//                        response = new WebResourceResponse("image/jpeg", "UTF-8", is);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                return response;
//            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(1);
            }
        };
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                // TODO Auto-generated method stub
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String t) {
                // TODO Auto-generated method stub
                super.onReceivedTitle(view, t);
//				if(t != null)
//					tv_title.setText(t);
            }
        });
//        url = "http://172.17.103.57:8888/cateQuestionAnswer/home";
        String s = initParams(url);
        webView.loadUrl(s);//getString(R.string.find_webview_disease_search_str)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            dealJavascriptLeak();
        }
    }

    private void dealJavascriptLeak() {
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.removeJavascriptInterface("accessibility");
        webView.removeJavascriptInterface("accessibilityTraversal");

    }

    public static class MyWebViewClient extends WebViewClient {
        public MyWebViewClient(BaseActivity activity, ImageView iv_close) {
            this.activity = activity;
            this.iv_close = iv_close;
        }

        private BaseActivity activity;
        private ImageView iv_close;

        @Override
        public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, String url) {
            if (FFUtils.isStringEmpty(url))
                return false;
            Uri uri = Uri.parse(url);
            SYSchemeData schemeData = FFUtils.getSchemeData(uri);
            if (schemeData != null) {
                if (schemeData.getHost() == 3) {
                    logout(1, "");
                    return true;
                }
            }

            if (url.contains("/action/views.")) {
                TreeMap<String, String> params = allParam(url);
                if (params.containsKey("type")) {
                    if (params.get("type").equals("5")) {
                        if (!SP.isLogin()) {
                            LoginOneActivity.startLogin(activity);
                        } else {
                            activity.refreshAfterLogin();
                        }
                        return true;
                    }
                    if (params.get("type").equals("3")) {
                        logout();
                        return true;
                    }
                }
            }

            webView.loadUrl(url);
            return true;
        }
    }

    public String initParams(String url) {
        return initParams(url, needPublicParams(), getParams());
    }

    public static String initParams(String url, boolean needPublicParams, String[] param) {
        TreeMap<String, String> params = allParam(url);
        url = delParams(url);

        if (needPublicParams) {
            params.put("visitorAccount", SP.getYoukeToken());
            params.put("version", FFUtils.getVerName());
            if (!FFUtils.isStringEmpty(SP.getUid())) {
                params.put("account", SP.getUid());
            }

            if (SP.getWeiChatCode() != null) {
                params.put("wechatCode", SP.getWeiChatCode());
            }
            if (!FFUtils.isStringEmpty(SP.getToken())) {
                params.put("token", SP.getToken());
            }
        }
        if (param != null)
            for (int i = 0; i < param.length - 1; i += 2) {
                params.put(param[i], param[i + 1]);
            }
        Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
        url += "?";
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            url += entry.getKey() + "=" + entry.getValue() + "&";
        }
        return url;
    }

    public boolean needPublicParams() {
        return true;
    }

    private static String delParams(String url) {
        int index = url.indexOf("?");
        if (index < 0) {
            return url;
        }
        return url.substring(0, index);
    }


    public static TreeMap<String, String> allParam(String url) {
        String result = "";
        int index = url.indexOf("?");
        if (index < 0) {
            return new TreeMap<>();
        }
        String temp = url.substring(index + 1);
        String[] keyValue = temp.split("&");
        TreeMap<String, String> params = new TreeMap<>();
        for (String str : keyValue) {
            if (str.length() == 0) {
                continue;
            }
            String[] pair = str.split("=");
            if (pair.length < 2) {
                continue;
            }
            params.put(pair[0], pair[1]);
        }
        return params;
    }


    long lastTime = 0;
    boolean isBack = false;

    public void sendInfoToJs() {
        if (System.currentTimeMillis() - lastTime < 500) {
            return;
        }
        lastTime = System.currentTimeMillis();
        isBack = false;
        webView.loadUrl("javascript:shareContent()");
        FFUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isBack) {
                    new JsToJava().jsMethod(null);
                    isBack = true;
                }
            }
        }, 100);
    }

    public class _Client {
        /**
         * 修改标题
         *
         * @param title
         */
        @JavascriptInterface
        public void title(final String title) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setTitle(title);
                }
            });
        }

        /**
         * 添加右侧按钮
         *
         * @param title
         */
        @JavascriptInterface
        public void rItemTitle(final String title) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (FFUtils.isStringEmpty(title)) {
                        menuText.setVisibility(View.INVISIBLE);
                    } else {
                        menuText.setText(title);
                        menuText.setVisibility(View.VISIBLE);
                    }
                }
            });

        }

        /**
         * 右侧按钮能否点击
         *
         * @param title
         */
        @JavascriptInterface
        public void rItemEnable(final boolean title) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    menuText.setEnabled(title);
                }
            });
        }

        /**
         * 导航条透明
         *
         * @param alpha
         */
        @JavascriptInterface
        public void navClearColor(final boolean alpha) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (alpha) {
                        getTitleView().setBackgroundColor(0);
                        getTitleView().findViewById(R.id.actionbar_line).setVisibility(View.INVISIBLE);
                        ((FrameLayout.LayoutParams) getContainer().getLayoutParams()).setMargins(0, 0, 0, 0);
                        getContainer().requestLayout();
                    } else {
                        getTitleView().setBackgroundColor(getResources().getColor(R.color.ff_bg_actionbar));
                        getTitleView().findViewById(R.id.actionbar_line).setVisibility(View.VISIBLE);
                        ((FrameLayout.LayoutParams) getContainer().getLayoutParams()).setMargins(0, (int) getResources().getDimension(R.dimen.ff_actionbarHight), 0, 0);
                    }
                }
            });
        }

        /**
         * toast
         * @param title
         */
        @JavascriptInterface
        public void toast(final String title) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!FFUtils.isStringEmpty(title)) {
                        showToast(title);
                    }
                }
            });
        }

        @JavascriptInterface
        public void alert(final String title) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new EnsureDialog.Builder(context()).setMessage(title).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                }
            });
        }

        @JavascriptInterface
        public void back(final String title) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finish();
                    toast(title);
                }
            });
        }

        @JavascriptInterface
        public void push(String name, String json) {

        }

        @JavascriptInterface
        public void backCallBack() {
            hasBack = true;
        }

        @JavascriptInterface
        public void rBtnCallBack() {
            hasRbtn = true;
        }

        @JavascriptInterface
        public int netWorkStatus() {
            return FFUtils.checkNet() ? 0 : 1;
        }

        final ArrayList<Integer> ids = new ArrayList<>();

        @JavascriptInterface
        public void showActivity() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    FFLogUtil.e("asdfj", "show");
                    ids.add(showProgressDialog(""));
                }
            });
        }

        @JavascriptInterface
        public void hidenActivity() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    FFLogUtil.e("asdfj", "dismiss");
                    if (ids.size() > 0)
                        dismissProgressDialog(ids.remove(0));
                }
            });
        }


    }

    public class JsToJava {
        @JavascriptInterface
        public void jsMethod(String paramFromJS) {
            isBack = true;
            ShareContent content1 = null;
            if (!FFUtils.isStringEmpty(paramFromJS)) {
                try {
                    content1 = JSON.parseObject(paramFromJS, ShareContent.class);
                } catch (Exception e) {
                }
            }

            if (content1 == null) {
                content1 = new ShareContent();
            }
            content1.init((BaseWebViewActivity) context());
            final ShareContent content = content1;

            View contentView = LayoutInflater.from(context()).inflate(
                    R.layout.pop_share, null);
            final PopupWindow popupWindow = new PopupWindow(contentView,
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            // 设置按钮的点击事件
            TextView tv_pyq = (TextView) contentView.findViewById(R.id.tv_pyq);
            TextView tv_py = (TextView) contentView.findViewById(R.id.tv_py);
            TextView tv_sina = (TextView) contentView.findViewById(R.id.tv_sina);
            TextView tv_del = (TextView) contentView.findViewById(R.id.tv_del);
            ((View) tv_del.getParent()).setVisibility(View.GONE);
            contentView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                }
            });

            tv_pyq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int id = showProgressDialog("");
                    FFImageLoader.load_base(context(), content.img, null, true, Constants.SmallImage, Constants.SmallImage, 0, FFImageLoader.TYPE_NONE, new FFImageCallBack() {
                        @Override
                        public void imageLoaded(Bitmap bitmap, String imageUrl) {
                            dismissProgressDialog(id);
                            popupWindow.dismiss();
                            if (bitmap == null) {
                                return;
                            }
                            WeixinOpen.getInstance().share2weixin_pyq(content.link, content.content, content.title, bitmap);
                        }

                        @Override
                        public void onDownLoadProgress(int downloaded, int contentLength) {

                        }
                    });
                }
            });
            tv_sina.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final int id = showProgressDialog("");
                    FFImageLoader.load_base(context(), content.img, null, true, Constants.SmallImage, Constants.SmallImage, 0, FFImageLoader.TYPE_NONE, new FFImageCallBack() {
                        @Override
                        public void imageLoaded(Bitmap bitmap, String imageUrl) {
                            dismissProgressDialog(id);
                            popupWindow.dismiss();
                            if (bitmap == null) {
                                return;
                            }
                            SinaOpen.share(context(), bitmap, content.title, content.content, content.link);
                        }

                        @Override
                        public void onDownLoadProgress(int downloaded, int contentLength) {

                        }
                    });
                }
            });

            tv_py.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int id = showProgressDialog("");
                    FFImageLoader.load_base(context(), content.img, null, true, Constants.SmallImage, Constants.SmallImage, 0, FFImageLoader.TYPE_NONE, new FFImageCallBack() {
                        @Override
                        public void imageLoaded(Bitmap bitmap, String imageUrl) {
                            dismissProgressDialog(id);
                            popupWindow.dismiss();
                            if (bitmap == null) {
                                return;
                            }
                            WeixinOpen.getInstance().share2weixin(content.link, content.content, content.title, bitmap);
                        }

                        @Override
                        public void onDownLoadProgress(int downloaded, int contentLength) {

                        }
                    });
                }
            });


            popupWindow.setTouchable(true);

            popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
//                        Log.i("mengdd", "onTouch : ");
                    return false;
                    // 这里如果返回true的话，touch事件将被拦截
                    // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                }
            });

            // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
            // 我觉得这里是API的一个bug
            popupWindow.setBackgroundDrawable(new ColorDrawable(0x45000000));
            popupWindow.showAtLocation((View) getContainer().getParent(), Gravity.CENTER, 0, 0);
        }
    }


}
