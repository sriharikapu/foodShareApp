package com.fengnian.smallyellowo.foodie.login;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.config.Tool;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFLogUtil;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.CommonWebviewUtilActivity;
import com.fengnian.smallyellowo.foodie.EnglishCharFilter;
import com.fengnian.smallyellowo.foodie.MainActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appbase.UPushHelper;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.Logindate;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.LoginIntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.WebInfo;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.HashMap;

import static com.fengnian.smallyellowo.foodie.appbase.SP.getUser;

/**
 * Created by Administrator on 2017-4-11.
 */

public class SetNicknameOneActivty extends BaseActivity<LoginIntentData> implements View.OnClickListener {

    private ImageView iv_left, iv_is_password_show;
    private TextView tv_next, tv_agreement, tv_edit_pas_invite_code, tv_3_2, tv_4_2, tv_5_2;
    private EditText ed_nickname, ed_password, ed_invite_code;

    private RelativeLayout rl_set_pasword, rl_invite_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setNotitle(true);
        setContentView(R.layout.activity_setnickname_and_pas);
        iv_left = findView(R.id.iv_left);
        tv_next = findView(R.id.tv_next);
        ed_nickname = findView(R.id.ed_nickname);
        ed_nickname.setFilters(new InputFilter[]{new EnglishCharFilter(16)});
        tv_agreement = findView(R.id.tv_agreement);
        tv_3_2 = findView(R.id.tv_3_2);
        tv_4_2 = findView(R.id.tv_4_2);
        tv_5_2 = findView(R.id.tv_5_2);
        tv_edit_pas_invite_code = findView(R.id.tv_edit_pas_invite_code);
        rl_invite_code = findView(R.id.rl_invite_code);

        rl_set_pasword = findView(R.id.rl_set_pasword);
        ed_password = findView(R.id.ed_password);
        iv_is_password_show = findView(R.id.iv_is_password_show);
        ed_invite_code = findView(R.id.ed_invite_code);

        if (TextUtils.isEmpty(getUser().getTel())) {
            tv_edit_pas_invite_code.setVisibility(View.GONE);
            rl_set_pasword.setVisibility(View.GONE);
            rl_invite_code.setVisibility(View.GONE);

        } else {
            tv_edit_pas_invite_code.setVisibility(View.VISIBLE);
        }
        initclick();
        iswe_chat_setnickname();
    }

    void iswe_chat_setnickname() {
        /*if (getIntentData().getNickNameStaus() == 0){
            return;
        }

        String nickname = getUser().getNickName();
        tv_next.setClickable(false);
        tv_next.setTextColor(getResources().getColor(R.color.gray_bg_new));
        switch (getIntentData().getNickNameStaus()) {
            case 1:
                showToast("昵称最长为4个汉字或16个英文字符哦");
                StringBuffer str = new StringBuffer(nickname);
                str.delete(MAX_COUNT, str.length());
                nickname = str.toString();
                break;

            case 2:
                showToast("昵称最短为2个汉字或4个英文字符哦");
                break;

            case 3:
                showToast("昵称已被占用，请重新换个吧");
                break;

            case 4:
                showToast("昵称包含特殊字符，请重新输入");
                break;

            default:
                tv_next.setClickable(true);
                tv_next.setTextColor(getResources().getColor(R.color.title_text_color));
                break;
        }

        ed_nickname.setText(nickname);
        if (!TextUtils.isEmpty(nickname)) {
            ed_nickname.setSelection(nickname.length());
        }*/

        int status = getIntentData().getNickNameStaus();
        String nickname = getUser().getNickName();
        FFLogUtil.e("setNickName", "status = " + status);
        if (status != 0) {
//           1 字数过长时（大于8个汉字或16个英文字符）
//           2 字数过短时（小于2个汉字或4个英文字符）
//           3 昵称重复
//           4 包含特殊符号
            if (status == 3) {
                showToast("昵称已被占用，请重新换个吧");
            } else if (status == 1) {
                showToast("昵称最长为8个汉字或16个英文字符哦");
                TextView name = new TextView(this);
                name.setFilters(new InputFilter[]{new EnglishCharFilter(16)});
                name.setText(nickname);
                /*StringBuffer str = new StringBuffer(nickname);
                str.delete(MAX_COUNT, str.length());*/
                nickname = name.getText().toString();
            } else if (status == 2) {
                showToast("昵称最短为2个汉字或4个英文字符哦");
                tv_next.setClickable(false);
                tv_next.setTextColor(getResources().getColor(R.color.gray_bg_new));
            } else if (status == 4) {
                showToast("昵称包含特殊字符，请重新输入");

            } else {
                tv_next.setClickable(true);
                tv_next.setTextColor(getResources().getColor(R.color.title_text_color));
            }

            ed_nickname.setText(nickname);
            ed_nickname.setSelection(ed_nickname.getText().length());

            getUser().setNickName("");
            SP.setUser(getUser());
        }
    }

    void initclick() {
        iv_left.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        ed_nickname.addTextChangedListener(nickname_watch);
        tv_agreement.setOnClickListener(this);
        tv_edit_pas_invite_code.setOnClickListener(this);
        ed_password.addTextChangedListener(pas_watch);
        iv_is_password_show.setOnClickListener(this);
        ed_invite_code.addTextChangedListener(invite_code);
    }

    TextWatcher invite_code = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (tv_5_2 != null){
                if (s.length() > 0) {
                    tv_5_2.setVisibility(View.INVISIBLE);
                } else {
                    tv_5_2.setVisibility(View.VISIBLE);
                }
            }
        }
    };
    TextWatcher pas_watch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (tv_4_2 != null){
                if (s.length() > 0) {
                    tv_4_2.setVisibility(View.INVISIBLE);
                } else {
                    tv_4_2.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    TextWatcher nickname_watch = new TextWatcher() {
        private int editStart;
        private int editEnd;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (tv_3_2 != null){
                if (s.length() > 0) {
                    tv_3_2.setVisibility(View.INVISIBLE);
                } else {
                    tv_3_2.setVisibility(View.VISIBLE);
                }
            }

            editStart = ed_nickname.getSelectionStart();
            editEnd = ed_nickname.getSelectionEnd();
            /*ed_nickname.removeTextChangedListener(nickname_watch);
            while (calculateLength(s.toString()) > MAX_COUNT) {
                s.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
            }*/

            if (calculateLength(s.toString()) >= 2 && calculateLength(s.toString()) <= 8) {
                tv_next.setClickable(true);
                tv_next.setTextColor(getResources().getColor(R.color.title_text_color));
            } else {
                tv_next.setClickable(false);
                tv_next.setTextColor(getResources().getColor(R.color.gray_bg_new));
            }

//            ed_nickname.addTextChangedListener(nickname_watch);
        }
    };

    private double calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            /*int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 0.5;
            } else {
                len++;
            }*/

            if (FFUtils.isChinese(c.charAt(i))) {
                len++;
            } else {
                len += 0.5;
            }
        }
        return Math.floor(len);
    }

    private static final int MAX_COUNT = 8;//汉字的数量    相当于 两倍的字符
    boolean is_show_flag = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                dialog(this);
                break;
            case R.id.tv_agreement:
                WebInfo info = new WebInfo();
                info.setTitle("用户协议");
                info.setUrl("http://w.fengnian.cn/Wallet/agreement.html");
                startActivity(CommonWebviewUtilActivity.class, info);
                break;
            case R.id.iv_is_password_show:
                if (is_show_flag) {
                    ed_password.setTransformationMethod(PasswordTransformationMethod.getInstance()); //不可见
                    iv_is_password_show.setImageResource(R.mipmap.iv_set_password_type);
                    is_show_flag = false;
                } else {
                    is_show_flag = true;
                    ed_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());  //可见
                    iv_is_password_show.setImageResource(R.mipmap.iv_setpas_dianliang);
                }
                ed_password.setSelection(ed_password.getText().length());
                break;
            case R.id.tv_edit_pas_invite_code:
                rl_set_pasword.setVisibility(View.VISIBLE);
                rl_invite_code.setVisibility(View.VISIBLE);
                tv_edit_pas_invite_code.setVisibility(View.GONE);
                break;
            case R.id.tv_next:
                String nick_name = ed_nickname.getText().toString().trim();
                String pas_word = ed_password.getText().toString().trim();
                String invite_code = ed_invite_code.getText().toString().trim();
                String temp_pas = LoginOneActivity.getMd5password(getIntentData().getPhone(), pas_word);

//                if(FFUtils.LetterAndChinese(nick_name).length()>0){
//                    showToast("请输入6-16位非特殊字符");
//                    return;
//                }

                if (!FFUtils.checkString(nick_name)) {
                    showToast("昵称包含特殊字符，请重新输入");
                    return;
                }

                if (!FFUtils.isStringEmpty(getUser().getTel()) && !FFUtils.isStringEmpty(FFUtils.LetterAndChinese(pas_word))) {
                    showToast("请输入4-16位非特殊字符");
                    return;
                }
                setfristNickname(nick_name, temp_pas, invite_code);
                tv_next.setClickable(false);
                break;
        }
    }

    void setfristNickname(String nickname, String password, String invitationCode) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/user/firstSetNickname.do", "", extra, new FFNetWorkCallBack<Logindate>() {
        post(IUrlUtils.Search.firstSetNickname, "", extra, new FFNetWorkCallBack<Logindate>() {
            @Override
            public void onSuccess(Logindate response, FFExtraParams extra) {
                tv_next.setClickable(true);
                if (response.judge()) {
                    SYUser us = response.getUser();
                    us.setToken(response.getToken());
                    SP.setUser(us);
                    com.igexin.sdk.PushManager.getInstance().turnOnPush(getApplicationContext());
                    com.igexin.sdk.PushManager.getInstance().bindAlias(getApplicationContext(), SP.getUid());
                    showToast("注册成功！" + "\r\n" + "欢迎来到小黄圈");
                    UPushHelper.setPushAlias(SetNicknameOneActivty.this, SP.getUid());

                    HashMap<String, String> event = new HashMap<String, String>();
                    event.put("account", us.getId());
                    event.put("channel", Tool.getChannelName(SetNicknameOneActivty.this));
                    event.put("platform", SP.getLoginType()); // 1:微信; 2:QQ; 3:微博; 4:手机号码; 5:验证码登录
                    pushEventAction("Yellow_002", event);

                    setResult(RESULT_OK);
                    finish();
                } else showToast(response.getServerMsg());
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                tv_next.setClickable(true);
                return false;
            }
        }, "nickname", nickname, "password", password, "invitationCode", invitationCode);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            dialog(SetNicknameOneActivty.this);
        }
        return false;
    }

    public void dialog(Context context) {

        new EnsureDialog.Builder(context)
//                        .setTitle("系统提示")//设置对话框标题

                .setMessage("填写昵称后才完成注册哦!")//设置显示的内容能

                .setPositiveButton("继续登录", new DialogInterface.OnClickListener() {//添加确定按钮

                    @Override

                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                    }

                }).setNegativeButton("游客浏览", new DialogInterface.OnClickListener() {
            //添加返回按钮

            @Override

            public void onClick(DialogInterface dialog, int which) {//响应事件
                SP.ClearUserSp();
                if (allActivis.size() > 1) {
                    setResult(RESULT_OK);
//                    finish();
                } else {
                    startActivity(MainActivity.class, new IntentData());
//                    finish();
                }
                finish();
            }

        }).show();//在按键响应事件中显示此对话框
    }
}
