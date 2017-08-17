package com.fengnian.smallyellowo.foodie.shopcommiterror;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
 * Created by Administrator on 2017-3-30.
 */

public class ShopInfoErrorActivity extends BaseActivity<ShopErrorInfoIntent> implements  View.OnClickListener {

    private EditText ed_name,ed_address,ed_tel;

    private ImageView iv_name_delete,iv_address_delete,iv_tel_delete;
    private TextView tv_commit;

    String old_name,old_address,old_tel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMenuContainer().removeAllViews();
        setContentView(R.layout.activity_shop_info_error);
        ed_name=findView(R.id.ed_name);
        ed_name.addTextChangedListener(name_watch);
        iv_name_delete=findView(R.id.iv_name_delete);
        iv_name_delete.setOnClickListener(this);

        ed_address=findView(R.id.ed_address);
        ed_address.addTextChangedListener(address_watch);
        iv_address_delete=findView(R.id.iv_address_delete);
        iv_address_delete.setOnClickListener(this);

        ed_tel=findView(R.id.ed_tel);
        ed_tel.addTextChangedListener(tel_watch);
        iv_tel_delete=findView(R.id.iv_tel_delete);
        iv_tel_delete.setOnClickListener(this);

        tv_commit=findView(R.id.tv_commit);
        tv_commit.setOnClickListener(this);

        setoldvalue();

    }
   TextWatcher name_watch=new TextWatcher() {
       @Override
       public void beforeTextChanged(CharSequence s, int start, int count, int after) {

       }

       @Override
       public void onTextChanged(CharSequence s, int start, int before, int count) {

       }

       @Override
       public void afterTextChanged(Editable s) {
           String address=ed_address.getText().toString().trim();
           String tel=ed_tel.getText().toString().trim();
           selector(value_is_chang(s.toString(),address,tel));
           setIsVisbale(iv_name_delete,ed_name,old_name);
       }
   };
    TextWatcher address_watch=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String name=ed_name.getText().toString().trim();
            String tel=ed_tel.getText().toString().trim();
            selector(value_is_chang(name,s.toString(),tel));
            setIsVisbale(iv_address_delete,ed_address,old_address);
        }
    };
    TextWatcher tel_watch=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String name=ed_name.getText().toString().trim();
            String addres=ed_address.getText().toString().trim();
            boolean tel_falg=value_is_chang(name,addres,s.toString());
            selector(tel_falg);
            setIsVisbale(iv_tel_delete,ed_tel,old_tel);
        }
    };


    void   setIsVisbale(ImageView view,EditText ed,String old_value){
        String str=ed.getText().toString().trim();
        boolean flag=false;
        if(!TextUtils.isEmpty(str))
            flag=str.equals(old_value)?false:true;
        if(flag){
            view.setVisibility(View.VISIBLE);
        }else{
            view.setVisibility(View.GONE);
        }

    }
    boolean  value_is_chang(String name,String address,String tel){

        String name_flag="";
        String address_flag="";
        String tel_flag="";
        if(!TextUtils.isEmpty(name))
            name_flag=name.equals(old_name)?"1":"0";
        if(!TextUtils.isEmpty(address))
            address_flag=address.equals(old_address)?"1":"0";
        if(!TextUtils.isEmpty(tel))
            tel_flag=tel.equals(old_tel)?"1":"0";
        return  "0".equals(name_flag)||"0".equals(address_flag)||"0".equals(tel_flag);
    }

    void   setoldvalue(){
        old_name=getIntentData().getName();
        old_address=getIntentData().getAddress();
        old_tel=getIntentData().getPhone();
        ed_name.setText(old_name);
        ed_address.setText(old_address);
        ed_tel.setText(old_tel);

        selector(false);
    }


    void   selector(boolean fl){
        tv_commit.setClickable(fl);
        if(fl){
            tv_commit.setBackgroundResource(R.drawable.login_normal);
            tv_commit.setTextColor(getResources().getColor(R.color.color_1));
        }else{
            tv_commit.setBackgroundResource(R.drawable.logout_press);
            tv_commit.setTextColor(getResources().getColor(R.color.com_sina_weibo_sdk_loginview_text_color));
        }
    }
    @Override
    public void onClick(View v) {
         switch (v.getId()){
             case R.id.iv_name_delete:
                 ed_name.setText("");
                 break;
             case R.id.iv_address_delete:
                 ed_address.setText("");
                 break;
             case R.id.iv_tel_delete:
                 ed_tel.setText("");
                 break;
             case R.id.tv_commit:
                 String name=ed_name.getText().toString().trim();
                 String address=ed_address.getText().toString().trim();
                 String tel=ed_tel.getText().toString().trim();
                 String details=name+";"+address+";"+tel;
                 commitbusinesserror_info(getIntentData().getShopid(),getIntentData().getError_type()+"",details);

                 break;
         }
    }

    /**
     * 提交商户报错的错误信息
     */
    void  commitbusinesserror_info(String shopid,String type,String details) {
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
        }, "shopId", shopid, "type", type, "details", details);
    }
}
