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

public class MyRecyclerView extends MyRecyclerViewClub {
    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public View getFocusView() {
        return focusView;
    }

    public void setFocusView(View focusView) {
        this.focusView = focusView;
    }

    protected View focusView;

    private int offsetX;
    private int offsetY;

    boolean has = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            has = false;
        }
        if (focusView != null && focusView.getVisibility() == View.VISIBLE) {
            offsetY = 0;
            offsetX = 0;
            initOffset(focusView);

            final Rect frame = new Rect();
            focusView.getHitRect(frame);
            frame.bottom -= frame.top;
            frame.top = 0;
            final float xf = ev.getX();
            final float yf = ev.getY();
            if (frame.contains((int) xf - offsetX, (int) yf - offsetY)) {
                has = true;
                return false;
            } else {
                if (listener != null && ev.getAction() == MotionEvent.ACTION_DOWN) {
                    listener.OnOutFoucusViewActionDown();
                }
                if (has) {
                    return false;
                }
                return true;
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

    public static interface OnOutFoucusViewActionDownListener {
        void OnOutFoucusViewActionDown();
    }

    OnOutFoucusViewActionDownListener listener;


    public void setOnOutFoucusViewActionDownListener(OnOutFoucusViewActionDownListener l) {
        listener = l;
    }
}
