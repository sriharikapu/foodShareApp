package com.fengnian.smallyellowo.foodie.View;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fengnian.smallyellowo.foodie.R;

/**
 * Created by chenglin on 2017-4-28.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    StaggeredGridLayoutManager mgr;

    public SpacesItemDecoration(int space, StaggeredGridLayoutManager staggeredGridLayoutManager) {
        this.space = space;
        mgr = staggeredGridLayoutManager;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = 0;
        if (view.getTag(R.id.head_view) != null) {
            position = (int) view.getTag(R.id.head_view);
        }
        if (position != 0) {
            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear);
            RelativeLayout.LayoutParams myParams = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();

            if (view.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
//                outRect.bottom = space;
                if (lp.getSpanIndex() == 0) {//左侧间距是最左侧view决定的 所以乘2
//                    outRect.left = space * 2;
//                    outRect.right = space;
//                    view.setPadding(space * 2,0,space ,0);
                    myParams.leftMargin = space * 2;
                    myParams.rightMargin = space;
                } else if (lp.getSpanIndex() == 1) {
                    ////左侧间距是最左侧view决定的 所以乘2
//                    outRect.left = space;
//                    outRect.right = space * 2;
//                    view.setPadding(space ,0,space * 2,0);
                    myParams.leftMargin = space;
                    myParams.rightMargin = space * 2;
                } else {//中间间距是有2个view共同组成
//                    outRect.left = space;
//                    outRect.right = space;
//                    view.setPadding(0,0,0 ,0);
                    myParams.leftMargin = 0;
                    myParams.rightMargin = 0;
                }
            }
            linearLayout.setLayoutParams(myParams);
        }else {
//            outRect.left = 0;
//            outRect.right = 0;
//            outRect.top = 0;
//            outRect.bottom = 0;
        }

    }
}