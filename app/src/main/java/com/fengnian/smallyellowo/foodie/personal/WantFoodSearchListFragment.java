package com.fengnian.smallyellowo.foodie.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fan.framework.config.Value;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.View.RecyclerLoadMoreFooterView;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.widgets.LoadMoreRecyclerListener;

import java.util.List;

public class WantFoodSearchListFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    public WantFoodSearchFragAdapter mAdapter;
    public MainUserModelClass.ProfileCenterRightRequestParams mParams = new MainUserModelClass.ProfileCenterRightRequestParams();
    private LoadMoreRecyclerListener mLoadMoreRecyclerListener;

    public void onPopupWindowDismissEvent(final MainUserModelClass.ProfileCenterRightRequestParams params) {
        Log.v("tag_1","新的params = " + params);
        Log.d("tag_1","旧的params = " + mParams);
        if (params != null && !params.toString().equals(mParams.toString())) {
            params.keyWord = mParams.keyWord;
            mParams = params;
            getData(true);
        }
    }

    public void setKeyword(String keyword) {
        mParams.keyWord = keyword;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new WantFoodSearchFragAdapter(this);
        if (getActivity().getIntent() != null) {
            mParams.keyWord = getActivity().getIntent().getStringExtra("keyWord");
//            int isCategory = getActivity().getIntent().getIntExtra("isCategory", -1);
//            String key = getActivity().getIntent().getStringExtra("key");
//            if (isCategory == 1) {
//                mParams.streetId = key;
//            } else if (isCategory == 0) {
//                mParams.ptype = key;
//            }
        }
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
        getData(isReset, false);
    }

    public void getData(final boolean isReset, boolean isShow) {
        if (isReset) {
            mParams.lastFoodEatId = "";
        }

        String isShowStr = null;
        if (isShow) {
            isShowStr = "";
        }

//        post(Constants.shareConstants().getNetHeaderAdress() + "/myCenter/searchFoodEatByKeyWord.do",
        post(IUrlUtils.Search.searchFoodEatByKeyWord,
                isShowStr, null, new FFNetWorkCallBack<DeliciousFoodModel.FoodNetList>() {
                    @Override
                    public void onSuccess(DeliciousFoodModel.FoodNetList response, FFExtraParams extra) {
                        if (response != null) {
                            List<DeliciousFoodModel.SYSearchUserFoodModel> foodModelList = response.sySearchUserFoodModels;
                            if (isReset) {
                                mLoadMoreRecyclerListener.reset();
                                mAdapter.setDataList(foodModelList);
                            } else {
                                mAdapter.appendDataList(foodModelList);
                            }
                            mAdapter.setKeyword(mParams.keyWord);

                            final int count = foodModelList.size();
                            if (count > 0) {
                                mParams.lastFoodEatId = foodModelList.get(count - 1).wantEatId;
                            }


                            if (count < Integer.parseInt(mParams.pageSize)) {
                                mAdapter.setLoadState(RecyclerLoadMoreFooterView.LOAD_FINISH);
                            } else {
                                mAdapter.setLoadState(RecyclerLoadMoreFooterView.LOADING);
                            }

                            //发广播给tab栏显示数字用
                            Intent broadcastIntent = new Intent(MyActions.PROFILE_SEARCH_FINISH);
                            broadcastIntent.putExtra("tab_index", MainUserInfoFragmentHelper.TAB_RIGHT);
                            broadcastIntent.putExtra("count", response.eatNumber);
                            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(broadcastIntent);
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "ptype", mParams.ptype
                , "streetId", mParams.streetId
                , "keyWord", mParams.keyWord
                , "lastFoodEatId", mParams.lastFoodEatId
                , "pageSize", mParams.pageSize
                , "longitude", Value.mLongitude + ""
                , "latitude", Value.mLatitude + "");
    }

    public void clear(){
        mAdapter.getDataList().clear();
        mAdapter.notifyDataSetChanged();
    }

    public static WantFoodSearchListFragment newInstance() {
        WantFoodSearchListFragment tabFragment = new WantFoodSearchListFragment();
        return tabFragment;
    }

}
