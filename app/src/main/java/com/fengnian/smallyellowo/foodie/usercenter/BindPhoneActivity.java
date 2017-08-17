package com.fengnian.smallyellowo.foodie.usercenter;

import android.content.Context;
import android.content.DialogInterface;
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

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.BindIntentData;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by elaine on 2017/7/24.
 * 使用第三方账号登录时，需要强制绑定手机号
 */

public class BindPhoneActivity extends BaseActivity<BindIntentData> implements View.OnClickListener {
    public static String PHONE_MATCH = "^1[0-9]{10}$";// 手机号正则

    @Bind(R.id.input_number)
    EditText inputNumber;
    @Bind(R.id.tv_next)
    TextView tvNext;
    @Bind(R.id.iv_left)
    ImageView ivLeft;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.title_layout)
    RelativeLayout titleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNotitle(true);
        setContentView(R.layout.activity_bind_phone);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {

        tvNext.setClickable(false);
        ivLeft.setOnClickListener(this);
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phon = inputNumber.getText().toString().trim();

                if (!Pattern.matches(PHONE_MATCH, phon)) {
                    showToast("手机号格式不正确");
                    return;
                }

                use_verification_code_login(phon, 2000);
            }
        });

        inputNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s) || s.length() < 11) {
                    tvNext.setClickable(false);
                    tvNext.setTextColor(getResources().getColor(R.color.vsersion_bg_line));
                    tvNext.setBackgroundResource(R.drawable.login_normal_one);
                } else {
                    tvNext.setClickable(true);
                    tvNext.setTextColor(getResources().getColor(R.color.login_press_one_text));
                    tvNext.setBackgroundResource(R.drawable.login_press_one);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_left:
                dialog(BindPhoneActivity.this);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dialog(BindPhoneActivity.this);
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void use_verification_code_login(final String phone, final int type) {
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
                    BindIntentData intentData = new BindIntentData();
                    intentData.setNickNameStatus(getIntentData().getNickNameStatus());
                    intentData.setPhone(phone);
                    intentData.setUserId(getIntentData().getUserId());
                    intentData.setToken(getIntentData().getToken());
                    startActivity(VerificationCodeBindActivity.class, intentData);
                    finish();
                } else showToast(response.getServerMsg());
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "phoneNumber", phone, "type", "3");
    }

    private void dialog(Context context) {
        new EnsureDialog.Builder(context)

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
}
