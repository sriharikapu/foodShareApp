package com.fengnian.smallyellowo.foodie;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.feeddetail.FastDetailActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.DynamicDetailIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.FastDetailIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.LoginIntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.RestInfoIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.login.LoginActivity;
import com.fengnian.smallyellowo.foodie.receivers.SYSchemeData;

/**
 * Created by lanbiao on 2017/1/10.
 */

public class ApplicationBackActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = null;
        IntentData intentData = null;
        Intent intent = getIntent();
        Uri uri = intent.getData();
        SYSchemeData schemeData = FFUtils.getSchemeData(uri);

        int detailInfoTypeCode = 0;
        String detailInfoId = null;
        if (schemeData.getParams().size() <= 0) {
            finish();
            return;
        }

        String param = schemeData.getParams().get(0);
        String[] paramList = param.split("&");
        if (paramList.length >= 2) {
            detailInfoTypeCode = Integer.parseInt(paramList[0]);
            detailInfoId = paramList[1];
        } else if (paramList.length >= 1) {
        }

        if (FFUtils.isStringEmpty(SP.getToken())) {//未登陆
            activity = new LoginActivity();
            LoginIntentData loginIntentData = new LoginIntentData();
            loginIntentData.setCode(param);
            intentData = loginIntentData;
            //这个地方需要处理一下，已经显示存在登陆activity的情况
            startActivity(activity.getClass(), intentData);
            finish();
            return;
        } else {//已登陆
            if (schemeData.getHost() == 1) {
                finish();
                return;
            }
        }


        switch (schemeData.getHost()) {
            case 1:

                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                activity = loadOtherActivity(detailInfoTypeCode);
                if (detailInfoTypeCode == 1) {
                    DynamicDetailIntent detail = new DynamicDetailIntent();
                    detail.setId(detailInfoId);
                    intentData = detail;
                } else if (detailInfoTypeCode == 2) {
                    FastDetailIntent fastDetailIntent = new FastDetailIntent(detailInfoId);
                    fastDetailIntent.setMineMode(true);
                    intentData = fastDetailIntent;
                } else if (detailInfoTypeCode == 3) {
                    RestInfoIntent restInfoIntentIntent = new RestInfoIntent();
                    restInfoIntentIntent.setId(detailInfoId);
                    intentData = restInfoIntentIntent;
                } else if (detailInfoTypeCode == 4) {
//                    PGCDetailIntent userInfoIntent = new PGCDetailIntent();
//                    userInfoIntent.setId(detailInfoId);
//                    intentData = userInfoIntent;
                } else if (detailInfoTypeCode == 5) {
                    UserInfoIntent userInfoIntent = new UserInfoIntent();
                    userInfoIntent.setId(detailInfoId);
                    intentData = userInfoIntent;
                } else if (detailInfoTypeCode == 6) {
                    DynamicDetailIntent detail = new DynamicDetailIntent();
                    detail.setId(detailInfoId);
                    detail.setMineMode(true);
                    intentData = detail;
                } else if (detailInfoTypeCode == 7) {
                    FastDetailIntent detail = new FastDetailIntent();
                    detail.setId(detailInfoId);
                    detail.setMineMode(true);
                    intentData = detail;
                }
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            default:
                break;
        }

        if (activity != null) {
            if (activity instanceof BaseActivity) {
                startActivity(activity.getClass(), intentData);
            } else {
                startActivity(activity.getClass(), intentData);
            }
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private Activity loadOtherActivity(int detailInfoTypeCode) {
        Activity activity = null;
        if (detailInfoTypeCode == 1) {
            //富文本详情
            activity = new DynamicDetailActivity();
        } else if (detailInfoTypeCode == 2) {
            //速记详情
            activity = new FastDetailActivity();
        } else if (detailInfoTypeCode == 3) {
            //商户详情
            activity = new RestInfoActivity();
        } else if (detailInfoTypeCode == 4) {
            //PGC详情
//            activity = new PGCDetailActivity();
        } else if (detailInfoTypeCode == 5) {
            //外部扫描二维码跳转小黄圈打开用户外部页面
            activity = new UserInfoActivity();
        } else if (detailInfoTypeCode == 6) {
            //外部扫描二维码跳转小黄圈打开用户外部页面
            activity = new MainActivity();
        } else if (detailInfoTypeCode == 7) {
            //外部扫描二维码跳转小黄圈打开用户外部页面
            activity = new MainActivity();
        }
        return activity;
    }

}
