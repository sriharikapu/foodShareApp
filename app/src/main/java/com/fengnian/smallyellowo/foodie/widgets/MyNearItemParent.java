package com.fengnian.smallyellowo.foodie.widgets;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.fan.framework.utils.FFUtils;

/**
 * Created by Administrator on 2017-5-24.
 */

public class MyNearItemParent extends RelativeLayout{
    public MyNearItemParent(Context context) {
        super(context);
    }

    public MyNearItemParent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNearItemParent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyNearItemParent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(l < 100){
            setPadding(FFUtils.getPx(8),0,FFUtils.getPx(4),0);
        }
        else{
            setPadding(FFUtils.getPx(4),0,FFUtils.getPx(8),0);
        }
        super.onLayout(changed, l, t, r, b);
    }
}
