package com.fengnian.smallyellowo.foodie.personal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.http.FFBaseBean;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.DynamicMessagesActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.SuggestActivity;
import com.fengnian.smallyellowo.foodie.appbase.APP;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.ActivityTags;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.appconfig.DevelopEnvironmentDialog;
import com.fengnian.smallyellowo.foodie.appconfig.DevelopEnvironmentHelper;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.BangDingPhoneResult;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.bean.results.InvitationAuthority;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.scoreshop.AddressManager;
import com.fengnian.smallyellowo.foodie.scoreshop.SYAddressListModel;
import com.fengnian.smallyellowo.foodie.scoreshop.SkuDetailActivity;
import com.fengnian.smallyellowo.foodie.usercenter.BindAccountActivity;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.wxapi.WeixinOpen;

import java.io.File;

/**
 * Created by Administrator on 2016-9-1.
 */

public class SettingActivity extends BaseActivity<IntentData> implements View.OnClickListener {
    private RelativeLayout rl_invite_friend, rl_problem_feedback, rl_tongyong;
    private RelativeLayout rl_about, rl_messagepush, rl_tel, rl_wechat, rl_address, rl_message_list;
    private TextView tv_logout, tv_tel_is_bangding;
    private TextView tv_address_empty;
    private DevelopEnvironmentHelper mDevelopEnvironmentHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        addChildTag(ActivityTags.setting);
        rl_invite_friend = findView(R.id.rl_invite_friend);
        rl_problem_feedback = findView(R.id.rl_problem_feedback);
        rl_tongyong = findView(R.id.rl_tongyong);
        rl_messagepush = findView(R.id.rl_messagepush);
        rl_message_list = findView(R.id.rl_message_list);
        rl_tel = findView(R.id.rl_tel);
        rl_tel.setOnClickListener(this);
        tv_tel_is_bangding = findView(R.id.tv_tel_is_bangding);
        rl_wechat = findView(R.id.rl_wechat);
        rl_wechat.setOnClickListener(this);
        rl_about = findView(R.id.rl_about);
        tv_logout = findView(R.id.tv_logout);
        rl_address = findView(R.id.rl_address);
        tv_address_empty = findView(R.id.tv_address_empty);

        rl_invite_friend.setOnClickListener(this);
        rl_invite_friend.setVisibility(View.GONE);
        rl_problem_feedback.setOnClickListener(this);
        rl_tongyong.setOnClickListener(this);
        rl_messagepush.setOnClickListener(this);
        rl_message_list.setOnClickListener(this);
        rl_about.setOnClickListener(this);
        tv_logout.setOnClickListener(this);
        findViewById(R.id.develop).setOnClickListener(this);
        rl_address.setOnClickListener(this);
        tv_address_empty.setVisibility(View.GONE);

        if (TextUtils.isEmpty(SP.getUser().getTel())){
            findViewById(R.id.no_phone).setVisibility(View.VISIBLE);
        }

        getInvitationAuthority();
        getAddress();
        registerweChatLoginReceiver();
    }

    SYUser us;

    @Override
    protected void onResume() {
        super.onResume();
        us = SP.getUser();
        if (us.getTel() == null) us.setTel("");
        if (us.getTel().length() == 0) {
            tv_tel_is_bangding.setText("未绑定");
        } else {

            String tel = us.getTel();
            changPhoneUi(tel, tv_tel_is_bangding);
//            tv_tel_is_bangding.setText(us.getTel());
        }
/*        if (us.isBindWechat()) {
            tv_wechat_is_bangding.setText("已绑定");
        } else {
            tv_wechat_is_bangding.setText("未绑定");
        }

        if (!us.isBindWechat()) {

            rl_wechat.setClickable(true);
            //// TODO  进行绑定微信逻辑处理
        } else {
            rl_wechat.setClickable(false);
        }*/

        if (us.getTel().length() == 0) {
            rl_tel.setClickable(true);
        } else {
            rl_tel.setClickable(false);
        }
    }

    void changPhoneUi(String phone, TextView textView) {
        StringBuffer str = new StringBuffer(phone);
//        str.replace(3, 7, "****");
        textView.setText(str);
    }

    private void getInvitationAuthority() {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/invitation/getInvitationAuthority.do", "", extra, new FFNetWorkCallBack<InvitationAuthority>() {
        post(IUrlUtils.Search.getInvitationAuthority, null, extra, new FFNetWorkCallBack<InvitationAuthority>() {
            @Override
            public void onSuccess(InvitationAuthority response, FFExtraParams extra) {
                if (response.getErrorCode() == 0) {
                    if (response.isInviteFriendsAuthority())
                        rl_invite_friend.setVisibility(View.VISIBLE);
                    else {
                        rl_invite_friend.setVisibility(View.GONE);
                    }
                } else showToast(response.getErrorMessage());
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_invite_friend:
                startActivity(InviteFriendActivity.class, new IntentData());

                break;
            case R.id.rl_problem_feedback:
                startActivity(SuggestActivity.class, new IntentData());
                break;
            case R.id.rl_tongyong:
                startActivity(TongYongActivity.class, new IntentData());
                break;

            case R.id.rl_messagepush: //消息推送
                startActivity(PushMessageActivity.class, new IntentData());
                break;

            case R.id.rl_message_list: //消息列表
                if(!FFUtils.checkNet()){
                    APP.app.showToast("网络连接失败，请检查网络设置",null);
                    return;
                }
                startActivity(DynamicMessagesActivity.class, new IntentData());
                break;
            case R.id.rl_about:
                startActivity(VersionUpdateActivity.class, new IntentData());
                break;
            case R.id.tv_logout:
                new EnsureDialog.Builder(SettingActivity.this)
//                        .setTitle("系统提示")//设置对话框标题

                        .setMessage("确定要退出当前账号!")//设置显示的内容

                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮

                            @Override

                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                logout();
                            }

                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override

                    public void onClick(DialogInterface dialog, int which) {//响应事件

                    }

                }).show();//在按键响应事件中显示此对话框
                break;

            case R.id.rl_tel:
                startActivity(BangDingTelActivty.class, new IntentData());
                break;
            case R.id.rl_wechat:
//                test();
//                WeixinOpen.getInstance().AuthLgin();
                startActivity(BindAccountActivity.class, new IntentData());
                break;
            case R.id.develop:
                if (mDevelopEnvironmentHelper == null){
                    mDevelopEnvironmentHelper = new DevelopEnvironmentHelper(context());
                }
                mDevelopEnvironmentHelper.changeEnvironment();
                break;
            case R.id.rl_address:
                startActivity(AddressEditActivity.class, new IntentData());
                break;
        }
    }

    private void getAddress() {
        AddressManager.getInstance().getAddress(this,false, new FFNetWorkCallBack<SYAddressListModel>() {
            @Override
            public void onSuccess(SYAddressListModel response, FFExtraParams extra) {
                if (isFinishing()) {
                    return;
                }
                if (response != null) {
                    if (SkuDetailActivity.hasAddress(response)) {
                        tv_address_empty.setVisibility(View.GONE);
                    } else {
                        tv_address_empty.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        });
    }

    public class WeChatLoginReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (MyActions.ACTION_UPDATE_ADDRESS.equals(intent.getAction())) {
                boolean isHasAddress = intent.getBooleanExtra("isHasAddress", false);
                if (isHasAddress) {
                    tv_address_empty.setVisibility(View.GONE);
                } else {
                    tv_address_empty.setVisibility(View.VISIBLE);
                }
            }

            /*if ("autho_login".equals(intent.getAction())) {
                String token = intent.getStringExtra("token");
                BangDingWeiXin(token);
            } else if (MyActions.ACTION_UPDATE_ADDRESS.equals(intent.getAction())) {
                boolean isHasAddress = intent.getBooleanExtra("isHasAddress", false);
                if (isHasAddress) {
                    tv_address_empty.setVisibility(View.GONE);
                } else {
                    tv_address_empty.setVisibility(View.VISIBLE);
                }
            }*/
        }
    }

    WeChatLoginReceiver weChatLoginReceiver;

    public void registerweChatLoginReceiver() {
        weChatLoginReceiver = new WeChatLoginReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction("autho_login");
        filter.addAction(MyActions.ACTION_UPDATE_ADDRESS);
        registerReceiver(weChatLoginReceiver, filter);
    }

    /**
     * 绑定微信
     */
    /*void BangDingWeiXin(String wechatCode) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
        post(Constants.shareConstants().getNetHeaderAdress() + "/user/bindingWeChat.do", null, extra, new FFNetWorkCallBack<BangDingPhoneResult>() {
            @Override
            public void onSuccess(BangDingPhoneResult response, FFExtraParams extra) {
                if (response.judge()) {
                    SYUser us = response.getUser();
                    SP.setUser(us);
                    showToast("已成功绑定");
                    if (us.isBindWechat()) {
                        tv_wechat_is_bangding.setText("已绑定");
                    }
                } else {
                    showToast(response.getServerMsg());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        });
    }*/


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (weChatLoginReceiver != null) {
            unregisterReceiver(weChatLoginReceiver);
        }
    }
}
