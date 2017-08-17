package com.fengnian.smallyellowo.foodie.login;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.MainActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.LoginResult;
import com.fengnian.smallyellowo.foodie.bean.results.WeChatDataReult;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.LoginIntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.RegisterIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.SetNickNameIntent;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.MD5;
import com.fengnian.smallyellowo.foodie.wxapi.WeixinOpen;

public class LoginActivity extends BaseActivity<LoginIntentData> implements View.OnClickListener {

    private EditText ed_phone, ed_password;
    private TextView tv_login, tv_forgot_password, tv_regist;

    private RelativeLayout iv_phone_delete, iv_pas_delete;

    private ImageView iv_wechat_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNotitle(false);
        setTitle("登录");
        setBackVisible(false);
//        code=getIntentData().getCode();
        setContentView(R.layout.activity_login);
        ed_phone = (EditText) findView(R.id.ed_phone);
        ed_phone.addTextChangedListener(phone_watcher);
        iv_phone_delete = findView(R.id.iv_phone_delete);
        iv_phone_delete.setOnClickListener(this);
        ed_password = (EditText) findView(R.id.ed_password);
        iv_pas_delete = findView(R.id.iv_pas_delete);
        iv_pas_delete.setOnClickListener(this);
        ed_password.addTextChangedListener(watcher);
        tv_login = (TextView) findView(R.id.tv_login);
        tv_login.setOnClickListener(this);
        tv_forgot_password = (TextView) findView(R.id.tv_forgot_password);
        tv_forgot_password.setOnClickListener(this);
        tv_regist = (TextView) findView(R.id.tv_regist);
        tv_regist.setOnClickListener(this);
        iv_wechat_login=findView(R.id.iv_wechat_login);
        iv_wechat_login.setOnClickListener(this);
        registerweChatLoginReceiver();
    }

    private TextWatcher phone_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (editable.length() > 0) {
                iv_phone_delete.setVisibility(View.VISIBLE);
            } else {
                iv_phone_delete.setVisibility(View.GONE);
            }

            if (ed_phone.length() > 0 && editable.length() > 0) {
                tv_login.setClickable(true);
                tv_login.setTextColor(LoginActivity.this.getResources().getColor(R.color.title_text_color));
                tv_login.setBackgroundResource(R.drawable.login_selector);
            } else {
                tv_login.setClickable(false);
                tv_login.setTextColor(LoginActivity.this.getResources().getColor(R.color.white_bg));
                tv_login.setBackgroundResource(R.drawable.logout_press);
            }
        }
    };
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() > 0) {
                iv_pas_delete.setVisibility(View.VISIBLE);
            } else {
                iv_pas_delete.setVisibility(View.GONE);
            }
            if (ed_phone.length() > 0 && editable.length() > 0) {
                tv_login.setClickable(true);
                tv_login.setTextColor(LoginActivity.this.getResources().getColor(R.color.title_text_color));
                tv_login.setBackgroundResource(R.drawable.login_selector);
            } else {
                tv_login.setClickable(false);
                tv_login.setTextColor(LoginActivity.this.getResources().getColor(R.color.white_bg));
                tv_login.setBackgroundResource(R.drawable.logout_press);
            }
        }
    };

    private void getLoginInfo(String phone, final String password) {

        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/userLoginNew.do", "", extra, new FFNetWorkCallBack<LoginResult>() {
        post(IUrlUtils.UserCenter.userLoginNew, "", extra, new FFNetWorkCallBack<LoginResult>() {
            @Override
            public void onSuccess(LoginResult response, FFExtraParams extra) {
                if (response.judge()) {
                    SYUser us = response.getUser().getUser();
                    us.setToken(response.getUser().getToken());
                    if(us.getNickName()==null)  us.setNickName("");
                    SP.setUser(us);
                     if (us.getNickName().length() == 0) {
//                        SetNickNameIntent data = new SetNickNameIntent();
//                        data.setRequestCode(0);
//                        startActivity(SetNicknameActivty.class, data);
                         LoginIntentData data=new LoginIntentData();
                         data.setRequestCode(100);
                         startActivity(SetNicknameOneActivty.class,data);

                    } else {
//                         startActivity(MainActivity.class, new IntentData());
                        finish();
                    }
                } else {
                    showToast(response.getServerMsg());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {

                return false;
            }
        }, "phoneNumber", phone, "password", password);
    }

    public String getMd5password(String phone, String password) {

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_phone_delete:
                ed_phone.setText("");
                break;
            case R.id.iv_pas_delete:
                ed_password.setText("");
                break;
            case R.id.tv_login:
                String phone = ed_phone.getText().toString().trim();
                if(phone.length()<11)  {
                    showToast("手机号输入错误");
                    return;
                }
                String password = getMd5password(phone, ed_password.getText().toString().trim());
                getLoginInfo(phone, password);
                break;
            case R.id.tv_forgot_password:
                startActivity(FoundPassWordActivty.class, new IntentData());
                break;
            case R.id.tv_regist:
                RegisterIntent in = new RegisterIntent();
                LoginIntentData intentData = getIntentData();
                if (intentData != null) {
                    String code = intentData.getCode();
                    if (code != null)
                        in.setCode(code);
                }
                startActivity(RegisterActvity.class, in);
                break;

            case R.id.iv_wechat_login:

                new EnsureDialog.Builder(LoginActivity.this)
//                        .setTitle("系统提示")//设置对话框标题

                        .setMessage("小黄圈想要打开微信")//设置显示的内容

                        .setPositiveButton("打开", new DialogInterface.OnClickListener() {//添加确定按钮

                            @Override

                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                                WeixinOpen.getInstance().AuthLgin();
                            }

                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override

                    public void onClick(DialogInterface dialog, int which) {//响应事件

                    }

                }).show();//在按键响应事件中显示此对话框
                break;
        }
    }



    WeChatLoginReceiver  weChatLoginReceiver;
    public void registerweChatLoginReceiver() {
        weChatLoginReceiver = new WeChatLoginReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(IUrlUtils.Constans.LOAGIN_CODE_WECHAT);
//        filter.addAction("autho_login");
        registerReceiver(weChatLoginReceiver, filter);
    }

    public class WeChatLoginReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            if ("autho_login".equals(intent.getAction())) {
            if (IUrlUtils.Constans.LOAGIN_CODE_WECHAT.equals(intent.getAction())) {
                String token=intent.getStringExtra("token");
                getWeChatUserInfo(token);

            }
        }
    }



    void      getWeChatUserInfo(String token){

        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/wechat/wechatLogin.do", "", extra, new FFNetWorkCallBack<WeChatDataReult>() {
        post(IUrlUtils.Login.wechatLogin, "", extra, new FFNetWorkCallBack<WeChatDataReult>() {
            @Override
            public void onSuccess(WeChatDataReult response, FFExtraParams extra) {
                if (response.judge()) {
                    SYUser us = response.getUser().getUser();
                    us.setToken(response.getUser().getToken());
                    SP.setUser(us);
                    if (response.getNickNameStaus() != 0) {
                        SetNickNameIntent data = new SetNickNameIntent();
                        data.setRequestCode(0);
                        data.setNickNameStaus(response.getNickNameStaus());
                        startActivity(SetNicknameActivty.class, data);
                        finish();
                    } else {
                        startActivity(MainActivity.class, new IntentData());
                        finish();
                    }
                } else {
                    showToast(response.getServerMsg());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        },"wechatCode", token);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(weChatLoginReceiver);
    }
}

