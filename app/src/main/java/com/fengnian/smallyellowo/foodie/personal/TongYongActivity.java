package com.fengnian.smallyellowo.foodie.personal;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.fengnian.smallyellowo.foodie.CacheManagerActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;

/**
 * Created by Administrator on 2016-11-22.
 */

public class TongYongActivity extends BaseActivity<IntentData> implements View.OnClickListener {

    private RelativeLayout rl_clear_cache;
    private ToggleButton tl_toggle;
    private boolean flag;

    private Drawable  drawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongyong);
        rl_clear_cache=findView(R.id.rl_clear_cache);
        rl_clear_cache.setOnClickListener(this);
        tl_toggle=findView(R.id.tl_toggle);
        tl_toggle.setOnClickListener(this);

        drawable = getResources().getDrawable(R.drawable.me_remind_point);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
        flag= SP.getCheckScore();

        refreshui(flag);
    }


    private void  refreshui(boolean  stus){
        if(stus){
            tl_toggle.setCompoundDrawables(null, null, drawable, null);
            tl_toggle.setBackgroundResource(R.mipmap.me_remind_normal);
            tl_toggle.setChecked(true);
        }else{
            tl_toggle.setCompoundDrawables(drawable, null,null , null);
            tl_toggle.setBackgroundResource(R.mipmap.me_remind_pressed);
            tl_toggle.setChecked(false);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_clear_cache:
                startActivity(CacheManagerActivity.class, new IntentData());
                break;
            case R.id.tl_toggle:
                if(flag){
                    flag=false;
                }else{
                    flag=true;
                }
                SP.setCheckScore(flag);
                refreshui(flag);

                break;
        }
    }
}
