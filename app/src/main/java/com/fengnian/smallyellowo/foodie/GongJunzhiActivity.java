package com.fengnian.smallyellowo.foodie;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017-2-28.
 */

public class GongJunzhiActivity extends Activity implements View.OnClickListener {

    private ImageView iv_know;
    private RelativeLayout rl_0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gongjinzhi);
        rl_0= (RelativeLayout) findViewById(R.id.rl_0);
        rl_0.setOnClickListener(this);

        iv_know= (ImageView) findViewById(R.id.iv_know);
        iv_know.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_know:
                finish();
                break;
            case R.id.rl_0:
                finish();
                break;
        }
    }
}
