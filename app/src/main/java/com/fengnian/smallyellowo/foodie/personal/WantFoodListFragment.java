package com.fengnian.smallyellowo.foodie.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fan.framework.config.Value;
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

import java.util.List;

public class WantFoodListFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    public WantFoodFragAdapter mAdapter;
    private WantFoodListHelper mHelper;
    public MainUserModelClass.ProfileCenterRightRequestParams mParams = new MainUserModelClass.ProfileCenterRightRequestParams();
    private LoadMoreRecyclerListener mLoadMoreRecyclerListener;

    public void onPopupWindowDismissEvent(final MainUserModelClass.ProfileCenterRightRequestParams params) {
        if (params != null && !params.toString().equals(mParams.toString())) {
            mParams = params;
            getData(true);
        }
    }

    /**
     * 重置搜索参数
     */
    public void resetFilterParams() {
        mParams = new MainUserModelClass.ProfileCenterRightRequestParams();
        getData(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHelper = new WantFoodListHelper(this);
        mAdapter = new WantFoodFragAdapter(this);
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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_stickynavlayout_innerscrollview);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void getData(final boolean isReset) {
        if (!SP.isLogin()) {
            return;
        }
        if (!FFUtils.checkNet()) {
            showToast(getString(R.string.lsq_network_connection_interruption));
            return;
        }
        if (isReset) {
            mParams.lastFoodEatId = "";
        }

//        post(Constants.shareConstants().getNetHeaderAdress() + "/myCenter/queryFoodEatByCriteria.do",
        post(IUrlUtils.Search.queryFoodEatByCriteria,
                null, null, new FFNetWorkCallBack<DeliciousFoodModel.FoodNetList>() {
                    @Override
                    public void onSuccess(DeliciousFoodModel.FoodNetList response, FFExtraParams extra) {
                        if (response != null && response.sySearchUserFoodModels != null) {
                            List<DeliciousFoodModel.SYSearchUserFoodModel> foodModelList = response.sySearchUserFoodModels;
                            if (isReset) {
                                mLoadMoreRecyclerListener.reset();
                                mAdapter.setDataList(foodModelList);
                            } else {
                                mAdapter.appendDataList(foodModelList);
                            }

                            final int count = foodModelList.size();
                            if (count > 0) {
                                mParams.lastFoodEatId = foodModelList.get(count - 1).wantEatId;
                            }

                            if (count < Integer.parseInt(mParams.pageSize)) {
                                mAdapter.setLoadState(RecyclerLoadMoreFooterView.LOAD_FINISH);
                            } else {
                                mAdapter.setLoadState(RecyclerLoadMoreFooterView.LOADING);
                            }

                            //显示空页面
                            if (mAdapter.getItemCount() <= 0) {
                                setEmptyView();
                            }
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "ptype", mParams.ptype
                , "streetId", mParams.streetId
                , "eatType", mParams.eatType
                , "eatSort", mParams.eatSort
                , "eatSource", mParams.eatSource
                , "pageSize", mParams.pageSize
                , "longitude", Value.mLongitude + ""
                , "latitude", Value.mLatitude + ""
                , "lastFoodEatId", mParams.lastFoodEatId);
    }


    public void setEmptyView() {
        MainMyUserFragment myUserFragment = null;
        if (getParentFragment() != null && getParentFragment() instanceof MainMyUserFragment) {
            myUserFragment = (MainMyUserFragment) getParentFragment();
        }

        if (myUserFragment != null) {
            //判断是筛选导致的空页面，还是压根就没数据导致的空页面
            if (myUserFragment.isFilterChanged()) {
                mAdapter.setFilterEmptyState(true);
                mAdapter.setDataList(null);
            } else {
                mAdapter.setFilterEmptyState(false);
                mAdapter.setDataList(null);
                Intent intent = new Intent(MyActions.PROFILE_CENTER_EMPTY);
                intent.putExtra("right_tab", 100);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            }
        }
    }

    public static WantFoodListFragment newInstance() {
        WantFoodListFragment tabFragment = new WantFoodListFragment();
        return tabFragment;
    }

}
