package com.fengnian.smallyellowo.foodie.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by Administrator on 2017-3-15.
 */

public class DynamicReviewListView extends ListView {
    public DynamicReviewListView(Context context) {
        super(context);
        init();
    }

    public DynamicReviewListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DynamicReviewListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DynamicReviewListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        isIntercept = true;
    }


    public boolean isIntercept() {
        return isIntercept;
    }

    public void setIntercept(boolean intercept) {
        isIntercept = intercept;
    }

    boolean isIntercept;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isIntercept) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
