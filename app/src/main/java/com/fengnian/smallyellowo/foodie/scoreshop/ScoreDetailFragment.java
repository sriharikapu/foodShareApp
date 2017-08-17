package com.fengnian.smallyellowo.foodie.scoreshop;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.View.RecyclerLoadMoreFooterView;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.widgets.LoadMoreRecyclerListener;

import java.util.ArrayList;

/**
 * Created by chenglin on 2017-5-4.
 */
public class ScoreDetailFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    public ScoreDetailFragAdapter mAdapter;
    private LoadMoreRecyclerListener mLoadMoreRecyclerListener;
    private String mLastId = "0";
    private final int pageSize = 20;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ScoreDetailFragAdapter(this);
    }


    @Override
    public void onFindView() {

    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_profile_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_stickynavlayout_innerscrollview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        getData(true);
        mLoadMoreRecyclerListener = new LoadMoreRecyclerListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                getData(false);
            }
        };
        mRecyclerView.addOnScrollListener(mLoadMoreRecyclerListener);
    }

    public void getData(final boolean isRefresh) {
        if (!SP.isLogin()) {
            return;
        }
        if (!FFUtils.checkNet()) {
            showToast(getString(R.string.lsq_network_connection_interruption));
            return;
        }

//        post(Constants.shareConstants().getNetHeaderAdress() + "/shopmall/queryUserScoreAllHistoryList.do", "",
        post(IUrlUtils.Search.queryUserScoreAllHistoryList, "",
                null, new FFNetWorkCallBack<ScoreDetailActivity.ScoreDetailModel>() {
                    @Override
                    public void onSuccess(ScoreDetailActivity.ScoreDetailModel response, FFExtraParams extra) {
                        if (response != null) {
                            if (isRefresh) {
                                mLoadMoreRecyclerListener.reset();
                                mAdapter.setDataList(response.pointsInfoList);
                            } else {
                                mAdapter.appendDataList(response.pointsInfoList);
                            }

                            int count = 0;
                            if (response.pointsInfoList != null && response.pointsInfoList.size() > 0) {
                                count = response.pointsInfoList.size();
                                mLastId = response.pointsInfoList.get(count - 1).scoreDetailsId;
                            }

                            if (count < pageSize) {
                                mAdapter.setLoadState(RecyclerLoadMoreFooterView.LOAD_FINISH);
                            } else {
                                mAdapter.setLoadState(RecyclerLoadMoreFooterView.LOADING);
                            }

                            ScoreDetailActivity activity = (ScoreDetailActivity) getActivity();
                            if (!TextUtils.isEmpty(response.totalPoints)) {
                                activity.setScore(response.totalPoints);
                            }
                        }

                        //显示空页面
                        if (mAdapter.getItemCount() <= 0) {
                            mLoadMoreRecyclerListener.setEnable(false);
                            setEmptyView();
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "scoreDetailsId", mLastId,
                "pageSize", pageSize + "",
                "pointsType", "0");
    }

    /**
     * 显示空页面
     */
    public void setEmptyView() {
        mAdapter.setDataList(null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static ScoreDetailFragment newInstance() {
        ScoreDetailFragment tabFragment = new ScoreDetailFragment();
        return tabFragment;
    }


}
