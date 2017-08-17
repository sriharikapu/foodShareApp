package com.fengnian.smallyellowo.foodie.shopcommiterror;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.ShopErrorInfoIntent;

/**
 * Created by Administrator on 2017-3-28.
 */

public class ShopErrorTypeActivity extends BaseActivity<ShopErrorInfoIntent> implements View.OnClickListener {


    private RelativeLayout rl_1,rl_2,rl_3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_error_type);

        rl_1=findView(R.id.rl_1);
        rl_2=findView(R.id.rl_2);
        rl_3=findView(R.id.rl_3);

        rl_1.setOnClickListener(this);
        rl_2.setOnClickListener(this);
        rl_3.setOnClickListener(this);
        getMenuContainer().removeAllViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_1:
                getIntentData().setError_type(1);
                startActivity(ShopBusinessQuestionActivty.class,getIntentData());
                break;
            case R.id.rl_2:
                getIntentData().setError_type(2);
                startActivity(ChangeAdressToMapActivity.class,getIntentData());
                break;
            case R.id.rl_3:
                getIntentData().setError_type(3);
                startActivity(ShopInfoErrorActivity.class,getIntentData());
                break;
        }
    }
}
