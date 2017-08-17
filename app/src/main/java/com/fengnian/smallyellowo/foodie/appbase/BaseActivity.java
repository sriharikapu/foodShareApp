package com.fengnian.smallyellowo.foodie.appbase;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.fan.framework.base.FFBaseActivity;
import com.fan.framework.http.FFNetWorkRequest;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.MainActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appconfig.ActivityTags;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.fragments.HomeChildDynamicFrag;
import com.fengnian.smallyellowo.foodie.fragments.MainDiscoverFragment;
import com.fengnian.smallyellowo.foodie.fragments.RelationshipFragment;
import com.fengnian.smallyellowo.foodie.homepage.HomeChildMeetFrag;
import com.fengnian.smallyellowo.foodie.homepage.HomeChildNearbyFrag;
import com.fengnian.smallyellowo.foodie.homepage.HomeChildSelectedFrag;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.login.LoginOneActivity;
import com.fengnian.smallyellowo.foodie.personal.MainMyUserFragment;
import com.fengnian.smallyellowo.foodie.receivers.PushManager;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.HashMap;

public abstract class BaseActivity<T extends IntentData> extends FFBaseActivity {
    private String addMenu;
    private Button menu;

    @Override
    public BaseActivity context() {
        return this;
    }

//    private String getFrom() {
//
////        if (getChildTags() != null) {
////            if (getChildTags().contains(ActivityTags.setting)) {
////                return null;
////            } else if (getChildTags().contains(ActivityTags.mainHomeDynamic)) {
////                return "动态";
////            } else if (getChildTags().contains(ActivityTags.mainHomeUGC)) {
////                return "精选";
////            } else if (getChildTags().contains(ActivityTags.mainHomeAct)) {
////                return "活动";
////            } else if (getChildTags().contains(ActivityTags.mainHomeAround)) {
////                return "附近";
////            } else if (getChildTags().contains(ActivityTags.mainDiscover)) {
////                return "发现";
////            } else if (getChildTags().contains(ActivityTags.mainSNS)) {
////                return "社交";
////            } else if (getChildTags().contains(ActivityTags.mainUser)) {
////                return "个人";
////            }
////        }
//        return null;
//    }

    @Override
    public Button addMenu(String text, View.OnClickListener listener) {
        if (menu != null) {
            ((ViewGroup) menu.getParent()).removeView(menu);
            menu = null;
        }
        return super.addMenu(text, listener);
    }

    @Override
    public ImageButton addMenu(int iconId, View.OnClickListener listener) {
        if (menu != null) {
            ((ViewGroup) menu.getParent()).removeView(menu);
            menu = null;
        }
        return super.addMenu(iconId, listener);
    }

    public void close(View v) {
        finishAllActivitysByTag(ActivityTags.main);
    }

    private boolean isYouke = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View iv_actionbar_close = findViewById(R.id.iv_actionbar_close);
        if (iv_actionbar_close != null) {
            if (showClose()) {
                iv_actionbar_close.setVisibility(View.VISIBLE);
            } else {
                iv_actionbar_close.setVisibility(View.GONE);
            }
        }
        //备注：这个方法是友盟推送的方法，不是友盟统计的方法。此方法与统计分析sdk中统计日活的方法无关！请务必调用此方法！
        //如果不调用此方法，不仅会导致按照"几天不活跃"条件来推送失效，还将导致广播发送不成功以及设备描述红色等问题发生。
        //可以只在应用的主Activity中调用此方法，但是由于SDK的日志发送策略，有可能由于主activity的日志没有发送成功，而导致未统计到日活数据
        //文档地址：http://dev.umeng.com/push/android/integration
        PushAgent.getInstance(this).onAppStart();

        if (savedInstanceState == null) {
            isYouke = TextUtils.isEmpty(SP.getUid()) ? true : false;
        }

//        String from = getFrom();
//        if (from != null) {
//            menu = super.addMenu(from, new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    finishAllActivitysByTag(ActivityTags.main);
//                }
//            });
//        }
        if (isFrom(MainActivity.class)) {
            addChildTag(ActivityTags.main);
            if (isFrom(MainActivity.class, HomeChildSelectedFrag.class)) {//精选
                addChildTag(ActivityTags.mainHomeUGC);
            } else if (isFrom(MainActivity.class, HomeChildDynamicFrag.class)) {//动态
                addChildTag(ActivityTags.mainHomeDynamic);
            } else if (isFrom(MainActivity.class, HomeChildMeetFrag.class)) {//活动
                addChildTag(ActivityTags.mainHomeAct);
            } else if (isFrom(MainActivity.class, HomeChildNearbyFrag.class)) {//附近
                addChildTag(ActivityTags.mainHomeAround);
            } else if (isFrom(MainActivity.class, MainDiscoverFragment.class)) {
                addChildTag(ActivityTags.mainDiscover);
            } else if (isFrom(MainActivity.class, RelationshipFragment.class)) {
                addChildTag(ActivityTags.mainSNS);
            } else if (isFrom(MainActivity.class, MainMyUserFragment.class)) {
                addChildTag(ActivityTags.mainUser);
            }
        }
    }


    public boolean isFrount = false;

    protected void onPause() {
        super.onPause();
        isFrount = false;
//        MobclickAgent.onPageEnd(getClass().getSimpleName());
        MobclickAgent.onPause(this);
    }

    protected void onResume() {
        super.onResume();
        isFrount = true;
        boolean isYouke = TextUtils.isEmpty(SP.getUid()) ? true : false;
        if (isYouke != this.isYouke) {
            refreshAfterLogin();
        }
        this.isYouke = isYouke;
//        MobclickAgent.onPageStart(getClass().getSimpleName());
        MobclickAgent.onResume(this);
    }

//    protected boolean hasDismissSoft = false;

    @Override
    protected void onPostResume() {
        super.onPostResume();
//
//        if (!hasDismissSoft) {
//            hasDismissSoft = true;
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    FFUtils.setSoftInputInvis(getContainer().getWindowToken());
//                }
//            }, 200);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        FFUtils.setSoftInputInvis(getContainer().getWindowToken());
        super.finish();
    }

    @Override
    protected void onSetContentView() {
        View iv_close = findViewById(R.id.iv_close);
        if (iv_close != null) {
            if (showClose()) {
                iv_close.setVisibility(View.VISIBLE);
            } else {
                iv_close.setVisibility(View.GONE);
            }
        }
    }

    public boolean showClose() {
        if (isFrom(MainActivity.class)) {
            return false;
        }

        if (getChildTags().contains(ActivityTags.fast_edit)) {
            return false;
        }
        if (getChildTags().contains(ActivityTags.rich_edit)) {
            return false;
        }

        return true;
    }

    public T getIntentData() {
        return (T) getIntent().getParcelableExtra("IntentData");
    }

    private static boolean isShow = false;

    public static void logout(final int code, final String url) {

        if (!allActivis.isEmpty()) {
            if (code == 1) {//退出
                if (isShow) {
                    return;
                } else {
                    isShow = true;
                }

                Dialog exitDialog = EnsureDialog.showEnsureDialog(allActivis.get(allActivis.size() - 1), false, "您的账号可能在其他设备登录，\n请重新登录！", "退出", null, null, new EnsureDialog.EnsureDialogListener() {
                    @Override
                    public void onOk(DialogInterface dialog) {
                        dialog.dismiss();
                        logout();
                    }

                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                }, true);

                exitDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        isShow = false;
                    }
                });

            } else if (code == 9) {//
                EnsureDialog.showEnsureDialog(allActivis.get(allActivis.size() - 1), false, "该账号已冻结，\n请联系小黄圈官方微信：tinydonuts！", "退出", null, null, new EnsureDialog.EnsureDialogListener() {
                    @Override
                    public void onOk(DialogInterface dialog) {
                        dialog.dismiss();
                        logout();
                    }

                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                }, true);
            } else if (code == 10) {//更新
                EnsureDialog.showEnsureDialog(allActivis.get(allActivis.size() - 1), false, "您当前版本过低，需要更新后才能继续使用！\r\n如有问题，请联系小黄圈官方微信号：xiaohuangquan517.", "更新", null, null, new EnsureDialog.EnsureDialogListener() {
                    @Override
                    public void onOk(DialogInterface dialog) {
                        Uri uri = Uri.parse(url);
                        Intent it = new Intent(Intent.ACTION_VIEW, uri);
                        allActivis.get(0).startActivity(it);
                    }

                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                }, true);
            } else if (code == 601) {
                logout();
            }
        }
        SP.ClearUserSp();
    }

    @Deprecated
    @Override
    public void startActivity(Intent intent) {
        if (intent == null) {
            return;
        }
        super.startActivity(intent);
    }

    public static void logout() {
        UPushHelper.removePushAlias(APP.app, SP.getUid());
        if(!allActivis.isEmpty()) {
            BaseActivity baseActivity = (BaseActivity) allActivis.get(allActivis.size() - 1);
            baseActivity.startActivity(MainActivity.class, new IntentData());
        }
        SP.ClearUserSp();
        PushManager.logout();
    }

    public void refreshAfterLogin() {
    }

    @Deprecated
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (intent == null) {
            return;
        }
        super.startActivityForResult(intent, requestCode);
    }


    public <G extends IntentData> void startActivity(@NonNull Class<? extends BaseActivity<G>> clazz, G data) {
        if (getDestroyed()) {
            return;
        }
        startActivityForResult(new Intent(this, clazz).putExtra("IntentData", data), data.getRequestCode());
        if (clazz.getName().equals(LoginOneActivity.class.getName())) {
            overridePendingTransition(R.anim.switch_muban_activity_in, 0);
        }
    }

    public boolean hasPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

//
//    public <G extends IntentData> void startFragmentActivity(@NonNull Class<? extends BaseFragmentActivity<G>> clazz, G data) {
//        startActivityForResult(new Intent(this, clazz).putExtra("IntentData", data), data.getRequestCode());
//    }

    @Deprecated
    @Override
    public Intent getIntent() {
        return super.getIntent();
    }

    /**
     * 省掉强制转换
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    /**
     * 删除全部
     */
    public static void finishActvities() {
        if (allActivis.size() > 0) {
            for (Activity activity : allActivis) {
                if (activity != null) {
                    activity.finish();
                }
            }
        }
    }

    @Override
    public void onPageInitFail(FFNetWorkRequest request) {
        super.onPageInitFail(request);
    }

    @Override
    public void onPageInitNoNet(FFNetWorkRequest request) {
        super.onPageInitNoNet(request);
    }

    @Override
    public void onPageInitNoData(FFNetWorkRequest request) {
        super.onPageInitNoData(request);
    }

    @Override
    public void onPageInitHasData(FFNetWorkRequest request) {
        super.onPageInitHasData(request);
    }

    public static BaseActivity getTopActivity() {
        if (allActivis.size() > 0) {
            return (BaseActivity) allActivis.get(allActivis.size() - 1);
        }
        return null;
    }

    public void pushEventAction(String eventId) {
        MobclickAgent.onEvent(this, eventId);//统计新注册用户个数
    }

    public void pushEventAction(String eventId, HashMap<String, String> map) {

        MobclickAgent.onEvent(this, eventId, map);//统计新注册用户个数
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == com.tencent.connect.common.Constants.REQUEST_LOGIN && PlatformEngine.getInstance().getTencentManager().getListener() != null) {
            Tencent.onActivityResultData(requestCode, resultCode, data, PlatformEngine.getInstance().getTencentManager().getListener());
        }

        if (PlatformEngine.getInstance().getSinaManager().getSsoHandler() != null) {
            PlatformEngine.getInstance().getSinaManager().getSsoHandler().authorizeCallBack(requestCode, resultCode, data);
        }
    }
}
