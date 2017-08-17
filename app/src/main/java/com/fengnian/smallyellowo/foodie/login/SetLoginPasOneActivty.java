package com.fengnian.smallyellowo.foodie.login;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.EnglishCharFilter;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appbase.UPushHelper;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.LoginResult;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.LoginIntentData;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

/**
 * Created by Administrator on 2017-4-11.
 */

public class SetLoginPasOneActivty extends BaseActivity<LoginIntentData> implements View.OnClickListener {
   private ImageView iv_left,iv_input_type;
    private EditText ed_password;
    private TextView tv_next_button;

    private boolean is_show_flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNotitle(true);
        setContentView(R.layout.activity_set_password);
        iv_left=findView(R.id.iv_left);
        ed_password=findView(R.id.ed_password);

         setHintTypefaceSize();

        tv_next_button=findView(R.id.tv_next_button);
        iv_input_type=findView(R.id.iv_input_type);
        initclick();
    }
    void  setHintTypefaceSize(){
        Spannable sigur_style=new SpannableString("输入密码 (6-16位特殊字符)");
        sigur_style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gray_bg_new)), 4, sigur_style.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        name_style.setSpan(new ForegroundColorSpan(yello),index,index+len,Spannable.SPAN_INCLUSIVE_INCLUSIVE );
        //设置字体大小（绝对值,单位：像素）
        sigur_style.setSpan(new AbsoluteSizeSpan(14,true),4, sigur_style.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ed_password.setHint(sigur_style);
    }
    void initclick(){
        iv_left.setOnClickListener(this);
        ed_password.setFilters(new InputFilter[]{new EnglishCharFilter(16)});
        ed_password.addTextChangedListener(pas_watcher);
        tv_next_button.setOnClickListener(this);
        iv_input_type.setOnClickListener(this);

    }

    TextWatcher  pas_watcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.length()>0){
                ed_password.setGravity(Gravity.CENTER);
            }
              if(s.length()>0){
                  setisClickable(true);
              }else{
                  setisClickable(false);
              }
        }
    };

    void    setisClickable(boolean falg){
        if(falg){
            tv_next_button.setClickable(true);
            tv_next_button.setBackgroundResource(R.drawable.login_press_one);
            tv_next_button.setTextColor(getResources().getColor(R.color.login_press_one_text));
        }else{
            tv_next_button.setClickable(false);

            tv_next_button.setBackgroundResource(R.drawable.login_normal_one);
            tv_next_button.setTextColor(getResources().getColor(R.color.vsersion_bg_line));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                  dialog(this);
                break;
            case R.id.tv_next_button:
                String phone=getIntentData().getPhone();
                String pas =ed_password.getText().toString().trim();
               if(pas.length()==0){
                   showToast("请设置密码");
                   return;
               }

//                if(!Config.isRightRegular(pas,PASS_Match)){
//                    showToast("请输入6-16位非特殊字符");
//                    return;
//                }
                if(FFUtils.LetterAndChinese(pas).length()>0){
                    showToast("请输入6-16位非特殊字符");
                    return;
                }
                if(pas.length()>0&&pas.length()<6){
                    showToast("密码长度不小于6位");
                    return;
                }
                if(pas.length()>16){
                    showToast("密码长度不大于16位");
                    return;
                }

                String  tem_pas=  LoginOneActivity.getMd5password(phone,pas);
                tv_next_button.setClickable(false);
                setpasword(tem_pas);
                break;
            case R.id.iv_input_type:
                if(is_show_flag){
                    ed_password.setTransformationMethod(PasswordTransformationMethod.getInstance()); //不可见
                    iv_input_type.setImageResource(R.mipmap.iv_set_password_type);
                    is_show_flag=false;
                }else{
                    is_show_flag=true;
                    ed_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());  //可见
                    iv_input_type.setImageResource(R.mipmap.iv_setpas_dianliang);
                }
                ed_password.setSelection(ed_password.getText().length());
                break;

        }
    }

    void  setpasword(String pas){
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/user/updatePassWordNew.do", "", extra, new FFNetWorkCallBack<LoginResult>() {
        post(IUrlUtils.Search.updatePassWordNew, "", extra, new FFNetWorkCallBack<LoginResult>() {
            @Override
            public void onSuccess(LoginResult response, FFExtraParams extra) {
                tv_next_button.setClickable(true);
                if (response.judge()) {
                    SYUser us = response.getUser().getUser();
                    us.setToken(response.getUser().getToken());
                    SP.setUser(us);
//                        startFragmentActivity(MainActivity.class, new IntentData());
                    com.igexin.sdk.PushManager.getInstance().turnOnPush(getApplicationContext());
                    com.igexin.sdk.PushManager.getInstance().bindAlias(getApplicationContext(), SP.getUid());
                    showToast("密码设置成功！" + "欢迎回到小黄圈");
                    UPushHelper.setPushAlias(SetLoginPasOneActivty.this, SP.getUid());
                     finish();
                } else {
                    showToast(response.getServerMsg());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                tv_next_button.setClickable(true);
                return false;
            }
        }, "phoneNumber", getIntentData().getPhone(), "password",pas);
    }

    public   void  dialog(Context context){
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            dialog(SetLoginPasOneActivty.this);
        }
        return false;
    }
}
