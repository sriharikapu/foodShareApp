package com.fengnian.smallyellowo.foodie.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by chenglin on 2017-6-13.
 */

public class MyScrollView extends ScrollView {
    private OnScrollChangedListener onScrollChangedListener;

    public interface OnScrollChangedListener {
        void onScrollChanged(int top, int oldTop);
    }

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (this.onScrollChangedListener != null) {
            onScrollChangedListener.onScrollChanged(t, oldt);
        }
    }

    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        this.onScrollChangedListener = onScrollChangedListener;
    }
}
