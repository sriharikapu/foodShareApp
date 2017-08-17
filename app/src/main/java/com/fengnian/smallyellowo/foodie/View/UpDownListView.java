package com.fengnian.smallyellowo.foodie.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;

/**
 * Created by chenglin on 2017-4-17.
 */

public class UpDownListView extends ListView {
    private final int mTouchSlop = DisplayUtil.dip2px(8f);
    private float mFirstY;
    private float mCurrentY;
    private boolean isScrollToUp = true;
    private onListViewUpDownListener mListener;

    public UpDownListView(Context context) {
        super(context);
    }

    public UpDownListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnListViewUpDownListener(onListViewUpDownListener listener) {
        mListener = listener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mFirstY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentY = event.getY();
                if (mFirstY - mCurrentY > mTouchSlop) {//上滑
                    if (isScrollToUp) {
                        if (mListener != null) {
                            mListener.onDownUp(true);
                        }
                        isScrollToUp = false;
                    }
                } else if (mCurrentY - mFirstY > mTouchSlop) {//下滑
                    if (!isScrollToUp) {
                        if (mListener != null) {
                            mListener.onDownUp(false);
                        }
                        isScrollToUp = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;

        }

        return super.dispatchTouchEvent(event);
    }

    public interface onListViewUpDownListener {
        void onDownUp(boolean isUp);
    }
}
