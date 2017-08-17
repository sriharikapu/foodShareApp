package com.fengnian.smallyellowo.foodie.appbase;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fan.framework.base.MyBaseAdapter;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.widgets.PullToRefreshLayout;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.results.BasePullToRefreshResult;

/**
 * 基础下拉刷新fragment
 * Created by 张翻 on 2017-5-31.
 */

public abstract class PullToRefreshFragment<H, D, Re extends BasePullToRefreshResult<D>> extends BaseFragment {
    ListView listView;
    private com.fan.framework.base.MyBaseAdapter<H, D> adapter;
    private PullToRefreshLayout prl;
    private int currentPage = 0;
    private int dividerHeight = 0;

    Class<Re> clazz;

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_bwtd, container, false);
    }

    public void onResult(Re result) {

    }

    public void setDividerHeight(int height) {
        dividerHeight = height;
        if(listView == null) {
            return;
        }
        listView.setDividerHeight(height);
    }


    @Override
    public void onFindView() {
        clazz = FFUtils.getTClass(this, 2);
        listView = (ListView) findViewById(R.id.listView);
        listView.setDividerHeight(dividerHeight);
        Class<? extends H> tClass = FFUtils.getTClass(this, 0);
        adapter = new MyBaseAdapter<H, D>(tClass, getBaseActivity(), R.layout.item_eat_word) {
            @Override
            public void initView(View convertView, H holder, int position, final D item) {
                PullToRefreshFragment.this.refreshItem(convertView, holder, position, item);
            }

            @Override
            public void onGetView(H holder) {
                PullToRefreshFragment.this.onGetView(holder);
            }

            @Override
            public int getItemViewId(int position) {
                return PullToRefreshFragment.this.getItemViewId(position);
            }
        };
        listView.setAdapter(adapter);
        prl = PullToRefreshLayout.supportPull(listView, new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                getData(true);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                getData(false);
            }
        });
        prl.setDoPullDown(true);
        prl.setDoPullUp(true);
        FFUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                prl.autoRefresh();
            }
        }, 500);
    }


    private void getData(final boolean isRefresh) {
        if (isRefresh) {
            currentPage = 0;
        }
        post(getUrl(), isRefresh ? null : "", new FFExtraParams().setInitPage(isRefresh && adapter.getCount() == 0), new FFNetWorkCallBack<Re>(clazz) {
            @Override
            public void onSuccess(Re response, FFExtraParams extra) {
                onResult(response);
                prl.refreshFinish(PullToRefreshLayout.SUCCEED);
                currentPage++;
                if (FFUtils.isListEmpty(response.getData())) {
                    prl.loadmoreFinish(PullToRefreshLayout.NO_DATA_SUCCEED);
                } else {
                    prl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
                if (isRefresh) {
                    adapter.setData(response.getData());
                } else {
                    adapter.addData(response.getData());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                prl.loadmoreFinish(PullToRefreshLayout.FAIL);
                prl.refreshFinish(PullToRefreshLayout.FAIL);
                return false;
            }
        }, getParams(currentPage));
    }

    public MyBaseAdapter<H, D> getAdapter() {
        return adapter;
    }

    public int getCount() {
        if (adapter == null) {
            return 0;
        }
        return adapter.getCount();
    }


    /**
     * @param currentPage 当前要加载的页，0是第一页
     * @return
     */
    protected abstract Object[] getParams(int currentPage);

    protected abstract String getUrl();

    protected abstract void refreshItem(View convertView, H holder, int position, final D item);

    protected abstract void onGetView(H holder);

    protected abstract int getItemViewId(int position);
}
