package com.fengnian.smallyellowo.foodie.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017-3-6.
 */

public class AdjustResizeLayout extends LinearLayout {
    public AdjustResizeLayout(Context context) {
        super(context);
    }

    public AdjustResizeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AdjustResizeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AdjustResizeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

//    private int[] mInsets = new int[4];
//
//    public final int[] getInsets() {
//        return mInsets;
//    }

    @Override
    protected final boolean fitSystemWindows(Rect insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Intentionally do not modify the bottom inset. For some reason,
            // if the bottom inset is modified, window resizing stops working.
            // TODO: Figure out why.

//            mInsets[0] = insets.left;
//            mInsets[1] = insets.top;
//            mInsets[2] = insets.right;

            insets.left = 0;
            insets.top = 0;
            insets.right = 0;
        }

        return super.fitSystemWindows(insets);
    }
}
