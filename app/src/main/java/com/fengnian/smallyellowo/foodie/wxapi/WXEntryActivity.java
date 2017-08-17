package com.fengnian.smallyellowo.foodie.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.widget.Toast;

import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appbase.WeiChatOverdueManager;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * Created by ntop on 15/9/4.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler, IWeiboHandler.Response {
    private IWXAPI api;

    public static Runnable runnable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, WeixinOpen.APP_ID_WEIXIN, false);
        api.handleIntent(getIntent(), this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        String result = "";


//        if(resp instanceof SendAuth.Resp){
//            SendAuth.Resp newResp = (SendAuth.Resp) resp;
//
//            //获取微信传回的code
//            String code = newResp.code;
//        }


        switch (resp.errCode) {

            case BaseResp.ErrCode.ERR_OK:
                result = "发送成功";
                if (resp instanceof SendAuth.Resp) {  //autho 登录
                    if ("wechat_sdk_small_yellow_circle".equals(((SendAuth.Resp) resp).state)) {  //防止篡改

                        SendAuth.Resp newResp = (SendAuth.Resp) resp;

                        //获取微信传回的code
                        String code = newResp.code;
                        SP.setWeiChatCode(code);
//                        请求后台
//                        Intent intent = new Intent("autho_login");
                        Intent intent = new Intent(IUrlUtils.Constans.LOAGIN_CODE_WECHAT);
                        intent.putExtra("token", code);
                        sendBroadcast(intent);

                        WeiChatOverdueManager.onWeiChatOssSuccess(code);
                    }
                } else {
                    Intent intent = new Intent("share_sucessful");
                    sendBroadcast(intent);

                    Intent event = new Intent(IUrlUtils.Constans.SHARE_MESSAGE_WECHAT);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(event);
                }


                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (resp instanceof SendAuth.Resp) {  //autho 登录
                    showToast("操作未完成时用户退出");
                    WeiChatOverdueManager.onWeiChatOssCanceled();
                } else {
                    result = "取消分享";
                    showToast(result);
                }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                if (resp instanceof SendAuth.Resp) {  //autho 登录

                    WeiChatOverdueManager.onWeiChatOssFail();
                }
                result = "认证失败";
                showToast(result);

                break;
            default:
                WeiChatOverdueManager.onWeiChatOssFail();

        }
        finish();
    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        showToast("asdfasdf");
    }
}
