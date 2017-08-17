package com.fengnian.smallyellowo.foodie.widgets;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by chenglin on 2017-4-5.
 */
public abstract class LoadMoreRecyclerListener extends RecyclerView.OnScrollListener {
    private int previousTotal = 0;
    private boolean loading = true;
    private int firstVisibleItem, visibleItemCount, totalItemCount;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean isEnable = true;

    public LoadMoreRecyclerListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    /**
     * 重置下拉参数
     */
    public void reset() {
        previousTotal = 0;
        loading = true;
        isEnable = true;
    }

    /**
     * 设置下拉加载更多是否可用
     */
    public void setEnable(boolean is_enable) {
        isEnable = is_enable;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (!isEnable) {
            return;
        }

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= firstVisibleItem) {
            onLoadMore();
            loading = true;
        }
    }

    public abstract void onLoadMore();
}
