package com.fengnian.smallyellowo.foodie.sinaapi;

import android.content.Intent;
import android.view.View;

import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;

/**
 * Created by Administrator on 2016-10-19.
 */

public class WBShareActivity extends BaseActivity<IntentData> implements View.OnClickListener, IWeiboHandler.Response{


    @Override
    public void onClick(View view) {

    }



    @Override
    public void onResponse(BaseResponse baseResponse) {
        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:


                showToast("分享成功");
                Intent intent = new Intent("share_sucessful");
                sendBroadcast(intent);
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                showToast("取消分享");
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                showToast("分享失败");
                break;
        }
        finish();

    }
    }

