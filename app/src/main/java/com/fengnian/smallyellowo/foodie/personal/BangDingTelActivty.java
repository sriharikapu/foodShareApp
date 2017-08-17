package com.fengnian.smallyellowo.foodie.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;

/**
 * Created by Administrator on 2017-3-23.
 */

public class BangDingTelActivty extends BaseActivity<IntentData> {

    private TextView tv_bangding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bangding_tel);
        tv_bangding=findView(R.id.tv_bangding);
        tv_bangding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  startActivity(BangDingTelNextActivty.class,new IntentData());
                  finish();

            }
        });

    }
}
