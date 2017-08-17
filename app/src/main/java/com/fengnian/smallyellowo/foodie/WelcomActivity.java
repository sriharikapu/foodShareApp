package com.fengnian.smallyellowo.foodie;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.fan.framework.config.FFConfig;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFLogUtil;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.xtaskmanager.XTaskManagerServices;
import com.fengnian.smallyellowo.foodie.appbase.APP;
import com.fengnian.smallyellowo.foodie.appbase.CityPref;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.LoginIntentData;
import com.fengnian.smallyellowo.foodie.login.SetNicknameOneActivty;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.igexin.sdk.PushManager;

import java.util.List;


public class WelcomActivity extends PermissionActivity implements ServiceConnection {
    private boolean isHasFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (allActivis.size() > 2) {
            moveTaskToFront(((Activity) allActivis.get(allActivis.size() - 2)));
            finish();
            return;
        }
        PushManager.getInstance().initialize(this.getApplicationContext(), null);
        setNotitle(true);//设置无标题 去掉框架自带actionBar
        setContentView(R.layout.activity_welcom);

        ImageView channelImage = (ImageView) findView(R.id.channel_icon);
        isHasFirst = ChannelFirstUtils.setFirstChannelFlag(channelImage);

        if (start()) {
//            checkVersion(true);
        }
    }

    private ActivityManager.RunningAppProcessInfo getRunningAppProcessInfo(String packageName) {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processList = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo p : processList) {
            if (p.processName.equals(packageName)) {
                return p;
            }
        }
        return null;
    }

    private boolean isForeground(String packageName) {
        ActivityManager.RunningAppProcessInfo processInfo = getRunningAppProcessInfo(packageName);
        if (processInfo != null) {
            return ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND == processInfo.importance;
        }
        return false;
    }

    private boolean moveTaskToFront(Activity activity) {
        ActivityManager am = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        am.moveTaskToFront(activity.getTaskId(), 0);
        return isForeground(getPackageName());
    }


    private void checkVersion(final boolean hasPermission) {
        FFConfig.LOG2SD_ENABLE = hasPermission;
        FFImageLoader.init(hasPermission);
        startAnim();

//        APP.app.post(Constants.shareConstants().getNetHeaderAdress() + "/oss/getOssSettings.do", null, new FFNetWorkCallBack<BaseResult>() {
        APP.app.post(IUrlUtils.Search.getOssSettings, null, new FFNetWorkCallBack<BaseResult>() {

            @Override
            public void onSuccess(BaseResult response, FFExtraParams extra) {
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return true;
            }
        });
    }

    private void onUpdata(final CheckVersionResult response) {
        if (response.getType() != 0) {
            if (allActivis.size() == 0) {
                return;
            }

            if (response.getType() == 1) {
                if (SP.isVersionIgnored(response.getVersionNew())) {
                    return;
                }

                EnsureDialog.showEnsureDialog(allActivis.get(0), true, "您当前版本较低，需要更新后才能继续使用！\r\n如有问题，请联系小黄圈官方微信号：xiaohuangquan517", "更新", "下次提醒", "忽略", new EnsureDialog.EnsureDialogListener1() {
                    @Override
                    public void onCenter(DialogInterface dialog) {
                        SP.setIgnore(response.getVersionNew());
                    }

                    @Override
                    public void onOk(DialogInterface dialog) {
                        Uri uri = Uri.parse(response.getUrl());
                        Intent it = new Intent(Intent.ACTION_VIEW, uri);
                        allActivis.get(0).startActivity(it);
                    }

                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                });
            } else {//强制更新
                EnsureDialog.showEnsureDialog(allActivis.get(0), false, "您当前版本过低，需要更新后才能继续使用！\r\n如有问题，请联系小黄圈官方微信号：xiaohuangquan517.", "更新", null, null, new EnsureDialog.EnsureDialogListener() {
                    @Override
                    public void onOk(DialogInterface dialog) {
                        Uri uri = Uri.parse(response.getUrl());
                        Intent it = new Intent(Intent.ACTION_VIEW, uri);
                        allActivis.get(0).startActivity(it);
                    }

                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                });
            }
        }
    }

    @Override
    void onPermissionsAllowed() {
        FFUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!getDestroyed()) {
                    TextView tv = (TextView) findViewById(R.id.tv_skip);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onAnimationEnd();
                        }
                    });
                }

            }
        }, 1000);

        if (FFUtils.isStringEmpty(SP.getYoukeToken())) {
            post(IUrlUtils.Search.visitorRegister, "", null, new FFNetWorkCallBack<VisitorResult>() {
                @Override
                public void onSuccess(VisitorResult response, FFExtraParams extra) {
                    SP.setYoukeToken(response.account);
                    checkVersion(true);
                }

                @Override
                public boolean onFail(FFExtraParams extra) {
                    checkVersion(true);
                    return false;
                }
            });
        }else {
            checkVersion(true);
        }
    }

    public static class VisitorResult extends BaseResult {
        public String account;
    }


    public static class CheckVersionResult extends BaseResult {
        int type;
        String versionNew;
        String url;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getVersionNew() {
            return versionNew;
        }

        public void setVersionNew(String versionNew) {
            this.versionNew = versionNew;
        }

    }


    private void startAnim() {
        if ((getIntent().getStringExtra("hhh")) != null) {//如果是崩溃后重启  不需要等待
            WelcomActivity.this.onAnimationEnd();
            return;
        }

        //为了提高应用打开速度：这里仅仅是为了显示首发标志而已，所以有首发才走这里 by chenglin
        if (isHasFirst){
            FFUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isFinishing() && !getDestroyed()) {
                        WelcomActivity.this.onAnimationEnd();
                    }
                }
            }, 400);
        }else {
            WelcomActivity.this.onAnimationEnd();
        }
    }

    private void onAnimationEnd() {
//        APP.app.post(Constants.shareConstants().getNetHeaderAdress() + "/version/detectionVersonUpdate.do", null, new FFNetWorkCallBack<CheckVersionResult>() {
        APP.app.post(IUrlUtils.Search.detectionVersonUpdate, null, new FFNetWorkCallBack<CheckVersionResult>() {
            @Override
            public void onSuccess(final CheckVersionResult response, final FFExtraParams extra) {
                onUpdata(response);
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "nowVersion", FFUtils.getVerName(), "os", 2);

        SYUser info = SP.getUser();
        FFLogUtil.e("nick name", "nick name = " + info.getNickName());
        if (TextUtils.isEmpty(info.getNickName()) || !FFUtils.checkString(info.getNickName())) {
            LoginIntentData data = new LoginIntentData();
            data.setRequestCode(20001);
            startActivity(SetNicknameOneActivty.class, data);

        } else {

            if (CityPref.isFirstStartApp()){
                startActivity(SelectCityActivity.class, new IntentData());
                finish();
                return;
            }

            if (FFUtils.isStringEmpty(SP.getYoukeToken())) {
                startActivity(Main2Activity.class, new IntentData());
            } else {
                startActivity(MainActivity.class, new IntentData());
            }
            finish();
        }

    }


    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (iBinder instanceof XTaskManagerServices.XLocalTaskBind) {
            XTaskManagerServices.XLocalTaskBind binder = (XTaskManagerServices.XLocalTaskBind) iBinder;
            XTaskManagerServices taskManagerServices = binder.getServices();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case 20001:
                checkVersion(true);
                break;
        }
    }

}