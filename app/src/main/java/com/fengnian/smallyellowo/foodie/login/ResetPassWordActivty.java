package com.fengnian.smallyellowo.foodie.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.SetNickNameIntent;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.MD5;

/**
 * Created by Administrator on 2016-9-20.
 */

public class ResetPassWordActivty extends BaseActivity<IntentData> implements View.OnClickListener{

    private EditText ed_password,ed_confire_password;
    private TextView tv_commit;

    String phone,  pas1;
    private ImageView iv_pas_delete,iv_confir_pas_delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.actvity_reset_password);
         phone=getIntent().getStringExtra("phone");
        ed_password= (EditText) findViewById(R.id.ed_password);
        ed_password.addTextChangedListener(pas_watcher);
        ed_confire_password= (EditText) findViewById(R.id.ed_confire_password);
        ed_confire_password.addTextChangedListener(watcher);
        tv_commit= (TextView) findViewById(R.id.tv_commit);
        tv_commit.setOnClickListener(this);
        tv_commit.setClickable(false);

        iv_pas_delete= (ImageView) findView(R.id.iv_pas_delete);

        iv_confir_pas_delete= (ImageView) findView(R.id.iv_confir_pas_delete);

        iv_pas_delete.setOnClickListener(this);
        iv_confir_pas_delete.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.iv_pas_delete:
                ed_password.setText("");
                break;

            case R.id.iv_confir_pas_delete:
                ed_confire_password.setText("");
                break;
            case R.id.tv_commit:
                pas1=ed_password.getText().toString().trim();
                String pas2=ed_confire_password.getText().toString().trim();
                int pas1_length=pas1.length();
                int pas2_length=pas2.length();
                if(pas1_length==0&&pas2_length==0){
                    showToast("重置密码不能为空");
                    return;
                }
              if (pas1_length<6&&pas2_length<6){
                  showToast("密码长度不能小于6位");
                  return;
              }
                if(!pas1.equals(pas2)){
                    showToast("两次输入密码不一致");
                    return;
                }
                String pas=getMd5password(phone,pas1);
                resetPassword(pas,phone);
                break;
        }
    }


    private void  resetPassword(String  pas,String phone){
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/user/updatePassWordNew.do", null, extra, new FFNetWorkCallBack<LoginResult>() {
        post(IUrlUtils.Search.updatePassWordNew, null, extra, new FFNetWorkCallBack<LoginResult>() {
            @Override
            public void onSuccess(LoginResult response, FFExtraParams extra) {
//                if("success".equals(response.getRestate())){
//                    UserInfo userInfo=response.getRedata().getAccount();
//                    userInfo.setPassWord(pas1);
////                    String info= JSON.toJSON(userInfo)+"";
//                    SP.setUser(userInfo);// 将信息 转化为 json
//                    if(userInfo.getNickname()==null||userInfo.getNickname().length()==0){
//                        startActivity(SetNicknameActivty.class,new IntentData());
//                    }else{
//                        startActivity(MainActivity.class,new IntentData());
//                        finish();
//                    }
//                }else   showToast(response.getMessage());

                if(response.judge()){
                    SYUser us= response.getUser().getUser();
                    us.setToken(response.getUser().getToken());
                    SP.setUser(us);
                    if(us.getNickName()==null||us.getNickName().length()==0){
                        startActivity(SetNicknameActivty.class,new SetNickNameIntent());
                    }else{
                        startActivity(MainActivity.class,new IntentData());
                        finish();
                    }
                }else{
                  showToast( response.getServerMsg());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        },"password",pas,"phoneNumber",phone);

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


    TextWatcher  pas_watcher=new TextWatcher() {
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

        }
    };



    CharSequence temp;
    TextWatcher watcher =new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            temp=charSequence;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.length()>0){
                iv_confir_pas_delete.setVisibility(View.VISIBLE);
            }else{
                iv_confir_pas_delete.setVisibility(View.GONE);
            }

            int pas_length=ed_password.getText().toString().trim().length();
            if(pas_length>0&&temp.length()>0){
                tv_next_click(true);
            }else{
                tv_next_click(false);
            }
        }
    };

    private  void   tv_next_click(boolean  flag){
        if(flag){
            tv_commit.setClickable(true);
            tv_commit.setBackgroundResource(R.drawable.login_selector);
            tv_commit.setTextColor(ResetPassWordActivty.this.getResources().getColor(R.color.title_text_color));
        }else{
            tv_commit.setClickable(false);
            tv_commit.setTextColor(ResetPassWordActivty.this.getResources().getColor(R.color.white_bg));
            tv_commit.setBackgroundResource(R.drawable.logout_press);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
