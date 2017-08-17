package com.fengnian.smallyellowo.foodie.shopcommiterror;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.intentdatas.ShopErrorInfoIntent;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;


/**
 * Created by Administrator on 2017-3-29.
 */

public class ShopBanQianAdressActivtyi extends BaseActivity<ShopErrorInfoIntent>implements View.OnClickListener{
    private EditText ed_nickname;

    private TextView tv_set_nickname;
    private CharSequence temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopbanqian_adress);
        getMenuContainer().removeAllViews();
        ed_nickname = findView(R.id.ed_set_nickname);
        ed_nickname.addTextChangedListener(watcher);
        tv_set_nickname = findView(R.id.tv_set_nickname);

        tv_set_nickname.setOnClickListener(this);
        tv_set_nickname.setClickable(false);
    }

    private int max_length = 1000;
    private static final int MAX_COUNT = 100;//汉字的数量    相当于 两倍的字符

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
                tv_set_nickname.setTextColor(ShopBanQianAdressActivtyi.this.getResources().getColor(R.color.title_text_color));
                tv_set_nickname.setBackgroundResource(R.drawable.login_selector);
            } else {
                tv_set_nickname.setClickable(false);
//                tv_set_nickname.setTextColor(ShopBanQianAdressActivtyi.this.getResources().getColor(R.color.white_bg));
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
                String address = ed_nickname.getText().toString().trim();
                ShopErrorInfoIntent  info=getIntentData();
                commitbusinesserror_info(info.getShopid(),info.getError_type()+"",info.getError_type_childe(),address);
                break;
        }
    }



    /**
     * 提交商户报错的错误信息
     */
    void  commitbusinesserror_info(String shopid,String type,String businessErrorType,String details){
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/shop/shopErrorFeedback.do", "", extra, new FFNetWorkCallBack<BaseResult>() {
        post(IUrlUtils.Search.shopErrorFeedback, "", extra, new FFNetWorkCallBack<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response, FFExtraParams extra) {
                if (response.judge()) {
                    showToast("提交成功!");
                    finish();
                } else {
                    showToast(response.getServerMsg());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {

                return false;
            }
        }, "shopId", shopid, "type", type,"businessErrorType",businessErrorType,"details",details);
    }

}
