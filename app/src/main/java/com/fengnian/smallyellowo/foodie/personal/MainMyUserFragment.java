package com.fengnian.smallyellowo.foodie.personal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.GrowthHistoryActivity;
import com.fengnian.smallyellowo.foodie.MainActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.View.StickyNavLayout;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.receivers.PushManager;
import com.fengnian.smallyellowo.foodie.scoreshop.ScoreDetailActivity;
import com.fengnian.smallyellowo.foodie.scoreshop.ScoreShopActivity;


public class MainMyUserFragment extends BaseFragment implements View.OnClickListener {
    public static final String[] TITLES = {"美食记录", "想吃清单"};
    private MainUserInfoFragmentHelper mHelper;
    public FoodFilterHelper mFoodFilterHelper;
    public MyFoodListFragment mMyFoodListFragment;
    public WantFoodListFragment mWantFoodListFragment;
    public ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    public StickyNavLayout mStickyNavLayout;
    private BroadcastReceiver mBroadcastReceiver;
    private MainUserModelClass.UserInfoResult infoResult;
    private View mNoNetView;

    private PopupWindow.OnDismissListener mDismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            mFoodFilterHelper.onDismissListener();
            mFoodFilterHelper.setFilterViewSelected(null);
            mFoodFilterHelper.setFilterBtnColor();
            setShadowBg(false);

            if (mHelper.getSelectedTab() == MainUserInfoFragmentHelper.TAB_LEFT) {
                mMyFoodListFragment.onPopupWindowDismissEvent(mFoodFilterHelper.buildProfileCenterLeftParams());
            } else {
                mWantFoodListFragment.onPopupWindowDismissEvent(mFoodFilterHelper.buildProfileCenterRightParams());
            }
            mFoodFilterHelper.setRestBtnVisible();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new MainUserInfoFragmentHelper(this);
        registerBroadcast();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isHidden()) {
            if (FFUtils.checkNet()) {
                mNoNetView.setVisibility(View.GONE);
            } else {
                mNoNetView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    @Override
    public void onFindView() {
        initViews();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHelper.onViewCreated();
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_profile_layout, container, false);
    }

    private void initViews() {
        findViewById(R.id.btn_edit).setOnClickListener(this);
        findViewById(R.id.profile_head_search).setOnClickListener(this);
        findViewById(R.id.profile_head_setting).setOnClickListener(this);
        findViewById(R.id.iv_avatar).setOnClickListener(this);
        findViewById(R.id.tv_name).setOnClickListener(this);
        findViewById(R.id.profile_head_food_record).setOnClickListener(this);
        findViewById(R.id.tv_score_title).setOnClickListener(this);
        findViewById(R.id.tv_score).setOnClickListener(this);
        findViewById(R.id.tv_score_shop).setOnClickListener(this);
        mNoNetView = findView(R.id.tv_no_net);

        //初始化隐藏某些View
        mNoNetView.setVisibility(View.GONE);
        findViewById(R.id.iv_add_crown).setVisibility(View.GONE);
        findViewById(R.id.tv_sex).setVisibility(View.GONE);
        findViewById(R.id.red_dot).setVisibility(View.GONE);

        mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        mStickyNavLayout = (StickyNavLayout) findViewById(R.id.stick_nav_layout);

        mAdapter = new MyFragmentPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setCurrentItem(MainUserInfoFragmentHelper.TAB_LEFT);
        mMyFoodListFragment = (MyFoodListFragment) mAdapter.getItem(MainUserInfoFragmentHelper.TAB_LEFT);
        mWantFoodListFragment = (WantFoodListFragment) mAdapter.getItem(MainUserInfoFragmentHelper.TAB_RIGHT);

        mHelper.initTabView();
        mFoodFilterHelper = new FoodFilterHelper(this, FoodFilterHelper.PROFILE_CENTER);
        mFoodFilterHelper.setDismissListener(mDismissListener);
        mFoodFilterHelper.initFilterLayout();
        getPersonData("");

        if (TextUtils.isEmpty(SP.getUser().getTel())){
            findViewById(R.id.no_phone).setVisibility(View.VISIBLE);
        }
//
//        String has_new = PushManager.getHasNewProgress();
//        if ("1".equals(has_new)) {
//            mHelper.setGrowthRedDot(true);
//        } else {
//            mHelper.setGrowthRedDot(false);
//        }
        findViewById(R.id.filter_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStickyNavLayout.scrollToTop(true);
            }
        });
    }

    private void getPersonData(String isShow) {
        if (!FFUtils.checkNet()) {
            showToast(getString(R.string.lsq_network_connection_interruption));
            return;
        } else if (!SP.isLogin()) {
            return;
        }

        post(Constants.shareConstants().getNetHeaderAdress() + "/user/getIndexPersonalInfoV250.do",
                isShow, null, new FFNetWorkCallBack<MainUserModelClass.UserInfoResult>() {
                    @Override
                    public void onSuccess(MainUserModelClass.UserInfoResult response, FFExtraParams extra) {
                        infoResult = response;
                        mHelper.setUserHeaderInfo(response);
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "targetAccount", SP.getUid(), "type", "1");
    }

    @Override
    public void onClick(View v) {
        if (!FFUtils.checkNet()) {
            showToast(getString(R.string.lsq_network_connection_interruption));
            return;
        }

        if (v.getId() == R.id.tv_score_title || v.getId() == R.id.tv_score) {
            ScoreDetailActivity.start((BaseActivity) getActivity());
        } else if (v.getId() == R.id.iv_avatar || v.getId() == R.id.tv_name) {
            if (infoResult != null) {
                mHelper.toUserPage((FFContext) getActivity(), infoResult.user);
            }
        }

        switch (v.getId()) {
            case R.id.btn_edit:
                if (infoResult != null) {
                    mHelper.toEditProfile(MainMyUserFragment.this, infoResult.user);
                }
                break;
            case R.id.profile_head_search:
                startActivity(ProfileSearchSuggestAct.class, new IntentData());
                break;
            case R.id.profile_head_setting:
                startActivity(SettingActivity.class, new IntentData());
                break;
            case R.id.profile_head_food_record:
                startActivity(GrowthHistoryActivity.class, new IntentData());
                break;
            case R.id.tv_score_shop:
                startActivity(ScoreShopActivity.class, new IntentData());
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mHelper.showOrHideFoodFragmentEmptyGuide();
        } else {
            mHelper.removeEmptyGuideView();
        }
    }

    public void onTabSelected(final int currentIndex) {
        mHelper.showOrHideFoodFragmentEmptyGuide();
        mHelper.hideOrShowFilterView(currentIndex);
        mFoodFilterHelper.setFilterTitle();
        mFoodFilterHelper.setFilterBtnColor();
    }

    /**
     * 重置搜索参数
     */
    public void resetFilterParams() {
        if (getSelectedTab() == MainUserInfoFragmentHelper.TAB_LEFT) {
            mMyFoodListFragment.resetFilterParams();
        } else if (getSelectedTab() == MainUserInfoFragmentHelper.TAB_RIGHT) {
            mWantFoodListFragment.resetFilterParams();
        }
    }

    private void registerBroadcast() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(MyActions.ACTION_NO_NET)) {
                    if ("type_net".equals(intent.getStringExtra("type"))) {
                        boolean isConnect = intent.getBooleanExtra("isConnect", false);
                        if (isConnect) {
                            mNoNetView.setVisibility(View.GONE);
                        } else {
                            mNoNetView.setVisibility(View.VISIBLE);
                        }
                    }
                } else if (intent.getAction().equals(MyActions.ACTION_TAB_SELECTED)) {
                    int tabIndex = intent.getIntExtra("tab_index", -1);
                    if (tabIndex == 3) {//个人tab
                        getPersonData(null);
                        mMyFoodListFragment.getData(true);
                        mWantFoodListFragment.getData(true);
                        mFoodFilterHelper.getFilterData();
                    }
                } else if (intent.getAction().equals(MyActions.PROFILE_CENTER_EMPTY)) {
                    mHelper.hideOrShowFilterView(getSelectedTab());

                    int leftTab = intent.getIntExtra("left_tab", -1);
                    int rightTab = intent.getIntExtra("right_tab", -1);
                    if (leftTab > 0) {
                        mHelper.showOrHideFoodFragmentEmptyGuide();
                    }
                } else if (intent.getAction().equals(MyActions.ACTION_PROFILE)) {
                    if ("type_has_new_growth".equals(intent.getStringExtra("type"))) {
                        //是否有新的里程消息,(1----有，""----没有)2.8.0版本加入的
                        String has_new = intent.getStringExtra("has_new");
                        if ("1".equals(has_new)) {
                            mHelper.setGrowthRedDot(true);
                        } else {
                            mHelper.setGrowthRedDot(false);
                        }
                    } else if ("type_is_show_want_food_filter".equals(intent.getStringExtra("type"))) {
                        if (getSelectedTab() == MainUserInfoFragmentHelper.TAB_RIGHT) {
                            mHelper.hideOrShowFilterView(MainUserInfoFragmentHelper.TAB_RIGHT);
                        }
                    } else if ("type_delete_item".equals(intent.getStringExtra("type"))) {
                        mFoodFilterHelper.getFilterData();
                    }
                } else if (intent.getAction().equals(MyActions.ACTION_UPDATE_USER_INFO)) {
                    getPersonData(null);
                } else if (intent.getAction().equals(MyActions.ACTION_UPDATE_SCORE)) {
                    double totalScore = intent.getFloatExtra("score", -100f);
                    if (totalScore != -100f) {
                        TextView tv_score = (TextView) findViewById(R.id.tv_score);
                        tv_score.setText("" + FFUtils.getSubFloat(totalScore, 1, true));
                    }
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyActions.ACTION_TAB_SELECTED);
        intentFilter.addAction(MyActions.PROFILE_CENTER_EMPTY);
        intentFilter.addAction(MyActions.ACTION_PROFILE);
        intentFilter.addAction(MyActions.ACTION_UPDATE_USER_INFO);
        intentFilter.addAction(MyActions.ACTION_NO_NET);
        intentFilter.addAction(MyActions.ACTION_UPDATE_SCORE);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void unregisterReceiver() {
        if (mBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
        }
    }

    public void setShadowBg(boolean isAdd) {
        mHelper.setShadowBg(isAdd);
    }

    public int getSelectedTab() {
        return mHelper.getSelectedTab();
    }

    /**
     * 判断当前是否执行过筛选操作
     */
    public boolean isFilterChanged() {
        boolean isFoodTypeChanged = false;
        boolean isFoodAreaChanged = false;

        if (getSelectedTab() == MainUserInfoFragmentHelper.TAB_LEFT) {
            MainUserModelClass.ProfileCenterLeftRequestParams newParams = new MainUserModelClass.ProfileCenterLeftRequestParams();
            if (!mMyFoodListFragment.mParams.ptype.equals(newParams.ptype)) {
                isFoodTypeChanged = true;
            }
            if (!mMyFoodListFragment.mParams.streetId.equals(newParams.streetId)) {
                isFoodAreaChanged = true;
            }
        } else if (getSelectedTab() == MainUserInfoFragmentHelper.TAB_RIGHT) {
            MainUserModelClass.ProfileCenterRightRequestParams newParams = new MainUserModelClass.ProfileCenterRightRequestParams();
            if (!mWantFoodListFragment.mParams.ptype.equals(newParams.ptype)) {
                isFoodTypeChanged = true;
            }
            if (!mWantFoodListFragment.mParams.streetId.equals(newParams.streetId)) {
                isFoodAreaChanged = true;
            }
        }

        if (isFoodTypeChanged || isFoodAreaChanged || !mFoodFilterHelper.isInitState()) {
            return true;
        } else {
            return false;
        }
    }

    public static final class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        BaseFragment[] mFragList = new BaseFragment[TITLES.length];

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragList[MainUserInfoFragmentHelper.TAB_LEFT] = MyFoodListFragment.newInstance();
            mFragList[MainUserInfoFragmentHelper.TAB_RIGHT] = WantFoodListFragment.newInstance();
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public BaseFragment getItem(int position) {
            return mFragList[position];
        }
    }
}
