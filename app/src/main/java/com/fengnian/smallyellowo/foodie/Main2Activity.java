package com.fengnian.smallyellowo.foodie;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ImageView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Main2Activity extends BaseActivity<IntentData> {

    @Bind(R.id.iv_upper)
    ImageView ivUpper;
    @Bind(R.id.iv_lower)
    ImageView ivLower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        setNotitle(true);
        ivUpper.getLayoutParams().height = FFUtils.getDisWidth()*300/1080;
        ivLower.getLayoutParams().height = FFUtils.getDisWidth()*201/1080;
        showDialog();
    }

    private void showDialog() {
        EnsureDialog.showEnsureDialog(this, false, "初始化失败\r\n请检查网络后重新加载", "确定",null,null, new EnsureDialog.EnsureDialogListener() {
            @Override
            public void onOk(final DialogInterface dialog) {
                dialog.dismiss();
                if (FFUtils.isStringEmpty(SP.getYoukeToken())) {
                    post(IUrlUtils.Search.visitorRegister, "", null, new FFNetWorkCallBack<WelcomActivity.VisitorResult>() {
//                    post(Constants.shareConstants().getNetHeaderAdress() + "/visitorRegister.do", "", null, new FFNetWorkCallBack<WelcomActivity.VisitorResult>() {
                        @Override
                        public void onSuccess(WelcomActivity.VisitorResult response, FFExtraParams extra) {
                            SP.setYoukeToken(response.account);
                            startActivity(MainActivity.class,new IntentData());
                            finish();
                        }

                        @Override
                        public boolean onFail(FFExtraParams extra) {
                            return false;
                        }
                    });
                    return;
                }
            }

            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
    }
}
