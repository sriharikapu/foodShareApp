package com.fengnian.smallyellowo.foodie.personal;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.results.IsPushResult;
import com.fengnian.smallyellowo.foodie.bean.results.SetAppPushSwitchResult;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.NotificationsUtils;

/**
 * Created by Administrator on 2016-12-20.
 */

public class PushMessageActivity extends BaseActivity<IntentData> implements View.OnClickListener{

    private RelativeLayout rl_allow_push, rl_comment ,rl_jieshou_comment,rl_1,rl_2,rl_3,rl_new_content_push;

    private ToggleButton tl_psuh_toggle,tl_comment_toggle,tl_jieshou_toggle,tl_new_conntent_toggle;

    private TextView tv_comment,tv_jieshou_comment,tv_new_content_push,tv_not_kaqi_comment,tv_not_jieshou_comment,tv_not_new_content_push;

    private int rl_comment_num=1,rl_jieshou_comment_num=1,rl_new_content_push_num=1;

    private boolean  tl_psuh_toggle_flag,tl_comment_toggle_flag,tl_jieshou_toggle_flag,tl_new_conntent_toggle_flag;
    private Drawable img_slector,img_not_selector,drawable;
    int currentapiVersion;//Android  sdk 版本
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_message);

        rl_allow_push= findView(R.id.rl_allow_push);
        rl_comment= findView(R.id.rl_comment);
        rl_jieshou_comment= findView(R.id.rl_jieshou_comment);
        rl_1= findView(R.id.rl_1);
        rl_2= findView(R.id.rl_2);
        rl_3= findView(R.id.rl_3);
        rl_new_content_push= findView(R.id.rl_new_content_push);

        tl_psuh_toggle= findView(R.id.tl_psuh_toggle);
        tl_comment_toggle= findView(R.id.tl_comment_toggle);
        tl_jieshou_toggle= findView(R.id.tl_jieshou_toggle);
        tl_new_conntent_toggle= findView(R.id.tl_new_conntent_toggle);

        tv_comment= findView(R.id.tv_comment);
        tv_jieshou_comment= findView(R.id.tv_jieshou_comment);
        tv_new_content_push= findView(R.id.tv_new_content_push);

        tv_not_kaqi_comment=findView(R.id.tv_not_kaqi_comment);
        tv_not_jieshou_comment=findView(R.id.tv_not_jieshou_comment);
        tv_not_new_content_push=findView(R.id.tv_not_new_content_push);

        setonclic();
        img_slector = this.getResources().getDrawable(R.mipmap.push_xiangshang_img);
        img_not_selector = this.getResources().getDrawable(R.mipmap.push_xiangxia_omg);

        drawable = getResources().getDrawable(R.mipmap.push_message_point);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());

        img_slector.setBounds(0, 0, img_slector.getMinimumWidth(), img_slector.getMinimumHeight());
        img_not_selector.setBounds(0, 0, img_not_selector.getMinimumWidth(), img_not_selector.getMinimumHeight());



    }

    @Override
    protected void onResume() {
        super.onResume();
        if(android.os.Build.VERSION.SDK_INT>=23){
            boolean  lala=     NotificationsUtils.isNotificationEnabled(this);
            if(lala){ //开启权限
//                set("isAppPush","1");
                setkaiguan();
            } else{//未开启权限
                Iskaiqiquanshi();
//                set("isAppPush","0");
            }
        }else{
            setkaiguan();
        }
    }


    private void  Iskaiqiquanshi(){
        new EnsureDialog.Builder(PushMessageActivity.this)
//                        .setTitle("系统提示")//设置对话框标题

                .setMessage("是否开启推送权限")//设置显示的内容

                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮

                    @Override

                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);

                    }

                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
            @Override

            public void onClick(DialogInterface dialog, int which) {//响应事件


            }

        }).show();//在按键响应事件中显示此对话框
    }
    private void setonclic(){
        rl_comment.setOnClickListener(this);
        rl_jieshou_comment.setOnClickListener(this);
        rl_new_content_push.setOnClickListener(this);

        tl_psuh_toggle.setOnClickListener(this);
        tl_comment_toggle.setOnClickListener(this);
        tl_jieshou_toggle.setOnClickListener(this);
        tl_new_conntent_toggle.setOnClickListener(this);
    }

    private boolean  refreshui(boolean  stus,ToggleButton v){
        if(stus){
            v.setCompoundDrawables(null, null, drawable, null);
            v.setBackgroundResource(R.mipmap.me_remind_normal);
            v.setChecked(true);
            return true;
        }else{
            v.setCompoundDrawables(drawable, null,null , null);
            v.setBackgroundResource(R.mipmap.me_remind_pressed);
            v.setChecked(false);
            return false;
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.rl_comment:
                if(rl_comment_num==1){
                    rl_comment_num=2;
                    rl_1.setVisibility(View.VISIBLE);
                    tv_comment.setCompoundDrawables(img_not_selector, null, null, null);
                }else{
                    rl_comment_num=1;
                    rl_1.setVisibility(View.GONE);
                    tv_comment.setCompoundDrawables(img_slector, null, null, null);
                }

                break;
            case R.id.rl_jieshou_comment:

                if(rl_jieshou_comment_num==1){
                    rl_jieshou_comment_num=2;
                    rl_2.setVisibility(View.VISIBLE);
                    tv_jieshou_comment.setCompoundDrawables(img_not_selector, null, null, null);
                }else{
                    rl_jieshou_comment_num=1;
                    rl_2.setVisibility(View.GONE);
                    tv_jieshou_comment.setCompoundDrawables(img_slector, null, null, null);
                }
                break;
            case R.id.rl_new_content_push:
                if(rl_new_content_push_num==1){
                    rl_new_content_push_num=2;
                    rl_3.setVisibility(View.VISIBLE);
                    tv_new_content_push.setCompoundDrawables(img_not_selector, null, null, null);
                }else{
                    rl_new_content_push_num=1;
                    rl_3.setVisibility(View.GONE);
                    tv_new_content_push.setCompoundDrawables(img_slector, null, null, null);
                }

                break;

            case R.id.tl_psuh_toggle:  //允许app推送

                if(currentapiVersion>=23){
                    boolean  lala=     NotificationsUtils.isNotificationEnabled(this);
                    if(lala){//开启权限
//                        showToast("开启权限了已经");
//                        setkaiguan();
                        if(tl_psuh_toggle_flag){
                            set("isAppPush","0");
                        }else {
                            set("isAppPush","1");
                        }

                    } else{//未开启权限
                        Iskaiqiquanshi();
                    }
                }else{
//                    setkaiguan();
                    if(tl_psuh_toggle_flag){
                        set("isAppPush","0");
                    }else {
                        set("isAppPush","1");
                    }
                }


                break;


            case R.id.tl_comment_toggle:

                if(tl_comment_toggle_flag){
                    set("isReview","0");
                }else {
                    set("isReview","1");
                }

                break;
            case R.id.tl_jieshou_toggle:
                if(tl_jieshou_toggle_flag){
                    set("isAppNotice","0");
                }else {
                    set("isAppNotice","1");
                }
                break;
            case R.id.tl_new_conntent_toggle:
//                if( refreshui(tl_new_conntent_toggle_flag,tl_new_conntent_toggle)){
//                    tl_new_conntent_toggle_flag=false;
//                }else {
//                    tl_new_conntent_toggle_flag=true;
//                }
                if( tl_new_conntent_toggle_flag){
                    set("isPGC","0");
                }else {
                    set("isPGC","1");
                }


                break;
        }
    }

    private void   setkaiguan(){
        if (FFUtils.checkNet()) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/config/getPushConfig.do", "", extra, new FFNetWorkCallBack<IsPushResult>() {
        post(IUrlUtils.Search.getPushConfig, "", extra, new FFNetWorkCallBack<IsPushResult>() {
            @Override
            public void onSuccess(IsPushResult response, FFExtraParams extra) {
                if (response.judge()) {
                    tl_psuh_toggle_flag=response.isAppPush();
                    tl_comment_toggle_flag=response.isReview();
                    tl_jieshou_toggle_flag=response.isAppNotice();
                    tl_new_conntent_toggle_flag=response.isPGC();

                    refreshui(tl_psuh_toggle_flag,tl_psuh_toggle);
                    refreshui(tl_comment_toggle_flag,tl_comment_toggle);
                    refreshui(tl_jieshou_toggle_flag,tl_jieshou_toggle);
                    refreshui(tl_new_conntent_toggle_flag,tl_new_conntent_toggle);
                    if(!tl_psuh_toggle_flag){
                        tv_not_kaqi_comment.setVisibility(View.VISIBLE);
                        tv_not_jieshou_comment.setVisibility(View.VISIBLE);
                        tv_not_new_content_push.setVisibility(View.VISIBLE);

                        tl_comment_toggle.setVisibility(View.GONE);
                        tl_jieshou_toggle.setVisibility(View.GONE);
                        tl_new_conntent_toggle.setVisibility(View.GONE);
                    }else{
                        tv_not_kaqi_comment.setVisibility(View.GONE);
                        tv_not_jieshou_comment.setVisibility(View.GONE);
                        tv_not_new_content_push.setVisibility(View.GONE);

                        tl_comment_toggle.setVisibility(View.VISIBLE);
                        tl_jieshou_toggle.setVisibility(View.VISIBLE);
                        tl_new_conntent_toggle.setVisibility(View.VISIBLE);
                    }


                } else showToast(response.getErrorMessage());
            }

            @Override
            public boolean onFail(FFExtraParams extra) {

                showToast("你的网络不好哦");
                return false;
            }
        });
        }else{
            showToast("暂无网络~");
        }
    }


    /**
     * shang chuan   token he source
     */
//    private void  rptDeviceToken(){
//        FFExtraParams extra = new FFExtraParams();
//        extra.setDoCache(true);
//        extra.setSynchronized(false);
//        extra.setKeepWhenActivityFinished(false);
//        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/config/rptDeviceToken.do", "", extra, new FFNetWorkCallBack<BaseResult>() {
//            @Override
//            public void onSuccess(BaseResult response, FFExtraParams extra) {
//                if (response.judge()) {
//
//                } else showToast(response.getErrorMessage());
//            }
//
//            @Override
//            public boolean onFail(FFExtraParams extra) {
//                return false;
//            }
//        });
//    }


    private void  set(String  flag, String flag_value){
        if (FFUtils.checkNet()) {
            FFExtraParams extra = new FFExtraParams();
            extra.setDoCache(true);
            extra.setSynchronized(false);
            extra.setKeepWhenActivityFinished(false);
            extra.setProgressDialogcancelAble(true);
//            post(Constants.shareConstants().getNetHeaderAdress() + "/config/apnsPushSetConfig.do", "", extra, new FFNetWorkCallBack<SetAppPushSwitchResult>() {
            post(IUrlUtils.Search.apnsPushSetConfig, "", extra, new FFNetWorkCallBack<SetAppPushSwitchResult>() {
                @Override
                public void onSuccess(SetAppPushSwitchResult response, FFExtraParams extra) {
                    if (response.judge()) {
                        setkaiguan();
//                    tl_psuh_toggle_flag=response.isAppPush();
//                    tl_comment_toggle_flag=response.isReview();
//                    tl_jieshou_toggle_flag=response.isPGC();
//                    tl_new_conntent_toggle_flag=response.isAppNotice();
                        if ("isReview".equals(response.getIsType())) { //pinglun
                            if ("0".equals(response.getIsOpen())) {
                                tl_comment_toggle_flag = false;
                            } else {
                                tl_comment_toggle_flag = true;
                            }
                            refreshui(tl_comment_toggle_flag, tl_comment_toggle);


                        } else if ("isAppPush".equals(response.getIsType())) { //App tuisong

                            if ("0".equals(response.getIsOpen())) {
                                tl_psuh_toggle_flag = false;
                            } else {
                                tl_psuh_toggle_flag = true;
                            }
                            refreshui(tl_psuh_toggle_flag, tl_psuh_toggle);
                            if (!tl_psuh_toggle_flag) {
                                tv_not_kaqi_comment.setVisibility(View.VISIBLE);
                                tv_not_jieshou_comment.setVisibility(View.VISIBLE);
                                tv_not_new_content_push.setVisibility(View.VISIBLE);

                                tl_comment_toggle.setVisibility(View.GONE);
                                tl_jieshou_toggle.setVisibility(View.GONE);
                                tl_new_conntent_toggle.setVisibility(View.GONE);
                            } else {
                                tv_not_kaqi_comment.setVisibility(View.GONE);
                                tv_not_jieshou_comment.setVisibility(View.GONE);
                                tv_not_new_content_push.setVisibility(View.GONE);

                                tl_comment_toggle.setVisibility(View.VISIBLE);
                                tl_jieshou_toggle.setVisibility(View.VISIBLE);
                                tl_new_conntent_toggle.setVisibility(View.VISIBLE);
                            }

                        } else if ("isPGC".equals(response.getIsType())) { //jieshou app tuisong
                            if ("0".equals(response.getIsOpen())) {
                                tl_new_conntent_toggle_flag = false;
                            } else {
                                tl_new_conntent_toggle_flag = true;
                            }
                            refreshui(tl_new_conntent_toggle_flag, tl_new_conntent_toggle);

                        } else if ("isAppNotice".equals(response.getIsType())) {//meishi zhi
                            if ("0".equals(response.getIsOpen())) {
                                tl_jieshou_toggle_flag = false;
                            } else {
                                tl_jieshou_toggle_flag = true;
                            }
                            refreshui(tl_jieshou_toggle_flag, tl_jieshou_toggle);
                        }

                    } else showToast(response.getErrorMessage());
                }

                @Override
                public boolean onFail(FFExtraParams extra) {

                    return false;
                }
            }, flag, flag_value);
        }else{
            showToast(getString(R.string.not_net_state));
        }
    }

    protected void requestPermission(int requestCode) {
        // TODO Auto-generated method stub
        // 6.0以上系统才可以判断权限

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
            // 进入设置系统应用权限界面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
            return;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 运行系统在5.x环境使用
            // 进入设置系统应用权限界面
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
            return;
        }
        return;
    }

}
