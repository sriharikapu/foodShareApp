package com.fengnian.smallyellowo.foodie.homepage;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fan.framework.config.Value;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.widgets.PullToRefreshLayout;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYChoiceModel;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.List;

import static com.fan.framework.widgets.PullToRefreshLayout.supportPull;

/**
 * Created by chenglin on 2017-4-7.
 */

public class HomeChildNearbyFrag extends BaseFragment {
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private HomeChildNearbyAdapter mAdapter;
    private PullToRefreshLayout prl;
    private RequestParams mParams = new RequestParams();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onFindView() {
        mRecyclerView = findView(R.id.recycler_view);
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_child_nearby_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new HomeChildNearbyAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setCurrentBeCoveredView(mRecyclerView);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mStaggeredGridLayoutManager.invalidateSpanAssignments();
            }
        });

        prl = supportPull(mRecyclerView, new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                mParams.lastId = null;
                getData(true);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                getData(false);
            }
        });
        prl.setDoPullUp(true);
        prl.setDoPullDown(true);
        mAdapter.setPullToRefreshLayout(prl);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void getData(final boolean isRefresh) {
        getData(isRefresh, false);
    }

    public void getData(final boolean isRefresh, final boolean isShowProgress) {
        if (!FFUtils.checkNet()) {
            if (prl != null) {
                prl.refreshFinish(PullToRefreshLayout.SUCCEED);
                prl.setDoPullUp(false);
            }
            mAdapter.setNoNet(new View.OnClickListener() {
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

//        post(Constants.shareConstants().getNetHeaderAdress() + "/index/queryIndexVicinity.do",
        post(IUrlUtils.Search.queryIndexVicinity,
                isShow, null, new FFNetWorkCallBack<NearbyModel>() {
                    @Override
                    public void onSuccess(NearbyModel response, FFExtraParams extra) {
                        if (isRefresh) {
                            mAdapter.setHeaderDataList(response.pgcs);
                            mAdapter.setDataList(response.feeds);
                            prl.refreshFinish(PullToRefreshLayout.SUCCEED);
                        } else {
                            if (response.feeds.size() > 0) {
                                prl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                            } else {
                                prl.loadmoreFinish(PullToRefreshLayout.NO_DATA_SUCCEED);
                            }
                            mAdapter.appendDataList(response.feeds);
                        }

                        if (response.feeds != null && response.feeds.size() > 0) {
                            mParams.lastId = response.feeds.get(response.feeds.size() - 1).feedId;
                        }

                        if (mAdapter.getItemCount() <= 0) {
                            mAdapter.setEmpty();
                            prl.setDoPullUp(false);
                        } else {
                            prl.setDoPullUp(true);
                            mAdapter.setNormal();
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        prl.loadmoreFinish(PullToRefreshLayout.FAIL);
                        prl.refreshFinish(PullToRefreshLayout.FAIL);
                        return false;
                    }
                }, "pageSize", mParams.pageSize
                , "lastId", mParams.lastId
                , "streetId", mParams.streetId
                , "longitude", Value.mLongitude
                , "latitude", Value.mLatitude);
    }

    public void setAutoRefresh() {
        mRecyclerView.scrollToPosition(0);
        prl.autoRefresh();
    }

    public static class NearbyModel extends BaseResult {
        public List<SYChoiceModel> pgcs;
        public List<SYUgcModel> feeds;
    }

    public static final class RequestParams {
        public final int pageSize = 20;
        public String lastId;
        public String streetId = "5";//现在只开通三里屯，所以这里写死了是5
    }
}
