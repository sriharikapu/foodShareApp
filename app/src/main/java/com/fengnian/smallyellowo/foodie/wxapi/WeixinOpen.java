package com.fengnian.smallyellowo.foodie.wxapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.fan.framework.base.FFApplication;
import com.fan.framework.utils.FFLogUtil;
import com.fengnian.smallyellowo.foodie.R;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class WeixinOpen {

    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
    //正式微信id
//    public static final String APP_ID_WEIXIN = BuildConfig.DEBUG ? "wx0e9399ee2123cc13" : "wx08d9a51582f90600";
    public static final String APP_ID_WEIXIN ="wx08d9a51582f90600";
    private static WeixinOpen instance;
    private IWXAPI api;

    private WeixinOpen() {
        api = WXAPIFactory.createWXAPI(FFApplication.app, APP_ID_WEIXIN, false);
        // 将该app注册到微信
        api.registerApp(APP_ID_WEIXIN);
        instance = this;
    }

    public static WeixinOpen getInstance() {
        if (instance == null) {
            instance = new WeixinOpen();
        }
        return instance;
    }

    public void share2weixin(String url, String text, String title,
                             Bitmap thumb) {
        FFLogUtil.d(null,
                "url:" + url + "-----" + text + "--" + title);
        int version = api.getWXAppSupportAPI();
        if (version == 0) {
            FFApplication.showToast("请先安装微信！", null);
            return;
        }
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = text;

        if (thumb != null) {
            Bitmap thumbBmp = Bitmap.createScaledBitmap(thumb, 100, 100, true);
            thumb = thumbBmp;
            msg.thumbData = bmpToByteArray(thumb, false);
        }


        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    /**
     * 微信分享图片
     *
     * @param bmp
     */
    public void share2weixin(Context context, String bmp) {

        if(bmp == null){
            FFApplication.showToast("生成失败！", null);
            return;
        }
        File file = new File(bmp);
        if(!file.exists() || file.length() == 0){
            FFApplication.showToast("生成失败！", null);
            return;
        }
        int version = api.getWXAppSupportAPI();
        if (version == 0) {
            FFApplication.showToast("请先安装微信！", null);
            return;
        }
        if (version < TIMELINE_SUPPORTED_VERSION) {
            FFApplication.showToast("您当前微信版本不支持朋友圈！", null);
            return;
        }
        WXMediaMessage msg = new WXMediaMessage();
        if (bmp != null) {
            WXImageObject imageObject = new WXImageObject();
            imageObject.imagePath = bmp;
            msg.mediaObject = imageObject;
        } else {
            Bitmap bmp_map = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            WXImageObject imageObject = new WXImageObject(bmp_map);
            msg.mediaObject = imageObject;
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        req.transaction = buildTransaction("img");
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    /**
     * 微信分享图片 gei haoyou
     *
     * @param bmp
     */
    public void share2weixinpy(Context context, String bmp) {
        int version = api.getWXAppSupportAPI();
        if (version == 0) {
            FFApplication.showToast("请先安装微信！", null);
            return;
        }
        if (version < TIMELINE_SUPPORTED_VERSION) {
            FFApplication.showToast("您当前微信版本不支持朋友圈！", null);
            return;
        }
        WXMediaMessage msg = new WXMediaMessage();
        if (bmp != null) {
            WXImageObject imageObject = new WXImageObject();
            imageObject.imagePath = bmp;
            msg.mediaObject = imageObject;
        } else {
            Bitmap bmp_map = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            WXImageObject imageObject = new WXImageObject(bmp_map);
            msg.mediaObject = imageObject;
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        req.transaction = buildTransaction("img");
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    public static byte[] bmpToByteArray(final Bitmap bmp,
                                        final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public void share2weixin_pyq(String url, String text, String title,
                                 Bitmap bitmap) {
        int version = api.getWXAppSupportAPI();
        if (version == 0) {
            FFApplication.showToast("请先安装微信！", null);
            return;
        }
        if (version < TIMELINE_SUPPORTED_VERSION) {
            FFApplication.showToast("您当前微信版本不支持朋友圈！", null);
            return;
        }
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = text;

        if (bitmap != null) {
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            bitmap = thumbBmp;
            msg.thumbData = bmpToByteArray(bitmap, false);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        boolean bo = api.sendReq(req);
        FFLogUtil.e("微信分享", "" + bo);
    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    public void AuthLgin(){

        int version = checkInstall();

        if (version == 0) {
            FFApplication.showToast("找不到微信啦,\n请下载微信后重试！", null);
            return;
        }
        if (version < TIMELINE_SUPPORTED_VERSION) {
            FFApplication.showToast("您当前微信版本不支持朋友圈！", null);
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        //授权读取用户信
        req.scope = "snsapi_userinfo";
        //自定义信息
        req.state = "wechat_sdk_small_yellow_circle";

        //向微信发送请求
        api.sendReq(req);
    }

    public int checkInstall(){
        return api.getWXAppSupportAPI();
    }
}
