package com.fengnian.smallyellowo.foodie.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by chenglin on 2017-2-20.
 */

public class NoSlideGridView extends GridView {


    public NoSlideGridView(Context context) {
        super(context);
    }

    public NoSlideGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
