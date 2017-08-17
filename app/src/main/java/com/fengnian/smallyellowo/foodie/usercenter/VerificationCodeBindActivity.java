package com.fengnian.smallyellowo.foodie.usercenter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appbase.UPushHelper;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.BangDingPhoneResult;
import com.fengnian.smallyellowo.foodie.bean.results.CheckRegisterPhoneReslt;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.BindIntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.LoginIntentData;
import com.fengnian.smallyellowo.foodie.login.SetNicknameOneActivty;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.widgets.VerifyCodeView;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2017-4-10.
 * 第三方登录，需要绑定手机号，获取验证码
 */

public class VerificationCodeBindActivity extends BaseActivity<BindIntentData> implements View.OnClickListener {
    private ImageView iv_left;
    private VerifyCodeView coder_view;

    private TextView tv_time_count, tv_phone, tv_title;
    Drawable restart_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setNotitle(true);
        setContentView(R.layout.activity_register_one);

        iv_left = findView(R.id.iv_left);
        tv_title = findView(R.id.tv_title);
        tv_title.setText("短信验证");

        coder_view = findView(R.id.coder_view);
        coder_view = findView(R.id.coder_view);
        tv_phone = findView(R.id.tv_phone);
        tv_phone.setText("验证码将通过短信发送到您的手机：" + getIntentData().getPhone());
        tv_time_count = findView(R.id.tv_time_count);
        inin_click();
        restart_time = getResources().getDrawable(R.mipmap.restart_verfode_img);
        restart_time.setBounds(0, 0, restart_time.getMinimumWidth(), restart_time.getMinimumHeight());
        if (timeCount == null) {
            timeCount = new TimeCount(60000, 100);
        }
        timeCount.start();

    }

    void inin_click() {
        tv_time_count.setOnClickListener(this);
        tv_time_count.setInputType(InputType.TYPE_NULL); //保持键盘状态
        iv_left.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_time_count:
                check_phone(getIntentData().getPhone());
                break;
            case R.id.iv_left:
                dialog(VerificationCodeBindActivity.this);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dialog(VerificationCodeBindActivity.this);
            return false;
        }

        if (coder_view.getVerifyCodeStr().length() >= 6) {

//            validMessageCode();
            bangdingTel(getIntentData().getPhone(), coder_view.getVerifyCodeStr(), "");

        }
        return super.onKeyDown(keyCode, event);
    }

    private void check_phone(final String phone) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
        post(IUrlUtils.UserCenter.sendMessageNewV280, null, extra, new FFNetWorkCallBack<CheckRegisterPhoneReslt>() {
            @Override
            public void onSuccess(CheckRegisterPhoneReslt response, FFExtraParams extra) {
                if (response.judge()) {
                    if (timeCount == null) {
                        timeCount = new TimeCount(60000, 100);
                    }
                    timeCount.start();
                } else showToast(response.getServerMsg());
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "phoneNumber", phone, "type", "3");
    }

    TimeCount timeCount;

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            setRestart_time();
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            settime_count();
            tv_time_count.setClickable(false);//防止重复点击
            tv_time_count.setText(millisUntilFinished / 1000 + "秒后重新获取");

        }
    }


    void setRestart_time() {
        tv_time_count.setClickable(true);
        tv_time_count.setCompoundDrawables(restart_time, null, null, null);
        tv_time_count.setTextColor(getResources().getColor(R.color.login_press_one_text));
        tv_time_count.setText("重新发送");
    }

    void settime_count() {
        tv_time_count.setCompoundDrawables(null, null, null, null);
        tv_time_count.setTextColor(getResources().getColor(R.color.title_text_color));
    }

    private void bangdingTel(String phone, String code, final String password) {

        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
        post(IUrlUtils.Search.bindingPhoneNum, null, extra, new FFNetWorkCallBack<BangDingPhoneResult>() {
            @Override
            public void onSuccess(BangDingPhoneResult response, FFExtraParams extra) {
                if (response.judge()) {

                    SYUser us = response.getUser();
                    us.setToken(getIntentData().getToken());
                    SP.setUser(us);

                    if (getIntentData().getNickNameStatus() != 0) {
                        LoginIntentData data = new LoginIntentData();
                        data.setRequestCode(22);
                        data.setPhone(getIntentData().getPhone());
                        startActivity(SetNicknameOneActivty.class, data);
                    } else {

                        MobclickAgent.onEvent(VerificationCodeBindActivity.this, "new_register_user_count");//统计新注册用户个数
                        /*SYUser us = response.getUser();
                        us.setToken(getIntentData().getToken());
                        SP.setUser(us);*/
                        com.igexin.sdk.PushManager.getInstance().turnOnPush(getApplicationContext());
                        com.igexin.sdk.PushManager.getInstance().bindAlias(getApplicationContext(), SP.getUid());
                        showToast("登录成功！" + "欢迎回到小黄圈");//+ "\r\n"
                        UPushHelper.setPushAlias(VerificationCodeBindActivity.this, SP.getUid());
                    }

                    finish();
                } else {
                    showToast(response.getServerMsg());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {

                return false;
            }
        }, "phoneNumber", phone, "validateCode", code, "password", password, "account", getIntentData().getUserId(), "token", getIntentData().getToken());
    }

    public void dialog(Context context) {
        new EnsureDialog.Builder(context)
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
                finish();
            }

        }).show();//在按键响应事件中显示此对话框
    }

    /**
     * 检验验证码
     */
    /*void validMessageCode() {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
        post(IUrlUtils.Search.validMessageCodeNewV280, "", extra, new FFNetWorkCallBack<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response, FFExtraParams extra) {
                if (response.judge()) {

                    if (getIntentData().getNickNameStatus() != 0) {
                        LoginIntentData data = new LoginIntentData();
                        data.setRequestCode(22);
                        data.setPhone(getIntentData().getPhone());
                        startActivity(SetNicknameOneActivty.class, data);
                    } else {
                        com.igexin.sdk.PushManager.getInstance().turnOnPush(getApplicationContext());
                        com.igexin.sdk.PushManager.getInstance().bindAlias(getApplicationContext(), SP.getUid());
                        showToast("登录成功！" + "欢迎回到小黄圈");//+ "\r\n"
                        UPushHelper.setPushAlias(VerificationCodeBindActivity.this, SP.getUid());
                    }
                    finish();
                } else showToast(response.getServerMsg());
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "phoneNumber", getIntentData().getPhone(), "validateCode", coder_view.getVerifyCodeStr());
    }*/

     /*void login() {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
        post(IUrlUtils.Search.userLoginByShortMessage, "", extra, new FFNetWorkCallBack<LoginResult>() {
            @Override
            public void onSuccess(LoginResult response, FFExtraParams extra) {
                if (response.judge()) {
                    SYUser us = response.getUser().getUser();
                    SP.setToken(response.getUser().getToken());
                    SP.setUser(us);
                    SP.setLoginType("验证码");

                    HashMap<String, String> event = new HashMap<String, String>();
                    event.put("account", us.getId());
                    event.put("channel", Tool.getChannelName(VerificationCodeBindActivity.this));
                    event.put("platform", "验证码"); // 1:微信; 2:QQ; 3:微博; 4:手机号码；5：验证码登录
                    pushEventAction("Yellow_001", event);

                    if (TextUtils.isEmpty(us.getNickName())) {
                        LoginIntentData data = new LoginIntentData();
                        data.setRequestCode(22);
                        data.setPhone(getIntentData().getPhone());
                        startActivity(SetNicknameOneActivty.class, data);
                        finish();
                    } else {
                        com.igexin.sdk.PushManager.getInstance().turnOnPush(getApplicationContext());
                        com.igexin.sdk.PushManager.getInstance().bindAlias(getApplicationContext(), SP.getUid());
                        UPushHelper.setPushAlias(VerificationCodeBindActivity.this, SP.getUid());
                        finish();
                    }
                }
            }

            @Override
            public boolean onFailContext(LoginResult response, FFExtraParams extra) {
                coder_view.deleVerifyCode();
                return super.onFailContext(response, extra);
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "phoneNumber", getIntentData().getPhone(), "validateCode", coder_view.getVerifyCodeStr());
    }*/
}
