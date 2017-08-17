package com.fengnian.smallyellowo.foodie.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SendVerificationCodeBean;
import com.fengnian.smallyellowo.foodie.bean.results.CheckRegisterPhoneReslt;
import com.fengnian.smallyellowo.foodie.bean.results.SendVerificationCodeResult;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016-9-20.
 */

public class FoundPassWordActivty extends BaseActivity<IntentData> implements View.OnClickListener{
  private EditText ed_phone,ed_verification_code;

    private TextView tv_send_verification_code,tv_next;
    public static   String PHONE_MATCH =  "^1[0-9]{10}$";// 手机号正则

    private ImageView iv_phone_delete,iv_code_delete;

    private int   prevent_repeated_clicks=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foundpassword);
        ed_phone= (EditText) findViewById(R.id.ed_phone);
        ed_phone.addTextChangedListener(phone_watcher);
        iv_phone_delete=findView(R.id.iv_phone_delete);
        iv_phone_delete.setOnClickListener(this);
        ed_verification_code= (EditText) findViewById(R.id.ed_verification_code);
        ed_verification_code.addTextChangedListener(code_watcher);
        iv_code_delete=findView(R.id.iv_code_delete);

        iv_code_delete.setOnClickListener(this);

        tv_send_verification_code= (TextView) findViewById(R.id.tv_send_verification_code);

        tv_send_verification_code.setOnClickListener(this);

        tv_next= (TextView) findViewById(R.id.tv_next);
        tv_next.setOnClickListener(this);
        tv_next.setClickable(false);
    }


    CharSequence phone_temp;
    TextWatcher  phone_watcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            phone_temp=charSequence;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            tv_send_verification_code.setClickable(true);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            int phone_length=ed_phone.getText().toString().trim().length();
            if(phone_length>0&&ed_verification_code.getText().toString().trim().length()>0){
                tv_next_click(true);
                iv_phone_delete.setVisibility(View.VISIBLE);
            }else if(phone_length>0&&ed_verification_code.getText().toString().trim().length()==0){
                tv_next_click(false);
                iv_phone_delete.setVisibility(View.VISIBLE);
            }
            else{
                tv_next_click(false);
                iv_phone_delete.setVisibility(View.GONE);
            }

        }
    };





    CharSequence temp;
    TextWatcher  code_watcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            temp=charSequence;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            tv_send_verification_code.setClickable(true);
        }

        @Override
        public void afterTextChanged(Editable editable) {
             int code_length=ed_verification_code.getText().toString().trim().length();
            if(code_length>0&&ed_phone.getText().toString().trim().length()>0){
                tv_next_click(true);
                iv_code_delete.setVisibility(View.VISIBLE);
            } else if(code_length>0&&ed_phone.getText().toString().trim().length()==0){
                tv_next_click(false);
                iv_code_delete.setVisibility(View.VISIBLE);
            }
            else{
                tv_next_click(false);
                iv_code_delete.setVisibility(View.GONE);
            }

        }
    };
    private  void   tv_next_click(boolean  flag){
        if(flag){
            tv_next.setClickable(true);
            tv_next.setBackgroundResource(R.drawable.login_selector);
            tv_next.setTextColor(FoundPassWordActivty.this.getResources().getColor(R.color.title_text_color));
        }else{
            tv_next.setClickable(false);
            tv_next.setTextColor(FoundPassWordActivty.this.getResources().getColor(R.color.white_bg));
            tv_next.setBackgroundResource(R.drawable.logout_press);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_send_verification_code:
                String phone=ed_phone.getText().toString().trim();
                if(phone.length()==0){
                    showToast("手机号不能为空");
                    return;
                }
                if (!Pattern.matches(PHONE_MATCH, phone)) {
                    showToast("手机号格式不正确");
                    return;
                }
                if(prevent_repeated_clicks==1){
                    prevent_repeated_clicks=2;
                    hideKey();
                    getpopwindinow(phone);
                }

                break;
            case R.id.iv_phone_delete:
                ed_phone.setText("");

                break;
            case R.id.iv_code_delete:
                ed_verification_code.setText("");

                break;
            case R.id.tv_next:
                String phone_number=ed_phone.getText().toString().trim();
                String code =ed_verification_code.getText().toString().trim();
                checkcode(phone_number,code);
                break;
        }
    }
    public void hideKey() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }
    private void getpopwindinow(final  String phone) {
        new EnsureDialog.Builder(FoundPassWordActivty.this)
//                        .setTitle("系统提示")//设置对话框标题

                .setMessage("发送验证码到: "+phone)//设置显示的内容

                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮

                    @Override

                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        check_phone(phone);
                    }

                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
            @Override

            public void onClick(DialogInterface dialog, int which) {
                // /响应事件
                prevent_repeated_clicks=1;
            }

        }).show();//在按键响应事件中显示此对话框
    }


    @Override
    public void onBackPressed(View v) {
        super.onBackPressed(v);
    }

    /**
     * 验证手机号
     */
    private  void  check_phone(final String phone){
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/shortMessage/sendMessageNew.do", null, extra, new FFNetWorkCallBack<CheckRegisterPhoneReslt>() {
        post(IUrlUtils.Search.sendMessageNew, null, extra, new FFNetWorkCallBack<CheckRegisterPhoneReslt>() {
            @Override
            public void onSuccess(CheckRegisterPhoneReslt response, FFExtraParams extra) {
                prevent_repeated_clicks=1;
                if(response.judge()){
                    PromptAlert();
                }else{
                    showToast(response.getServerMsg());
                }

            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                prevent_repeated_clicks=1;
                return false;
            }
        },"type","2","phoneNumber",phone);
    }

   //phoneNumber
    private  void  PromptAlert(){
                        FoundPassWordActivty.TimeCount timeCount=new  TimeCount(60000,100);
                        timeCount.start();
//                        SendVerificationCode(phone);
    }
    /**
     * 发送验证码
     * @param phone
     */

    String shortMessage_id;
    private void   SendVerificationCode(String phone){
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/sendMessage.do", null, extra, new FFNetWorkCallBack<SendVerificationCodeResult>() {
        post(IUrlUtils.Search.sendMessage, null, extra, new FFNetWorkCallBack<SendVerificationCodeResult>() {
            @Override
            public void onSuccess(SendVerificationCodeResult response, FFExtraParams extra) {
                if("success".equals(response.getRestate())){
                    SendVerificationCodeBean codeBean=response.getRedata().getShortMessage();
                    shortMessage_id=codeBean.getId();
                }else   showToast(response.getMessage());
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        },"account",phone,"phoneNumber",phone,"type","2");

    }


    private void checkcode(final String  phone, String code){
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/shortMessage/validMessageCodeNew.do", null, extra, new FFNetWorkCallBack<CheckRegisterPhoneReslt>() {
        post(IUrlUtils.Search.validMessageCodeNew, null, extra, new FFNetWorkCallBack<CheckRegisterPhoneReslt>() {
            @Override
            public void onSuccess(CheckRegisterPhoneReslt response, FFExtraParams extra) {
//                if("success".equals(response.getRestate())){
////                     startActivity(ResetPassWordActivty.class,new IntentData());
//
//                    Intent  intent=new Intent(FoundPassWordActivty.this,ResetPassWordActivty.class);
//                    intent.putExtra("phone",phone);
//                    startActivity(intent);
//                  }else   showToast(response.getMessage());

                if(response.getErrorCode()==0){
                    Intent intent=new Intent(FoundPassWordActivty.this,ResetPassWordActivty.class);
                    intent.putExtra("phone",phone);
                    startActivity(intent);
                }else showToast(response.getServerMsg());
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        },"phoneNumber",phone,"validateCode",code);//"id",shortMessage_id,
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            tv_send_verification_code.setText("获取验证码");
            tv_send_verification_code.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            tv_send_verification_code.setClickable(false);//防止重复点击
            tv_send_verification_code.setText(millisUntilFinished / 1000 + "秒后重新获取");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

