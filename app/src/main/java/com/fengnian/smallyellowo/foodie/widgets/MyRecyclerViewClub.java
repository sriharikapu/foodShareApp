package com.fengnian.smallyellowo.foodie.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017-3-3.
 */

public class MyRecyclerViewClub extends RecyclerView {
    public MyRecyclerViewClub(Context context) {
        super(context);
    }

    public MyRecyclerViewClub(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerViewClub(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public View getFView() {
        return fView;
    }

    public void setFView(View focusView) {
        this.fView = focusView;
    }

    private View fView;

    private int offsetX;
    private int offsetY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (fView != null && fView.getVisibility() == View.VISIBLE) {
            offsetY = 0;
            offsetX = 0;
            initOffset(fView);

            final Rect frame = new Rect();
            fView.getHitRect(frame);
            frame.bottom -= frame.top;
            frame.top = 0;
            final float xf = ev.getX();
            final float yf = ev.getY();
            if (frame.contains((int) xf - offsetX, (int) yf - offsetY)) {
                return false;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void initOffset(View view) {
        if (view == this || view == null) {
            return;
        }
        offsetY += view.getY();
        offsetX += view.getX();
        initOffset((View) view.getParent());
    }

    public static interface OnOutFViewActionDownListener {
        void OnOutFViewActionDown();
    }

    OnOutFViewActionDownListener listener;


    public void setOnOutFViewActionDownListener(OnOutFViewActionDownListener l) {
        listener = l;
    }
}
