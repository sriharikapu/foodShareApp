package com.fengnian.smallyellowo.foodie.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.base.Test.StringUtils;
import com.fan.framework.config.FFConfig;
import com.fan.framework.config.Tool;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appbase.UPushHelper;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.appconfig.DevelopEnvironmentHelper;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.bean.results.LoginResult;
import com.fengnian.smallyellowo.foodie.bean.results.WeChatDataReult;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.BindIntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.LoginIntentData;
import com.fengnian.smallyellowo.foodie.receivers.PushManager;
import com.fengnian.smallyellowo.foodie.usercenter.BindPhoneActivity;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.MD5;
import com.fengnian.smallyellowo.foodie.utils.QQSdkUtil;
import com.fengnian.smallyellowo.foodie.wxapi.SinaOpen;
import com.fengnian.smallyellowo.foodie.wxapi.WeixinOpen;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.tauth.Tencent;

import java.util.HashMap;

/**
 * Created by Administrator on 2017-4-9.
 */

public class LoginOneActivity extends BaseActivity<LoginIntentData> implements View.OnClickListener {

    private DevelopEnvironmentHelper mDevelopEnvironmentHelper;
    private RelativeLayout rl_password;
    private EditText ed_phone, ed_password;

    private TextView tv_login_button, tv_right_code, tv_left_code;
    private ImageView iv_wechat, iv_left, iv_sina, iv_QQ;
    private QQSdkUtil QQSdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNotitle(true);
        getContainer().setBackgroundResource(0);
        setContentView(R.layout.activity_login_one);

        iv_left = findView(R.id.iv_left);
        iv_left.setOnClickListener(this);
        ed_phone = findView(R.id.ed_phone);
        rl_password = findView(R.id.rl_password);
        rl_password.setVisibility(View.GONE);
        ed_password = findView(R.id.ed_password);

        tv_login_button = findView(R.id.tv_login_button);
        tv_right_code = findView(R.id.tv_right_code);
        tv_right_code.setText("使用密码登录");
        tv_left_code = findView(R.id.tv_left_code);
        tv_left_code.setVisibility(View.GONE);
        iv_wechat = findView(R.id.iv_wechat);
        iv_sina = findView(R.id.iv_sina);
        iv_QQ = findView(R.id.iv_QQ);
        init_click();
        registerQQLoginReceiver();

    }

    @Override
    protected void onResume() {
        super.onResume();
        int install = WeixinOpen.getInstance().checkInstall();
        if (install == 0) {
            iv_wechat.setVisibility(View.GONE);
        } else {
            iv_wechat.setVisibility(View.VISIBLE);
        }

        /*SoftKeyBoardListener.setListener(this,new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                showToast("键盘显示 高度" + height);
            }

            @Override
            public void keyBoardHide(int height) {
                showToast("键盘隐藏 高度" + height);

            }
        });*/
    }

    int animation_end_flag = 10;//防止多次点击

    void init_click() {
        ed_phone.addTextChangedListener(phone_watcher);
        ed_password.addTextChangedListener(pas_watcher);
        tv_login_button.setOnClickListener(this);
        tv_login_button.setClickable(false);
        tv_right_code.setOnClickListener(this);
        tv_left_code.setOnClickListener(this);
        iv_wechat.setOnClickListener(this);
        iv_sina.setOnClickListener(this);
        iv_QQ.setOnClickListener(this);
        findViewById(R.id.develop).setOnClickListener(this);
    }

    private SsoHandler ssoHandler;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_left:
                dialog(LoginOneActivity.this);
                break;
            case R.id.tv_login_button:
                PushManager.onNewFrientClick();
                PushManager.onNoticeClick();
                PushManager.onDynamicClick();
                PushManager.onMessageClick();

                String phone = ed_phone.getText().toString().trim();
                if (phone.length() == 0) {
                    showToast("请输入手机号");
                    return;
                }
                if (phone.length() < 11) {
                    showToast("请将手机号码输入完整");
                    return;
                }

                if (rl_password.getVisibility() == View.VISIBLE) {
                    loginCode = "密码";
//                    pushEvent("THIRD_LOGIN_CLICK", loginCode);
                    String password = getMd5password(phone, ed_password.getText().toString().trim());
                    getLoginInfo(phone, password);
                } else { //验证码登录
                    loginCode = "验证码";
//                    pushEvent("THIRD_LOGIN_CLICK", loginCode);
                    use_verification_code_login(phone, 1000);
                }

                break;
            case R.id.tv_right_code:

                setIsCodeOr_pas();
                break;
            case R.id.tv_left_code:
                String phone1 = ed_phone.getText().toString().trim();
                if (phone1.length() < 11 && phone1.length() != 0) {
                    showToast("请将手机号码输入完整");
                    return;
                }
                if (phone1.length() == 0) {
                    showToast("请输入手机号");
                    return;
                }
                use_verification_code_login(phone1, 2000);
                break;
            case R.id.iv_wechat:
                loginCode = "微信";
//                pushEvent("THIRD_LOGIN_CLICK", loginCode);

                WeixinOpen.getInstance().AuthLgin();
                break;

            case R.id.iv_sina:
                loginCode = "新浪微博";
//                pushEvent("THIRD_LOGIN_CLICK", loginCode);

                AuthInfo info = new AuthInfo(this, SinaOpen.APP_KEY, SinaOpen.REDIRECT_URL, SinaOpen.SCOPE);
                ssoHandler = new SsoHandler(this, info);

                SinaOpen.loginBySina(this, ssoHandler);

                break;

            case R.id.iv_QQ: // qq登陆
                loginCode = "QQ";
//                pushEvent("THIRD_LOGIN_CLICK", loginCode);

                QQSdk = new QQSdkUtil(this);
                QQSdk.login();

                break;
            case R.id.develop:
                if (mDevelopEnvironmentHelper == null){
                    mDevelopEnvironmentHelper = new DevelopEnvironmentHelper(context());
                }
                mDevelopEnvironmentHelper.changeEnvironment();
                break;
        }

    }

    private String loginCode = "";

    private void getLoginInfo(String phone, final String password) {

        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
        post(IUrlUtils.UserCenter.userLoginNew, "", extra, new FFNetWorkCallBack<LoginResult>() {
            @Override
            public void onSuccess(LoginResult response, FFExtraParams extra) {
                if (response.judge()) {
                    SYUser us = response.getUser().getUser();
                    us.setToken(response.getUser().getToken());
                    SP.setUser(us);
                    SP.setLoginType(loginCode);

                    if (!TextUtils.isEmpty(loginCode)) {
                        HashMap<String, String> event = new HashMap<String, String>();
                        event.put("account", us.getId());
                        event.put("channel", Tool.getChannelName(LoginOneActivity.this));
                        event.put("platform", loginCode); // 1:微信; 2:QQ; 3:微博; 4:手机号码
                        pushEventAction("Yellow_001", event);
                    }

                    if (us.getNickName().length() == 0) {

                        LoginIntentData data = new LoginIntentData();
                        data.setRequestCode(10);
                        startActivity(SetNicknameOneActivty.class, data);

                        finish();
                    } else {
                        com.igexin.sdk.PushManager.getInstance().turnOnPush(getApplicationContext());
                        com.igexin.sdk.PushManager.getInstance().bindAlias(getApplicationContext(), SP.getUid());
                        showToast("登录成功！" + "欢迎回到小黄圈");
                        UPushHelper.setPushAlias(LoginOneActivity.this, SP.getUid());
                        finish();
                    }
                } else {

                    /*if (!TextUtils.isEmpty(loginCode)) {
                        pushEvent("THIRD_LOGIN_FAIR", loginCode);
                    }*/

                    showToast(response.getServerMsg());
                }
            }

            @Override
            public void onFailPublic(LoginResult response, FFExtraParams extraParams) {
                super.onFailPublic(response, extraParams);
                /*if (!TextUtils.isEmpty(loginCode)) {
                    pushEvent("THIRD_LOGIN_FAIR", loginCode);
                }*/
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                /*if (!TextUtils.isEmpty(loginCode)) {
                    pushEvent("THIRD_LOGIN_FAIR", loginCode);
                }*/
                return false;
            }
        }, "phoneNumber", phone, "password", password);
    }

    public static String getMd5password(String phone, String password) {

        try {
            String pas_sign = MD5.md5s(password);
            char[] pas_char = pas_sign.toCharArray();
            char[] phone_char = phone.toCharArray();
            StringBuffer str = new StringBuffer();
            for (int i = 0; i < pas_char.length; i++) {
                str.append(pas_char[i]);
                if (i < phone_char.length)
                    str.append(phone_char[i]);
            }
            String pas = MD5.md5s(str.toString());
            return pas;

        } catch (Exception e) {
            e.printStackTrace();
            return "-100";
        }
    }

    void setIsCodeOr_pas() {
        String phone = ed_phone.getText().toString().trim();
        if (rl_password.getVisibility() == View.VISIBLE) {
            if (phone.length() == 0) {
                showToast("请输入手机号");
                return;
            }

            String str = ed_phone.getText().toString().trim();
            if (str.length() > 0 && str.length() < 11) {
                showToast("请将手机号码输入完整");
                return;
            }
            if (phone.length() == 11) {
                use_verification_code_login(phone, 1000);
                return;
            }
            rl_password.setVisibility(View.GONE);
            tv_left_code.setVisibility(View.GONE);
            ed_password.setText("");
            tv_right_code.setText("使用密码登录");
            tv_login_button.setText("验证码登录");
            if (phone.length() > 0) {
                tv_login_button.setClickable(true);
                tv_login_button.setTextColor(getResources().getColor(R.color.login_press_one_text));
                tv_login_button.setBackgroundResource(R.drawable.login_press_one);
            }


        } else {
            if (phone.length() == 11) {
                ed_password.setFocusable(true);
                ed_password.requestFocus();
            }
            if (phone.length() > 0 && phone.length() < 11) {
                showToast("请将手机号码输入完整");
            }

            rl_password.setVisibility(View.VISIBLE);
            tv_left_code.setVisibility(View.VISIBLE);
            tv_right_code.setText("使用验证码登录");
            tv_login_button.setText("登录");
            if (ed_password.getText().toString().trim().length() == 0) {
                tv_login_button.setClickable(false);
                tv_login_button.setTextColor(getResources().getColor(R.color.vsersion_bg_line));
                tv_login_button.setBackgroundResource(R.drawable.login_normal_one);
            }
        }
    }

    TextWatcher phone_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String str_pas = ed_password.getText().toString().trim();

            if (rl_password.getVisibility() == View.GONE && s.length() > 0) {
                str_pas = "123";//一个不是空的数值
            }
            if (rl_password.getVisibility() == View.GONE) str_pas = "123";
            LoginButtonIsClick(s, str_pas);
        }
    };
    TextWatcher pas_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String str_pas = ed_phone.getText().toString().trim();
            LoginButtonIsClick(s, str_pas);
        }
    };


    void LoginButtonIsClick(Editable s, String str) {
        if (str.length() > 0 && s.length() > 0) {
            tv_login_button.setClickable(true);
            tv_login_button.setTextColor(getResources().getColor(R.color.login_press_one_text));
            tv_login_button.setBackgroundResource(R.drawable.login_press_one);
        } else {
            tv_login_button.setClickable(false);
            tv_login_button.setTextColor(getResources().getColor(R.color.vsersion_bg_line));
            tv_login_button.setBackgroundResource(R.drawable.login_normal_one);
        }
    }

    /**
     * 使用验证码进行登录
     */
    void use_verification_code_login(final String phone, final int type) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
        post(IUrlUtils.UserCenter.sendMessageNewV280, "", extra, new FFNetWorkCallBack<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response, FFExtraParams extra) {
                if (getDestroyed()) {
                    return;
                }
                if (response.getErrorCode() == 0) {
                    LoginIntentData intentData = new LoginIntentData();
                    intentData.setPhone(phone);
                    intentData.setType(type);
                    intentData.setRequestCode(2);
                    startActivity(VerificationCodeLoginActivity.class, intentData);
                    finish();
                } else showToast(response.getServerMsg());
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "phoneNumber", phone, "type", type == 1000 ? "5" : "2");
    }

    public static void startLogin(BaseActivity baseActivity) {
        LoginIntentData loginIntentData = new LoginIntentData();
        loginIntentData.setCode("");
        baseActivity.startActivity(LoginOneActivity.class, loginIntentData);
    }

    @Override
    public void finish() {
        super.finish();
        //关闭窗体动画显示
        this.overridePendingTransition(0, R.anim.switch_muban_activity_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (animation_end_flag == 10)
                dialog(LoginOneActivity.this);
        }
        return false;
    }

    private LoginReceiver loginReceiver;

    public void registerQQLoginReceiver() {
        loginReceiver = new LoginReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(IUrlUtils.Constans.LOAGIN_CODE_QQ);
        filter.addAction(IUrlUtils.Constans.LOAGIN_CODE_SINA);
        filter.addAction(IUrlUtils.Constans.LOAGIN_CODE_WECHAT);
        registerReceiver(loginReceiver, filter);
    }

    public class LoginReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (IUrlUtils.Constans.LOAGIN_CODE_WECHAT.equals(intent.getAction())) { // 微信登录回调
                String token = intent.getStringExtra(IUrlUtils.Constans.CALL_TOKEN);
                getUserInfo(IUrlUtils.Login.wechatLogin, "wechatCode", token);

            } else if (IUrlUtils.Constans.LOAGIN_CODE_QQ.equals(intent.getAction())) { // QQ登录回调
                /*int status = intent.getIntExtra(QQSdkUtil.NAME_STATUS, 0);

                if (TextUtils.isEmpty(SP.getUser().getTel())){
                    BindIntentData bind = new BindIntentData();
                    bind.setNickNameStatus(status);
                    startActivity(BindPhoneActivity.class, bind);
                    finish();
                    return;
                }

                loginSuccessed(status);*/

                String openId = intent.getStringExtra(QQSdkUtil.QQ_OPEN_ID);
                String token = intent.getStringExtra(QQSdkUtil.QQ_TOKEN);
                getUserInfo(IUrlUtils.Login.qqLogin, "openId", openId, "accessToken", token);

            } else if (IUrlUtils.Constans.LOAGIN_CODE_SINA.equals(intent.getAction())) { // 微博登录回调
                String accessToken = intent.getStringExtra(IUrlUtils.Constans.CALL_TOKEN);
                String refreshToken = intent.getStringExtra(IUrlUtils.Constans.CALL_REFRESH_TOKEN);
                e("微博登录 token = " + accessToken);
                getUserInfo(IUrlUtils.Login.weiboLogin, "accessToken", accessToken, "refreshToken", refreshToken);
            }
        }
    }

    private void loginSuccessed(int status) {
        if (status != 0) {
            LoginIntentData intentData = new LoginIntentData();
            intentData.setRequestCode(20);
            intentData.setNickNameStaus(status);
            startActivity(SetNicknameOneActivty.class, intentData);
        } else {
            com.igexin.sdk.PushManager.getInstance().turnOnPush(getApplicationContext());
            com.igexin.sdk.PushManager.getInstance().bindAlias(getApplicationContext(), SP.getUid());
            showToast("登录成功！" + "欢迎回到小黄圈");//+ "\r\n"
            UPushHelper.setPushAlias(LoginOneActivity.this, SP.getUid());
        }

        finish();
    }

    private void getUserInfo(String url, Object... params) {

        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
        post(url, "", extra, new FFNetWorkCallBack<WeChatDataReult>() {
            @Override
            public void onSuccess(WeChatDataReult response, FFExtraParams extra) {
                if (response.judge()) {
                    SYUser us = response.getUser().getUser();
                    us.setToken(response.getUser().getToken());
                    e("getUserInfo nickName = " + us.getNickName());

                    SP.setLoginType(loginCode);
                    if (!TextUtils.isEmpty(loginCode)) {
                        HashMap<String, String> event = new HashMap<String, String>();
                        event.put("account", us.getId());
                        event.put("channel", Tool.getChannelName(LoginOneActivity.this));
                        event.put("platform", loginCode); // 1:微信; 2:QQ; 3:微博; 4:手机号码; 5:验证码登录
                        pushEventAction("Yellow_001", event);
                    }

                    if (TextUtils.isEmpty(us.getTel())) {
                        BindIntentData bind = new BindIntentData();
                        bind.setNickNameStatus(response.getNickNameStaus());
                        bind.setUserId(us.getId());
                        bind.setToken(us.getToken());
                        startActivity(BindPhoneActivity.class, bind);
                        finish();
                        return;
                    }
                    SP.setUser(us);
                    loginSuccessed(response.getNickNameStaus());
                } else {

                   /* if (!TextUtils.isEmpty(loginCode)) {
                        pushEvent("THIRD_LOGIN_FAIR", loginCode);
                    }*/

                    showToast(response.getServerMsg());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }

            @Override
            public void onFailPublic(WeChatDataReult response, FFExtraParams extraParams) {
                super.onFailPublic(response, extraParams);
                /*if (!TextUtils.isEmpty(loginCode)) {
                    pushEvent("THIRD_LOGIN_FAIR", loginCode);
                }*/
            }
        }, params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (loginReceiver != null) {
            unregisterReceiver(loginReceiver);
        }

        if (QQSdk != null) {
            QQSdk.onDestory();
            QQSdk = null;
        }
    }

    public void dialog(Context context) {
        EnsureDialog.Builder builder = new EnsureDialog.Builder(context)
//                        .setTitle("系统提示")//设置对话框标题

                .setMessage("是否要放弃登录!")//设置显示的内容

                .setPositiveButton("继续登录", new DialogInterface.OnClickListener() {//添加确定按钮

                    @Override

                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                    }

                }).setNegativeButton("游客浏览", new DialogInterface.OnClickListener() {
                    //添加返回按钮

                    @Override

                    public void onClick(DialogInterface dialog, int which) {//响应事件
//                        finishAct();
                        finish();
                    }

                });
        EnsureDialog dialog = builder.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == com.tencent.connect.common.Constants.REQUEST_LOGIN && QQSdk != null) {
            Tencent.onActivityResultData(requestCode, resultCode, data, QQSdk.getIUiListener());
        }

        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }

    public void pushEvent(String key, String value) {
        HashMap<String, String> map = new HashMap<>();
        map.put(key, value);
        pushEventAction(key, map);
    }

}

