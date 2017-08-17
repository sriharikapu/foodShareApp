package com.fengnian.smallyellowo.foodie.homepage;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fan.framework.config.FFConfig;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.http.FFNetWorkRequest;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.widgets.PullToRefreshLayout;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appbase.CityPref;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.bean.AdModelsList;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.scoreshop.OnFinishListener;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.ReplaceViewHelper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chenglin on 2017-4-7.
 */
public class HomeChildSelectedFrag extends BaseFragment {
    public static final int pageSize = 15;

    @Bind(R.id.my_recycle_view)
    RecyclerView mRecyclerView;
    public PullToRefreshLayout prl;
    private LinearLayoutManager mLinearManager;

    private HomeChildSelectedAdapter mAdapter;
    private RecommendTipsDialog mTipsDialog;
    private ReplaceViewHelper mReplaceViewHelper;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mAdapter != null) {
            mAdapter.onDestroy();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setDislikeIconHide();
    }

    public void setDislikeIconHide() {
        if (mAdapter != null) {
            mAdapter.setDislikeIconHide();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReplaceViewHelper = new ReplaceViewHelper(getActivity());
    }

    @Override
    public void onFindView() {

    }

    @Override
    public View getView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_child_selected_layout, container, false);
        ButterKnife.bind(this, view);
        prl = PullToRefreshLayout.supportPull(mRecyclerView, new PullToRefreshLayout.OnRefreshListener() {
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


        mLinearManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearManager);

        mAdapter = new HomeChildSelectedAdapter(HomeChildSelectedFrag.this);
        mRecyclerView.setAdapter(mAdapter);

        getData(true, true);

        return view;
    }

    /**
     * 精选页计划性推荐完成
     */
    public void recommendFinished() {
        if (mTipsDialog == null) {
            mTipsDialog = new RecommendTipsDialog(getActivity());
        }
        if (SP.getFirstHomeRecommend()) {
            mTipsDialog.addFirstRecommendDialog(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTipsDialog.dismiss();
                    mAdapter.hideRecommendView();
                }
            });
        } else {
            mTipsDialog.addRecommendDialog(new OnFinishListener() {
                @Override
                public void onFinish(Object object) {
                    mAdapter.hideRecommendView();
                }
            });
        }
    }


    public void getData(final boolean isRefresh) {
        getData(isRefresh, false);
    }

    public void getData(final boolean isRefresh, final boolean isShowProgress) {
        if (!FFUtils.checkNet()) {
            mReplaceViewHelper.toReplaceView(prl, R.layout.ff_nonet_layout);
            mReplaceViewHelper.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getData(true, true);
                }
            });
            return;
        }

        //只有刷新时才会重新获取banner和推荐位
        if (isRefresh) {
            getBannerData();
            getRecommendData();
        }

        String lastUgcId = "";
        if (isRefresh || mAdapter.getFeedList().size() <= 0) {
            lastUgcId = "";
        } else {
            if (mAdapter.getFeedList().size() > 0) {
                lastUgcId = mAdapter.getFeedList().get(mAdapter.getFeedList().size() - 1).getFeedId();
            }
        }

        String isShow = null;
        if (isShowProgress) {
            isShow = "";
        }

        post(IUrlUtils.Search.queryIndexSelection, isShow,
                new FFExtraParams(), new FFNetWorkCallBack<UgcResult>() {
                    @Override
                    public void onSuccess(UgcResult response, FFExtraParams extra) {
                        mReplaceViewHelper.removeView();
                        if (isRefresh) {
                            prl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            mAdapter.setDataList(response.feeds);
                        } else {
                            mAdapter.appendDataList(response.feeds);
                        }

                        prl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        if (FFUtils.isListEmpty(response.feeds) || response.feeds.size() < pageSize) {
                            prl.loadmoreFinish(PullToRefreshLayout.NO_DATA_SUCCEED);
                        }

                        if (mAdapter.getItemCount() <= 0) {
                            setEmptyView();
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        prl.loadmoreFinish(PullToRefreshLayout.FAIL);
                        prl.refreshFinish(PullToRefreshLayout.FAIL);
                        return false;
                    }
                }, "pageSize", pageSize
                , "lastUgcId", lastUgcId);
    }

    /**
     * 得到计划性推荐的数据
     */
    private void getRecommendData() {
        //只有登录并且是北京，才有推荐
        if (SP.isLogin() && CityPref.isBeiJing()) {
            post(IUrlUtils.HomeUrl.SELECTED_RECOMMEND, null, null, new FFNetWorkCallBack<RecommendModel>() {
                @Override
                public void onSuccess(RecommendModel response, FFExtraParams extra) {
                    if (response != null && response.feeds != null) {
                        mAdapter.setRecommendModel(response.feeds);
                    }
                }

                @Override
                public boolean onFail(FFExtraParams extra) {
                    prl.loadmoreFinish(PullToRefreshLayout.FAIL);
                    prl.refreshFinish(PullToRefreshLayout.FAIL);
                    return false;
                }
            });
        }
    }

    /**
     * 获取头部banner列表
     */
    private void getBannerData() {
        String indexKey = CityPref.getCityConfigInfo().getBanner();

        post(IUrlUtils.CommonUrl.PAGE_URL, null, null, new FFNetWorkCallBack<AdModelsList>() {
                    @Override
                    public void onSuccess(AdModelsList response, FFExtraParams extra) {
                        if (response != null && response.data != null && response.data.size() > 0) {
                            mAdapter.setHeaderPgcList(response.data.get(0).itemsAD);
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "pageKey", indexKey
                , "debug", !FFConfig.IS_OFFICIAL);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //点击tab自动刷新
    public void setAutoRefresh() {
        mRecyclerView.scrollToPosition(0);
        prl.autoRefresh();

    }

    private void setEmptyView() {
        mReplaceViewHelper.toReplaceView(prl, R.layout.ff_empty_layout);
        TextView textView = (TextView) mReplaceViewHelper.getView().findViewById(R.id.text);
        textView.setText(R.string.empty_data_text);
        mReplaceViewHelper.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(true, true);
            }
        });
    }

    @Override
    public void onPageInitRetry(FFNetWorkRequest request) {
//        super.onPageInitRetry(request);
        prl.autoRefresh();
    }

    public static class RecommendModel extends BaseResult {
        public SYUgcModel feeds;
    }


    public static class UgcResult extends BaseResult {
        public ArrayList<SYUgcModel> feeds;
    }
}