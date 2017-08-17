package com.fengnian.smallyellowo.foodie.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;

/**
 * Created by chenglin on 2017-2-21.
 * 用于viewPager下方的导航点点
 */

public class ViewPageNavigationDotLayout extends LinearLayout {
    private int mDotCount;
    private ViewPager mViewPager;

    public ViewPageNavigationDotLayout(Context context) {
        super(context);
    }

    public ViewPageNavigationDotLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setDotCount(int count) {
        this.setVisibility(View.VISIBLE);
        this.removeAllViews();
        this.setOrientation(LinearLayout.HORIZONTAL);

        mDotCount = count;
        if (mDotCount > 1) {
            for (int i = 0; i < mDotCount; i++) {
                addDot(i);
            }
        }
    }

    public int getDotCount() {
        return mDotCount;
    }

    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
    }

    private void addDot(final int index) {
        View view = new View(getContext());
        view.setBackgroundResource(R.drawable.viewpage_dot_selector);
        int size = DisplayUtil.dip2px(8f);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        params.leftMargin = DisplayUtil.dip2px(5f);
        params.rightMargin = params.leftMargin;
        this.addView(view, params);

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(index);
                }
            }
        });
    }

    public void clear() {
        this.removeAllViews();
        this.setVisibility(View.GONE);
    }

    public void setSelected(int index) {
        if (index < 0) {
            return;
        }

        for (int i = 0; i < this.getChildCount(); i++) {
            View view = this.getChildAt(i);
            if (i == index) {
                view.setSelected(true);
            } else {
                view.setSelected(false);
            }
        }
    }
}
