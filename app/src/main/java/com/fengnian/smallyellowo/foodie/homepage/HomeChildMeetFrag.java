package com.fengnian.smallyellowo.foodie.homepage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.fan.framework.config.FFConfig;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.widgets.PullToRefreshLayout;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appbase.CityPref;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.AdModelsList;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.ReplaceViewHelper;

import java.util.List;

import static com.fan.framework.widgets.PullToRefreshLayout.supportPull;

/**
 * Created by chenglin on 2017-4-7.
 */

public class HomeChildMeetFrag extends BaseFragment {
    private PullToRefreshLayout prl;
    private ListView mListView;
    private HomeChildMeetAdapter mAdapter;
    private ReplaceViewHelper mReplaceViewHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReplaceViewHelper = new ReplaceViewHelper(getActivity());
    }

    @Override
    public void onFindView() {
        mListView = findView(R.id.list_view);
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_child_meet_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new HomeChildMeetAdapter(this);
        mListView.setAdapter(mAdapter);

        prl = supportPull(mListView, new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                getData(true);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                getData(false);
            }
        });
        prl.setDoPullUp(false);
        prl.setDoPullDown(true);

        getData(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isHidden()) {
            if (mAdapter != null) {
                mAdapter.onResume();
            }
        }
    }

    public void getData(final boolean isRefresh) {
        getData(isRefresh, false);
    }

    public void getData(final boolean isRefresh, final boolean isShowProgress) {
        if (!FFUtils.checkNet()) {
            if (prl != null) {
                prl.refreshFinish(PullToRefreshLayout.SUCCEED);
//                prl.setDoPullUp(false);
            }
            setNoNetView(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getData(true, true);
                }
            });
            return;
        }

        String isShow = null;
        if (isShowProgress) {
            isShow = "";
        }

        String indexKey = CityPref.getCityConfigInfo().getActivityUrl();

        post(IUrlUtils.CommonUrl.PAGE_URL, isShow, null, new FFNetWorkCallBack<AdModelsList>() {
                    @Override
                    public void onSuccess(AdModelsList response, FFExtraParams extra) {
                        if (response != null && response.data != null && response.data.size() > 0) {
                            mReplaceViewHelper.removeView();

                            if (isRefresh) {
                                mAdapter.setDataList(response.data.get(0).itemsAD);
                                prl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            } else {
                                mAdapter.appendDataList(response.data.get(0).itemsAD);
                            }

//                            if (response.data.get(0).itemsAD.size() > 0) {
//                                prl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//                            } else {
//                                prl.loadmoreFinish(PullToRefreshLayout.NO_DATA_SUCCEED);
//                            }

                            if (mAdapter.getCount() <= 0) {
                                setEmptyView();
//                                prl.setDoPullUp(false);
                            } else {
//                                prl.setDoPullUp(true);
                            }
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
//                        prl.loadmoreFinish(PullToRefreshLayout.FAIL);
                        prl.refreshFinish(PullToRefreshLayout.FAIL);
                        return false;
                    }
                }, "pageKey", indexKey
                , "debug", !FFConfig.IS_OFFICIAL);
    }

//    /**
//     * 获取头部banner列表
//     */
//    private void getBannerData() {
//        String indexKey = CityPref.getCityConfigInfo().getBanner();
//
//        post(IUrlUtils.CommonUrl.PAGE_URL, null, null, new FFNetWorkCallBack<AdModelsList>() {
//                    @Override
//                    public void onSuccess(AdModelsList response, FFExtraParams extra) {
//                        if (response != null && response.data != null && response.data.size() > 0) {
//                            setBannerView(response.data.get(0).itemsAD);
//                        }
//                    }
//
//                    @Override
//                    public boolean onFail(FFExtraParams extra) {
//
//                        return false;
//                    }
//                }, "pageKey", indexKey
//                , "debug", !FFConfig.IS_OFFICIAL);
//    }

    public void setAutoRefresh() {
        mListView.setSelection(0);
        prl.autoRefresh();
    }


    /**
     * 设置空的HeaderView
     */
    private void setEmptyView() {
        mReplaceViewHelper.toReplaceView(prl, R.layout.ff_empty_layout);
        resetData();
    }

    /**
     * 设置无网络的HeaderView
     */
    private void setNoNetView(View.OnClickListener clickListener) {
        mReplaceViewHelper.toReplaceView(prl, R.layout.ff_nonet_layout);
        mReplaceViewHelper.getView().setOnClickListener(clickListener);
        resetData();
    }

    private void resetData() {
        mAdapter.getDataList().clear();
        mAdapter.notifyDataSetChanged();
    }

    public static class MeetModel extends BaseResult {
        public List<SYHomeActivityModel> list;
    }
}
