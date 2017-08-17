package com.fengnian.smallyellowo.foodie.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

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

public class NicknameActivty extends BaseActivity<IntentData> implements View.OnClickListener {
    private EditText ed_nickname;
    private RelativeLayout iv_delete;
    private UserInfo user;
    String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nickname = getIntent().getStringExtra("nickname");
        setContentView(R.layout.activity_nickname);
        ed_nickname = findView(R.id.ed_nickname);
        iv_delete = findView(R.id.iv_delete);
        iv_delete.setOnClickListener(this);
        ed_nickname.addTextChangedListener(watcher);


        ed_nickname.setText(nickname);
        ed_nickname.requestFocus();
        show();
        if (nickname != null && nickname.length() > 0) {
            iv_delete.setVisibility(View.VISIBLE);
        }

        setEtWatcher(ed_nickname, 16);
        addMenu("保存", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickname = ed_nickname.getText().toString().trim();
                int lgth = getStringLength(nickname);

                if (lgth < 4) {
                    showToast("昵称最短为2个汉字或4个英文字符哦");
                    return;
                }
                updateNickNameInfo(nickname);
            }
        });
    }

    public static boolean isChinese(char c) {
        return new String(new char[]{c}).getBytes().length > 1;
//        int a = 0xffffffff & c;
//
//        return a >= 0x4e00 || a <= 0x9fa5;
    }


    public static int getStringLength(String str) {
        if (str == null) {
            return 0;
        }

        char[] arr = str.toCharArray();
        int maxLength = 0;
        for (char c : arr) {
            if (isChinese(c)) {
                maxLength += 2;
            } else {
                maxLength += 1;
            }
        }
        return maxLength;
    }

    public static void setEtWatcher(final EditText editText, final int maxLen) {
        editText.addTextChangedListener(new TextWatcher() {
            private int editStart;
            private int editEnd;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            private long calculateLength(CharSequence c) {
                double len = 0;
                for (int i = 0; i < c.length(); i++) {
                    String tmp = new String(new char[]{c.charAt(i)});
                    if (tmp.getBytes().length == 1) {
                        len += 0.5;
                    } else {
                        len++;
                    }
                }
                return Math.round(len);
            }
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editStart = editText.getSelectionStart();
                editEnd = editText.getSelectionEnd();
                while (calculateLength(s.toString()) > maxLen) {
                    s.delete(editStart - 1, editEnd);
                    editStart--;
                    editEnd--;
                }
            }
        });
    }


    void show() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private static String subString(String str, int length) {
        StringBuilder sb = new StringBuilder();
        int leng = 0;
        char[] arr = str.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            int l = leng + (new String(new char[]{arr[i]}).getBytes().length > 1 ? 2 : 1);
            if (l <= length) {
                sb.append(arr[i]);
                leng = l;
            } else {
                break;
            }
        }
        return sb.toString();
    }


    private void updateNickNameInfo(String nickname) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/user/updateUserInfoNew.do", "", extra, new FFNetWorkCallBack<NewSetNicknameResult>() {
        post(IUrlUtils.Search.updateUserInfoNew, "", extra, new FFNetWorkCallBack<NewSetNicknameResult>() {
            @Override
            public void onSuccess(NewSetNicknameResult response, FFExtraParams extra) {
//                if ("Err-0002".equals(response.getResult())) {
//                    showToast("昵称已被使用，请换一个试试看吧");
//                } else if ("Err-0005".equals(response.getResult())) {
//                    showToast("用户昵称不合法");
//                } else if (response.getErrorCode() == 0) {
//                    PersionInfo pinfo = response.getUser();
//                    SYUser info = SP.getUser();
//                    info.setNickName(pinfo.getNickname());
////                     String infosss= JSON.toJSON(info)+"";
//                    SP.setUser(info);// 将信息 转化为 json
//                    onBackPressed();
//                } else showToast(response.getErrorMessage());

                if(response.judge()){
                    SYUser pinfo = response.getUser();
                    SYUser info = SP.getUser();
                    info.setNickName(pinfo.getNickName());
//                     String infosss= JSON.toJSON(info)+"";
                    SP.setUser(info);// 将信息 转化为 json
                    onBackPressed();

                    Intent intent = new Intent(MyActions.ACTION_UPDATE_USER_INFO);
                    LocalBroadcastManager.getInstance(NicknameActivty.this).sendBroadcast(intent);
                }else{
                    showToast(response.getServerMsg());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "nickname", nickname,"type","0");
    }


    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {


        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() > 0) {
                iv_delete.setVisibility(View.VISIBLE);
            } else {
                iv_delete.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_delete:
                ed_nickname.setText("");
                break;
        }
    }
}
