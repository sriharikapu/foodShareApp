package com.fengnian.smallyellowo.foodie.personal;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.BangDingPhoneResult;
import com.fengnian.smallyellowo.foodie.bean.results.CheckRegisterPhoneReslt;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.MD5;
import com.umeng.analytics.MobclickAgent;

import java.util.regex.Pattern;


/**
 * Created by Administrator on 2017-3-23.
 */

public class BangDingTelNextActivty extends BaseActivity<IntentData> implements View.OnClickListener{
    private EditText ed_phone,ed_verification_code,ed_password,ed_invite_code;
    private TextView tv_send_verification_code,tv_agreement,tv_regist_next;

    public static  String PHONE_MATCH = "^1[0-9]{10}$";// 手机号正则

    private ImageView iv_phone_delete,iv_code_delete,iv_pas_delete,iv_invite_delete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bangding_tel_nex);

        ed_phone=(EditText) findViewById(R.id.ed_phone);
        ed_phone.addTextChangedListener(phone_watcher);


        addMenu("", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ed_verification_code=(EditText) findViewById(R.id.ed_verification_code);
        ed_verification_code.addTextChangedListener(verification_code_watcher);
        tv_send_verification_code=(TextView) findViewById(R.id.tv_send_verification_code);
        tv_send_verification_code.setOnClickListener(this);

        ed_password=(EditText) findViewById(R.id.ed_password);
        ed_password.addTextChangedListener(password_watcher);

        iv_phone_delete=  findView(R.id.iv_phone_delete);
        iv_code_delete= findView(R.id.iv_code_delete);
        iv_pas_delete=  findView(R.id.iv_pas_delete);
//        iv_invite_delete= findView(R.id.iv_invite_delete);

        iv_phone_delete.setOnClickListener(this);
        iv_code_delete.setOnClickListener(this);
        iv_pas_delete.setOnClickListener(this);
//        iv_invite_delete.setOnClickListener(this);


        tv_regist_next= findView(R.id.tv_regist_next);

        tv_regist_next.setOnClickListener(this);
    }


    TextWatcher phone_watcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.length()>0){
                iv_phone_delete.setVisibility(View.VISIBLE);
            }else{
                iv_phone_delete.setVisibility(View.GONE);
            }

            if(JudgeLength())
                register_button_click(true);
            else register_button_click(false);


        }
    };

    private  void   register_button_click(boolean  flag){
        if(flag){
            tv_regist_next.setClickable(true);
            tv_regist_next.setBackgroundResource(R.drawable.login_selector);
            tv_regist_next.setTextColor(BangDingTelNextActivty.this.getResources().getColor(R.color.title_text_color));
        }else{
            tv_regist_next.setClickable(false);
            tv_regist_next.setTextColor(BangDingTelNextActivty.this.getResources().getColor(R.color.white_bg));
            tv_regist_next.setBackgroundResource(R.drawable.logout_press);
        }
    }

    TextWatcher verification_code_watcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.length()>0){
                iv_code_delete.setVisibility(View.VISIBLE);
            }else{
                iv_code_delete.setVisibility(View.GONE);
            }

            if( JudgeLength())
                register_button_click(true);
            else register_button_click(false);
        }
    };

    private  boolean  JudgeLength(){
        String  phone=ed_phone.getText().toString().trim();
        String   code=ed_verification_code.getText().toString().trim();
        String  password=ed_password.getText().toString().trim();
        if(phone.length()>0&&code.length()>0&&password.length()>0)  {
            return  true;
        }
        return  false;


    }

    TextWatcher password_watcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            if(editable.length()>0){
                iv_pas_delete.setVisibility(View.VISIBLE);
            }else{
                iv_pas_delete.setVisibility(View.GONE);
            }

            if(JudgeLength())
                register_button_click(true);
            else register_button_click(false);
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_phone_delete:
                ed_phone.setText("");

                break;
            case R.id.iv_code_delete:

                ed_verification_code.setText("");
                break;
            case R.id.iv_pas_delete:

                ed_password.setText("");
                break;
            case R.id.tv_send_verification_code:
                FFUtils.setSoftInputInvis(v.getWindowToken());
                String phone=ed_phone.getText().toString().trim();
                if(phone.length()==0){
                    showToast("手机号不能为空");
                    return;
                }
                if (!Pattern.matches(PHONE_MATCH, phone)) {
                    showToast("手机号格式不正确");
                    return;
                }

                getpopwindinow(phone);


                break;
            case R.id.tv_regist_next:
                String phon=ed_phone.getText().toString().trim();
                int  password_length=ed_password.getText().toString().trim().length();
                if (!Pattern.matches(PHONE_MATCH, phon)) {
                    showToast("手机号格式不正确");
                    return;
                }else if(password_length<6){
                    //验证密码zhishao是6位
                    if(password_length==0) showToast("密码不能为空");
                    else showToast("密码不少于6位");
                    return;
                }else{ //提交绑定手机号
                    String  code  =ed_verification_code.getText().toString().trim();
                    String word=ed_password.getText().toString().trim();
                    String password=getMd5password(phon,word);
                    bangdingTel(phon,code,password);
                }

                break;
        }
    }

    private void getpopwindinow(final  String phone) {
        new EnsureDialog.Builder(BangDingTelNextActivty.this)
//                        .setTitle("系统提示")//设置对话框标题

                .setMessage("发送验证码到: "+phone)//设置显示的内容

                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        check_phone(phone);

                    }

                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
            @Override

            public void onClick(DialogInterface dialog, int which) {//响应事件

            }

        }).show();//在按键响应事件中显示此对话框
    }


    /**
     * 验证手机号  跟发送验证码   已经合并
     */
    TimeCount timeCount;
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
                if(response.judge()){
                    if(timeCount==null){
                        timeCount=new TimeCount(60000,100);
                    }
                    timeCount.start();
                } else   showToast(response.getServerMsg());
            }
            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        },"phoneNumber",phone,"type","3");
    }

    private  void  bangdingTel(String phone ,String  code,final String  password){

        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/user/bindingPhoneNum.do", null, extra, new FFNetWorkCallBack<BangDingPhoneResult>() {
        post(IUrlUtils.Search.bindingPhoneNum, null, extra, new FFNetWorkCallBack<BangDingPhoneResult>() {
            @Override
            public void onSuccess(BangDingPhoneResult response, FFExtraParams extra) {
                if(response.judge()){
                    MobclickAgent.onEvent(BangDingTelNextActivty.this, "new_register_user_count");//统计新注册用户个数
                    SYUser us=response.getUser();
                    SYUser tempuser=SP.getUser();
                    tempuser.setTel(us.getTel());
                    SP.setUser(tempuser);
                    finish();
                }else{
                    showToast(response.getServerMsg());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {

                return false;
            }
        },"phoneNumber",phone,"validateCode",code,"password",password);
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

    public  String getMd5password( String phone ,String password) {

        try {
            String pas_sign = MD5.md5s(password);
            char[] pas_char=pas_sign.toCharArray();
            char[] phone_char= phone.toCharArray();
            StringBuffer  str=new StringBuffer();
            for(int i=0;i<pas_char.length;i++){
                str.append(pas_char[i]);
                if(i<phone_char.length)
                    str.append(phone_char[i]);
            }
            String pas=MD5.md5s(str.toString());
            return pas;

        } catch (Exception e) {
            e.printStackTrace();
            return "-100";
        }

    }
    }
