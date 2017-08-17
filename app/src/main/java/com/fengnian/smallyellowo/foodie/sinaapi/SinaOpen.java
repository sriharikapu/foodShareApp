//package com.fengnian.smallyellowo.foodie.sinaapi;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.view.Gravity;
//import android.widget.Toast;
//
//import com.fengnian.smallyellowo.foodie.R;
//import com.fengnian.smallyellowo.foodie.wxapi.WeixinOpen;
//import com.sina.weibo.sdk.api.TextObject;
//import com.sina.weibo.sdk.api.WebpageObject;
//import com.sina.weibo.sdk.api.WeiboMultiMessage;
//import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
//import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
//import com.sina.weibo.sdk.api.share.WeiboShareSDK;
//import com.sina.weibo.sdk.utils.Utility;
//import com.tencent.mm.opensdk.openapi.IWXAPI;
//import com.tencent.mm.opensdk.openapi.WXAPIFactory;
//
//import java.net.URL;
//import java.util.HashMap;
//
///**
// * Created by Administrator on 2016-10-19.
// */
//
//public class SinaOpen {
//    private IWXAPI api;
//    /** 微博微博分享接口实例 */
//    private IWeiboShareAPI mWeiboShareAPI = null;
//    private Context mContext;
//    private static final int THUMB_SIZE = 120;
//
//    private  final static String Sina_app_key="3451219723";
//    private SinaOpen(Context context) {
//        mContext=context;
//        initWeiBo();
//
//    }
//
//    public IWeiboShareAPI getWeiBoApi(){
//        return mWeiboShareAPI;
//    }
//
//    private static  SinaOpen  instance;
//    public static SinaOpen getInstance(Context context){
//        instance = new SinaOpen(context);
//        return instance;
//    }
//
//    private void initWeiBo(){
//        // 创建微博分享接口实例
//        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mContext, Sina_app_key, true);
//
//        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
//        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
//        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
//        mWeiboShareAPI.registerApp();
//
//        // 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
//        // 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
//        // 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
//        // 失败返回 false，不调用上述回调
//        api = WXAPIFactory.createWXAPI(mContext, Sina_app_key);
//    }
//
//    public void sendMutiplyMessage(final HashMap<String, String> temp){
//        // 1. 初始化微博的分享消息
//        if(!mWeiboShareAPI.isWeiboAppInstalled() || !mWeiboShareAPI.isWeiboAppSupportAPI()){
//            Toast toast=Toast.makeText(mContext, "请下载新浪微博客户端", Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER,0,0);
//            toast.show();
//            return;
//        }
//        new Thread(){
//            @Override
//            public void run() {
//                WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
//                if(temp.containsKey("imageUrl")){
//                    weiboMessage.mediaObject = getWebpageObj(temp);
//                } else {
//                    weiboMessage.textObject = getTextObject(temp);
//                }
//                // 2. 初始化从第三方到微博的消息请求
//                SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
//                // 用transaction唯一标识一个请求
//                request.transaction = String.valueOf(System.currentTimeMillis());
//                request.multiMessage = weiboMessage;
//
//                // 3. 发送请求消息到微博，唤起微博分享界面
//                mWeiboShareAPI.sendRequest((Activity) mContext, request);
//            }
//        }.start();
//    }
//
//    /**
//     * 创建多媒体（网页）消息对象。
//     *
//     * @return 多媒体（网页）消息对象。
//     */
//    private WebpageObject getWebpageObj(HashMap<String, String> temp) {
//        WebpageObject mediaObject = new WebpageObject();
//        mediaObject.identify = Utility.generateGUID();
//        mediaObject.title = "小黄圈";
//        mediaObject.description = (String) temp.get("text");
//        if(temp.containsKey("imageUrl")){
//            try {
//                String imgUrl = (String) temp.get("imageUrl");
//                Bitmap bmp = BitmapFactory.decodeStream(new URL(imgUrl)
//                        .openStream());
//                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE,
//                        THUMB_SIZE, true);
//
//                byte [] data = WeixinOpen.bmpToByteArray(thumbBmp, true);
//                mediaObject.thumbData = data;
//                bmp.recycle();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }else{
//            Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.logo);
//            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE,
//                    THUMB_SIZE, true);
//            byte [] data = WeixinOpen.bmpToByteArray(thumbBmp, true);
//
//            mediaObject.thumbData = data;
//            bmp.recycle();
//        }
//        // 设置 Bitmap 类型的图片到视频对象里
//        if(temp.containsKey("url"))
//            mediaObject.actionUrl = (String) temp.get("url");
//        mediaObject.defaultText = "Webpage 默认文案";
//        return mediaObject;
//    }
//
//    public TextObject getTextObject(HashMap<String, String> temp){
//        TextObject tObject = new TextObject();
//        tObject.identify = Utility.generateGUID();
//        tObject.title =  temp.get("title");
//        tObject.description =  temp.get("text");
//        tObject.text = temp.get("title")+" "+temp.get("url");
//        tObject.actionUrl =  temp.get("url");
//        return tObject;
//    }
//}
