package com.fengnian.smallyellowo.foodie.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.widget.Toast;

import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.mm.opensdk.openapi.IWXAPI;


/**
 * Created by ntop on 15/9/4.
 */
public class SinaEntryActivity extends Activity implements IWeiboHandler.Response {
    private IWXAPI api;

    public static Runnable runnable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            finish();
            return;
        }
        IWeiboShareAPI mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, SinaOpen.APP_KEY);
        mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        finish();
    }


    private void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onResponse(BaseResponse baseResp) {
        if (baseResp != null) {
            switch (baseResp.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    Intent event = new Intent(IUrlUtils.Constans.SHARE_MESSAGE_WECHAT);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(event);
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    Toast.makeText(this, "取消分享", Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    Toast.makeText(this, "分享失败：" + baseResp.errMsg, Toast.LENGTH_LONG).show();
                    break;
            }
        }

    }
}
