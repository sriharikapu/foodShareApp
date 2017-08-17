package com.fengnian.smallyellowo.foodie.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by Administrator on 2016-9-2.
 */


    public class MyListView extends ListView
    {

        public MyListView(Context paramContext) {
            super(paramContext);
        }
        public MyListView(Context paramContext, AttributeSet paramAttributeSet) {
            super(paramContext, paramAttributeSet);
        }
        public MyListView(Context paramContext,
                          AttributeSet paramAttributeSet, int paramInt) {
            super(paramContext, paramAttributeSet, paramInt);
        }
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
        }
    }
