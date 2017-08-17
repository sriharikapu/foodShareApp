package com.fengnian.smallyellowo.foodie;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fan.framework.imageloader.FFImageLoader;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;

/**
 * Created by Administrator on 2016-9-24.
 */

public class CheckBigimgActivty extends BaseActivity {
   private ImageView iv_img;
    private RelativeLayout rl_1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url=getIntent().getStringExtra("url");
        setContentView(R.layout.activity_tcheck_big_img);
         rl_1= (RelativeLayout) findView(R.id.rl_1);
        iv_img= (ImageView) findView(R.id.iv_avator);
        setNotitle(true);
        FFImageLoader.loadBigImage(this, url, iv_img);

        rl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBigimgActivty.this.finish();
            }
        });
    }
}
