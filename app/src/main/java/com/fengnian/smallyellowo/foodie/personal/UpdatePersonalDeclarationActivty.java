package com.fengnian.smallyellowo.foodie.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.publics.UserInfo;
import com.fengnian.smallyellowo.foodie.bean.results.NewSetNicknameResult;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

/**
 * Created by Administrator on 2016-9-3.
 */

public class UpdatePersonalDeclarationActivty extends BaseActivity<IntentData> {
    private EditText ed_declaration;
    private TextView tv_num;
    private UserInfo user;
    private int MAX_COUNT = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        user=(UserInfo)getIntent().getParcelableExtra("user");

        final String ii = getIntent().getStringExtra("user");
//        if(user==null) user=new UserInfo();
        setContentView(R.layout.activty_personaldeclaration);
        ed_declaration = findView(R.id.ed_declaration);
        tv_num = findView(R.id.tv_num);
        ed_declaration.addTextChangedListener(watcher);
        ed_declaration.setText(ii);
        ed_declaration.requestFocus();
        show();
        addMenu("保存", new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String text = ed_declaration.getText().toString().trim();
                if (text.equals(ii)) {
                    showToast("内容未变化");
                    return;
                } else {
                    updateNickpersonalitySignatureInfo(text);
                }

            }
        });

    }




    void show(){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
    private void updateNickpersonalitySignatureInfo(String personalitySignature) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/user/updateUserInfoNew.do", "", extra, new FFNetWorkCallBack<NewSetNicknameResult>() {
        post(IUrlUtils.Search.updateUserInfoNew, "", extra, new FFNetWorkCallBack<NewSetNicknameResult>() {
            @Override
            public void onSuccess(NewSetNicknameResult response, FFExtraParams extra) {
//                if (response.getErrorCode() == 0) {
//
//                    PersionInfo pinfo = response.getUser();
//                    SYUser info = SP.getUser();
//                    info.setPersonalDeclaration(pinfo.getPersonalitySignature());
//                    SP.setUser(info);// 将信息 转化为 json
//                    onBackPressed();
//                } else showToast("出现错误");
                if(response.judge()){
                    SYUser pinfo = response.getUser();
                    SYUser info = SP.getUser();
                    info.setPersonalDeclaration(pinfo.getPersonalDeclaration());
//                     String infosss= JSON.toJSON(info)+"";
                    SP.setUser(info);// 将信息 转化为 json
                    onBackPressed();
                    Intent intent = new Intent(MyActions.ACTION_UPDATE_USER_INFO);
                    LocalBroadcastManager.getInstance(UpdatePersonalDeclarationActivty.this).sendBroadcast(intent);
                }else{
                    showToast(response.getServerMsg());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "personalitySignature", personalitySignature,"type","0");
    }


    private TextWatcher watcher = new TextWatcher() {
        private int editStart;
        private int editEnd;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            editStart = ed_declaration.getSelectionStart();
            editEnd = ed_declaration.getSelectionEnd();
            ed_declaration.removeTextChangedListener(watcher);
            while (calculateLength(s.toString()) > MAX_COUNT) {
                s.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
            }
            ed_declaration.addTextChangedListener(watcher);
            setLeftCount();
        }
    };

    /**
     *
     */
    private void setLeftCount() {
        tv_num.setText((getInputCount()) + "");
    }

    /**
     * @return
     */
    private long getInputCount() {
        return MAX_COUNT - calculateLength(ed_declaration.getText().toString());
    }

    /**
     * @param c
     * @return
     */
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
}
