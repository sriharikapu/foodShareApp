package com.fengnian.smallyellowo.foodie.shopcommiterror;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.ShopErrorInfoIntent;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

/**
 * Created by Administrator on 2017-3-28.
 */

public class ShopBusinessQuestionActivty extends BaseActivity<ShopErrorInfoIntent> implements View.OnClickListener {
    private RelativeLayout rl_1,rl_2,rl_3,rl_4,rl_5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_business_question);
        getMenuContainer().removeAllViews();
        rl_1=findView(R.id.rl_1);
        rl_2=findView(R.id.rl_2);
        rl_3=findView(R.id.rl_3);
        rl_4=findView(R.id.rl_4);
        rl_5=findView(R.id.rl_5);

        rl_1.setOnClickListener(this);
        rl_2.setOnClickListener(this);
        rl_3.setOnClickListener(this);
        rl_4.setOnClickListener(this);
        rl_5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_1:
                setDialog("确认未找到该商户？","1");
                break;
            case R.id.rl_2:
                setDialog("确认该商户尚未开张营业？","2");
                break;
            case R.id.rl_3:
                setDialog("确认该商户暂停营业？","3");
                break;
            case R.id.rl_4:
                setDialog("确认该商户停止营业？","4");
                break;
            case R.id.rl_5:
                getIntentData().setRequestCode(1000);
                getIntentData().setError_type_childe("5");
                startActivity(ShopBanQianAdressActivtyi.class,getIntentData());
                break;
        }
    }



    void   setDialog(String title, final String type){
        new EnsureDialog.Builder(ShopBusinessQuestionActivty.this)
//                        .setTitle("系统提示")//设置对话框标题

                .setMessage(title)//设置显示的内容

                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮

                    @Override

                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        commitbusinesserror_info(getIntentData().getShopid(),getIntentData().getError_type()+"",type);
                    }

                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
            @Override

            public void onClick(DialogInterface dialog, int which) {//响应事件

            }

        }).show();//在按键响应事件中显示此对话框
    }
    /**
     * 提交商户报错的错误信息
     */
    void  commitbusinesserror_info(String shopid,String type,String businessErrorType){
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
                      setResult(10);
                      finish();
                   } else {
                      showToast(response.getServerMsg());
                 }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {

                return false;
            }
        }, "shopId", shopid, "type", type,"businessErrorType",businessErrorType);
    }
}
