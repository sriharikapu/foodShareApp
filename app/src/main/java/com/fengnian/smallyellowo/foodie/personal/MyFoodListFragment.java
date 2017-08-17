package com.fengnian.smallyellowo.foodie.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.MainActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.View.RecyclerLoadMoreFooterView;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.BrodcastActions;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.taskmanager.task.PublishModel;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.widgets.LoadMoreRecyclerListener;

import java.util.List;

public class MyFoodListFragment extends BaseFragment {
    public MyFoodListFragHelper mHelper;
    private RecyclerView mRecyclerView;
    public MyFoodFragAdapter mAdapter;
    public MainUserModelClass.ProfileCenterLeftRequestParams mParams = new MainUserModelClass.ProfileCenterLeftRequestParams();
    private BrodcastActions.FeedChangeListener mFeedChangeListener;
    private LoadMoreRecyclerListener mLoadMoreRecyclerListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHelper = new MyFoodListFragHelper(this);
        mAdapter = new MyFoodFragAdapter(this);
        addPublishListener();
    }

    public void onPopupWindowDismissEvent(final MainUserModelClass.ProfileCenterLeftRequestParams params) {
        if (params != null && !params.toString().equals(mParams.toString())) {
            mParams = params;
            getData(true);
        }
    }

    /**
     * 重置搜索参数
     */
    public void resetFilterParams() {
        mParams = new MainUserModelClass.ProfileCenterLeftRequestParams();
        getData(true);
    }

    //发布状态监听
    private void addPublishListener() {
        mFeedChangeListener = new BrodcastActions.FeedChangeListener() {

            @Override
            public void publishFailed(PublishModel task) {
                mAdapter.rebuildAllList();
            }

            @Override
            public void publishSuccessed(PublishModel task, SYFeed feed) {
                mAdapter.removeUnsavedMapWhenPublishSuccess(task, feed);
                if (MainActivity.fragment_is_change == 3) {
                    getData(true);
                }
            }

            @Override
            public void startTask(PublishModel task) {
                mAdapter.rebuildAllList();
            }

            @Override
            public void foodDeleted(SYFeed task, boolean isUserCenter) {
                //这里什么都不用做，已经在删除点击事件里做了
            }

            @Override
            public void newPubSuccess(SYFeed feed) {
            }
        };
        BrodcastActions.addListener(mFeedChangeListener);
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

    public void getData(final boolean isReset) {
        if (!SP.isLogin()) {
            return;
        }
        if (!FFUtils.checkNet()) {
            showToast(getString(R.string.lsq_network_connection_interruption));
            return;
        }
        if (isReset) {
            mParams.lastFoodNoteId = "";
        }

        if (mParams.isUnSaved) {
            mAdapter.buildNativeList();
            return;
        }

//        post(Constants.shareConstants().getNetHeaderAdress() + "/myCenter/queryFoodNoteByCriteria.do",
        post(IUrlUtils.Search.queryFoodNoteByCriteria,
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
                                mParams.lastFoodNoteId = foodModelList.get(count - 1).foodNoteId;
                            }

                            if (count < Integer.parseInt(mParams.pageSize)) {
                                mAdapter.setLoadState(RecyclerLoadMoreFooterView.LOAD_FINISH);
                                mAdapter.addRestNativeData();
                            } else {
                                mAdapter.setLoadState(RecyclerLoadMoreFooterView.LOADING);
                            }
                        }

                        //显示空页面
                        if (mAdapter.getItemCount() <= 0) {
                            setEmptyView();
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "ptype", mParams.ptype
                , "streetId", mParams.streetId
                , "isElite", mParams.isElite
                , "pubType", mParams.pubType
                , "shareStatus", mParams.status
                , "pageSize", mParams.pageSize
                , "lastFoodNoteId", mParams.lastFoodNoteId);
    }

    /**
     * 美食记录显示空页面
     */
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
                intent.putExtra("left_tab", 100);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BrodcastActions.removeListtener(mFeedChangeListener);
    }

    public static MyFoodListFragment newInstance() {
        MyFoodListFragment tabFragment = new MyFoodListFragment();
        return tabFragment;
    }


}
