package com.fengnian.smallyellowo.foodie.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import com.fan.framework.base.FFBaseActivity;
import com.fan.framework.base.Test.StringUtils;
import com.fengnian.smallyellowo.foodie.MainActivity;
import com.fengnian.smallyellowo.foodie.appbase.APP;
import com.fengnian.smallyellowo.foodie.appbase.PlatformEngine;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;

import java.util.HashMap;

public class SinaOpen {
    //    public static final String REDIRECT_URL = "http://www.tinydonuts.cn/";
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    public static final String APP_KEY = "3451219723";
    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    private SinaOpen() {

    }

    public static final int SHARE_CLIENT = 1;
    public static final int SHARE_ALL_IN_ONE = 2;
    private static final int mShareType = 2;

    public static void share(Activity activity, Bitmap bitmap, String title, String text, String url) {
        // 创建微博分享接口实例
        IWeiboShareAPI mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(activity, APP_KEY);

        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
        mWeiboShareAPI.registerApp();
//        if (savedInstanceState != null) {
//            mWeiboShareAPI.handleWeiboResponse(activity.getIntent(), activity);
//        }

        sendMessage(activity, title, text, url, bitmap, mWeiboShareAPI);
    }

    public static void sharesingle(Activity activity, Bitmap bitmap, String title, String text, String url, boolean hasimg) {
        // 创建微博分享接口实例
        IWeiboShareAPI mWeiboShareAPI = mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(activity, APP_KEY);

        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
        mWeiboShareAPI.registerApp();
//        if (savedInstanceState != null) {
//            mWeiboShareAPI.handleWeiboResponse(activity.getIntent(), activity);
//        }

        sendSingleMessage(activity, bitmap, title, text, url, hasimg, mWeiboShareAPI);
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     */
    private static void sendMessage(final Activity activity, String title, String text, String url, Bitmap bitmap, IWeiboShareAPI mWeiboShareAPI) {

        if (mShareType == SHARE_CLIENT) {
            if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
                int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
                if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
                    sendMultiMessage(activity, bitmap, title, text, url, mWeiboShareAPI);
                } else {
                    sendSingleMessage(activity, bitmap, title, text, url, false, mWeiboShareAPI);
                }
            } else {
                Toast.makeText(activity, "微博未安装", Toast.LENGTH_SHORT).show();
            }
        } else if (mShareType == SHARE_ALL_IN_ONE) {
            sendMultiMessage(activity, bitmap, title, text, url, mWeiboShareAPI);
        }
    }


    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 注意：当 {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     */
    private static void sendMultiMessage(final Activity activity, Bitmap bitmap, String title, String text, String url, IWeiboShareAPI mWeiboShareAPI) {

        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();

        {
            WebpageObject mediaObject = new WebpageObject();
            mediaObject.identify = Utility.generateGUID();
            mediaObject.title = title;
            mediaObject.description = text;

            // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
            mediaObject.setThumbImage(bitmap);
            mediaObject.actionUrl = url;
            mediaObject.defaultText = title;
            weiboMessage.mediaObject = mediaObject;
        }

        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        if (mShareType == SHARE_CLIENT) {
            mWeiboShareAPI.sendRequest(activity, request);
        } else if (mShareType == SHARE_ALL_IN_ONE) {
            AuthInfo authInfo = new AuthInfo(activity, APP_KEY, REDIRECT_URL, SCOPE);
            Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(activity.getApplicationContext());
            String token = "";
            if (accessToken != null) {
                token = accessToken.getToken();
            }
            mWeiboShareAPI.sendRequest(activity, request, authInfo, token, new WeiboAuthListener() {

                @Override
                public void onWeiboException(WeiboException arg0) {
                }

                @Override
                public void onComplete(Bundle bundle) {
                    // TODO Auto-generated method stub
                    Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                    AccessTokenKeeper.writeAccessToken(activity.getApplicationContext(), newToken);
                    APP.showToast("onAuthorizeComplete token = " + newToken.getToken(), null);
                }

                @Override
                public void onCancel() {
                }
            });
        }
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 当{@link IWeiboShareAPI#getWeiboAppSupportAPI()} < 10351 时，只支持分享单条消息，即
     * 文本、图片、网页、音乐、视频中的一种，不支持Voice消息。
     */
    private static void sendSingleMessage(final Activity activity, Bitmap bitmap, String title, String text, String url, boolean hasImage, IWeiboShareAPI mWeiboShareAPI) {

        // 1. 初始化微博的分享消息
        // 用户可以分享文本、图片、网页、音乐、视频中的一种
        WeiboMessage weiboMessage = new WeiboMessage();
//        {
//            WebpageObject mediaObject = new WebpageObject();
//            mediaObject.identify = Utility.generateGUID();
//            mediaObject.title = title;
//            mediaObject.description = text;
//
//            // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
//            mediaObject.setThumbImage(bitmap);
//            mediaObject.actionUrl = url;
//            mediaObject.defaultText = title;
//            weiboMessage.mediaObject = mediaObject;
//        }
        /*if (hasVoice) {
            weiboMessage.mediaObject = getVoiceObj();
        }*/

//        if (hasText) {
//            weiboMessage.mediaObject = getTextObj();
//        }
        if (hasImage) {
            weiboMessage.mediaObject = getImageObj(bitmap);
        }
        // 2. 初始化从第三方到微博的消息请求
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
//        mWeiboShareAPI.sendRequest(activity, request);
        AuthInfo authInfo = new AuthInfo(activity, APP_KEY, REDIRECT_URL, SCOPE);
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(activity.getApplicationContext());
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }
        mWeiboShareAPI.sendRequest(activity, request, authInfo, token, new WeiboAuthListener() {

            @Override
            public void onWeiboException(WeiboException arg0) {
            }

            @Override
            public void onComplete(Bundle bundle) {
                Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                AccessTokenKeeper.writeAccessToken(activity.getApplicationContext(), newToken);
                APP.showToast("onAuthorizeComplete token = " + newToken.getToken(), null);

            }

            @Override
            public void onCancel() {
            }
        });
    }

    public static void shareUGC(Activity activity, Bitmap bitmap, String title, String text, String url) {

        // 创建微博分享接口实例
        IWeiboShareAPI mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(activity, APP_KEY);

        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
        mWeiboShareAPI.registerApp();

        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.textObject = getTextObj(title, url);
        weiboMessage.imageObject = getImageObj(bitmap);
//        weiboMessage.mediaObject = getWebpageObj();

        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        mWeiboShareAPI.sendRequest(activity, request);
    }

    /**
     * 文本消息构造方法。
     *
     * @return 文本消息对象。
     */
    private static TextObject getTextObj(String title, String htmlUrl) {
        TextObject textObject = new TextObject();
        textObject.text = "分享美食《" + title + "》,详情链接" + htmlUrl;
        return textObject;
    }

    /**
     * 图片消息构造方法。
     *
     * @return 图片消息对象。
     */
    private static ImageObject getImageObj(Bitmap bit) {
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(bit);
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private static WebpageObject getWebpageObj(Bitmap bitmap, String urlTitle, String shareUrl) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = "测试title";
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = shareUrl;
        mediaObject.defaultText = "小黄圈美食分享";
        return mediaObject;
    }

    public static void sinaOauth(SsoHandler ssoHandler) {

        ssoHandler.authorize(new WeiboAuthListener() {
            @Override
            public void onComplete(Bundle bundle) {
                Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
                if (mAccessToken.isSessionValid()) {
                    String token = mAccessToken.getToken();
                    String refreshToken = mAccessToken.getRefreshToken();
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("weiboAccessToken", token);
                    map.put("refreshToken", refreshToken);
                    PlatformEngine.getInstance().getSinaManager().onOssSuccess(map);
                } else {
                    PlatformEngine.getInstance().getSinaManager().onOssFail();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                PlatformEngine.getInstance().getSinaManager().onOssFail();
            }

            @Override
            public void onCancel() {
                PlatformEngine.getInstance().getSinaManager().onOssCanceled();
            }
        });
    }

    public static void loginBySina(final Context activity, SsoHandler ssoHandler) {

        ssoHandler.authorize(new WeiboAuthListener() {
            Oauth2AccessToken mAccessToken = null;

            @Override
            public void onComplete(Bundle bundle) {
                mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
                if (mAccessToken.isSessionValid()) {
                    String token = mAccessToken.getToken();
                    String refreshToken = mAccessToken.getRefreshToken();
                    Intent intent = new Intent(IUrlUtils.Constans.LOAGIN_CODE_SINA);
                    intent.putExtra(IUrlUtils.Constans.CALL_TOKEN, token);
                    intent.putExtra(IUrlUtils.Constans.CALL_REFRESH_TOKEN, refreshToken);
                    activity.sendBroadcast(intent);
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                showToast(activity, e.getMessage());
            }

            @Override
            public void onCancel() {
                showToast(activity, "取消新浪微博登录");
            }
        });
    }

    private static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
