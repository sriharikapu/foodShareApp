package com.fengnian.smallyellowo.foodie.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.MainActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.NewSetNicknameResult;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.SetNickNameIntent;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import static com.fengnian.smallyellowo.foodie.personal.NicknameActivty.getStringLength;

/**
 * Created by Administrator on 2016-9-19.
 */

public class SetNicknameActivty extends BaseActivity<SetNickNameIntent> implements View.OnClickListener {

    private EditText ed_nickname;

    private TextView tv_set_nickname;
    SYUser info;
    private CharSequence temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (0 == getIntentData().getRequestCode()) {
            setBackVisible(false);
        }
        setContentView(R.layout.activty_setnickname);
        ed_nickname = findView(R.id.ed_set_nickname);
        ed_nickname.addTextChangedListener(watcher);
        tv_set_nickname = findView(R.id.tv_set_nickname);

        tv_set_nickname.setOnClickListener(this);
        tv_set_nickname.setClickable(false);
        info = SP.getUser();

       if(getIntentData().getNickNameStaus()!=0){
//           1 字数过长时（大于8个汉字或16个英文字符）
//           2 字数过短时（小于2个汉字或4个英文字符）
//           3 昵称重复
//           4 包含特殊符号
           if(getIntentData().getNickNameStaus()==3){
               showToast("昵称重复");
           }
          if (info!=null){
              String nickname=info.getNickName();
//              StringBuffer  str=new StringBuffer(info.getNickName());
              if(calculateLength(nickname.toString())>MAX_COUNT){
                  StringBuffer  str=new StringBuffer(nickname);
                  str.delete(MAX_COUNT,str.length());
                  ed_nickname.setText(str);

                  tv_set_nickname.setClickable(true);
                  tv_set_nickname.setTextColor(SetNicknameActivty.this.getResources().getColor(R.color.title_text_color));
                  tv_set_nickname.setBackgroundResource(R.drawable.login_selector);
              }else{
                ed_nickname.setText(info.getNickName());
              }

          }
       }
    }



    void  setTextHint(){

    }
    private int max_length = 1000;
    private static final int MAX_COUNT = 8;//汉字的数量    相当于 两倍的字符

    TextWatcher watcher = new TextWatcher() {
        private int editStart;
        private int editEnd;
        @Override
        public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {
            temp = c;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() > 0) {
                tv_set_nickname.setClickable(true);
                tv_set_nickname.setTextColor(SetNicknameActivty.this.getResources().getColor(R.color.title_text_color));
                tv_set_nickname.setBackgroundResource(R.drawable.login_selector);
            } else {
                tv_set_nickname.setClickable(false);
                tv_set_nickname.setTextColor(SetNicknameActivty.this.getResources().getColor(R.color.white_bg));
                tv_set_nickname.setBackgroundResource(R.drawable.logout_press);
            }
            editStart = ed_nickname.getSelectionStart();
            editEnd = ed_nickname.getSelectionEnd();
            ed_nickname.removeTextChangedListener(watcher);
            while (calculateLength(s.toString()) > MAX_COUNT) {
                s.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
            }
            ed_nickname.addTextChangedListener(watcher);
//            setLeftCount();
        }
    };
    private long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_set_nickname:
                String account = SP.getUid();
                String token = SP.getToken();

                String nickname = ed_nickname.getText().toString().trim();
                int lgth = getStringLength(nickname);

                if (lgth < 4) {
                    showToast("昵称最短为2个汉字或4个英文字符哦");
                    return;
                }
                set_nickname(account, token, nickname);
                break;
        }
    }

    private void set_nickname(String account, String token, final String nickname) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/user/updateUserInfoNew.do", "", extra, new FFNetWorkCallBack<NewSetNicknameResult>() {
        post(IUrlUtils.Search.updateUserInfoNew, "", extra, new FFNetWorkCallBack<NewSetNicknameResult>() {
            @Override
            public void onSuccess(NewSetNicknameResult response, FFExtraParams extra) {
//                if ("success".equals(response.getRestate())) {
//                    info.setNickName(nickname);
////                    String Info= JSON.toJSON(info)+"";
//
//                    SP.setUser(info);// 将信息 转化为 json
//
//                    Intent intent = new Intent(SetNicknameActivty.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                } else if ("fail".equals(response.getRestate())) {
//                    showToast(response.getMessage());
//                }
////                else {
                
////                    SetNicknameActivty.this.showErrorView(response.getMessage());
////                }
                if(response.judge()){
                    SYUser us=response.getUser();
                    SYUser  old=SP.getUser();
                    old.setNickName(us.getNickName());
                    SP.setUser(old);
//                    startFragmentActivity(MainActivity.class,new IntentData());
                    finish();
                }else{
                    showToast(response.getServerMsg());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "nickname", nickname, "type", "1");
    }

}
