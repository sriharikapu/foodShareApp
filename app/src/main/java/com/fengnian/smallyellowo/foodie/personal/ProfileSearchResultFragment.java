package com.fengnian.smallyellowo.foodie.personal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.emoji.EmojiUtils;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;


public class ProfileSearchResultFragment extends BaseFragment {
    public static final String[] TITLES = {"美食记录", "想吃清单"};
    private ProfileCenterSearchHelper mHelper;
    private FoodFilterHelper mFoodFilterHelper;
    private MyFoodSearchListFragment mMyFoodSearchListFragment;
    private WantFoodSearchListFragment mWantFoodSearchListFragment;
    public ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private EditText mEditSearch;
    private BroadcastReceiver mBroadcastReceiver;
    private ImageView mEmptyView;
    private boolean isSearch = false;

    private PopupWindow.OnDismissListener mDismissListener = new PopupWindow.OnDismissListener() {
        public void onDismiss() {
            isSearch = false;
            mFoodFilterHelper.onDismissListener();
            mFoodFilterHelper.setFilterViewSelected(null);
            setShadowBg(false);
            if (mHelper.getSelectedTab() == MainUserInfoFragmentHelper.TAB_LEFT) {
                mMyFoodSearchListFragment.onPopupWindowDismissEvent(mFoodFilterHelper.buildProfileCenterLeftParams());
            } else {
                mWantFoodSearchListFragment.onPopupWindowDismissEvent(mFoodFilterHelper.buildProfileCenterRightParams());
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBroadcast();
        mHelper = new ProfileCenterSearchHelper(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    public void onTabSelected(int index) {
        setEmptyView();
        mFoodFilterHelper.setFilterTitle();
    }

    @Override
    public void onFindView() {
        initViews();
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_search_result_layout, container, false);
    }

    private void initViews() {
        findViewById(R.id.iv_back).setVisibility(View.GONE);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mEditSearch = (EditText) findViewById(R.id.et_input);
        mEmptyView = findView(R.id.empty_view);
        mAdapter = new MyFragmentPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setCurrentItem(MainUserInfoFragmentHelper.TAB_LEFT);
        mMyFoodSearchListFragment = (MyFoodSearchListFragment) mAdapter.getItem(MainUserInfoFragmentHelper.TAB_LEFT);
        mWantFoodSearchListFragment = (WantFoodSearchListFragment) mAdapter.getItem(MainUserInfoFragmentHelper.TAB_RIGHT);

        mHelper.initTabView();
        mFoodFilterHelper = new FoodFilterHelper(this, FoodFilterHelper.PROFILE_SEARCH);
        mFoodFilterHelper.setDismissListener(mDismissListener);
        mFoodFilterHelper.initFilterLayout();
        mFoodFilterHelper.setFilterTitleKeyWord(getKeyWord());

        //空页面
        mEmptyView.setVisibility(View.GONE);
        mEmptyView.setClickable(true);
        mEmptyView.setBackgroundColor(getResources().getColor(R.color.normal_bg));
        RelativeLayout.LayoutParams emptyParams = (RelativeLayout.LayoutParams) mEmptyView.getLayoutParams();
        emptyParams.topMargin = getResources().getDimensionPixelSize(R.dimen.user_tab_height)
                + getResources().getDimensionPixelSize(R.dimen.user_tab_filter_item_height)
                + DisplayUtil.dip2px(8f);
        mEmptyView.setImageResource(R.drawable.food_list_filter_empty);
        mEmptyView.setLayoutParams(emptyParams);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mEditSearch.getLayoutParams();
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.padding_middle);
        mEditSearch.setLayoutParams(params);
        mEditSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                }
                return false;
            }
        });

        mEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search();
            }
        });

        mFoodFilterHelper.setOnTitleClickListener(new FoodFilterHelper.onTitleClickListener() {
            @Override
            public void onClick() {
                EmojiUtils.hideKeyboard(getActivity(), mEditSearch);
                mMyFoodSearchListFragment.setKeyword(null);
                mWantFoodSearchListFragment.setKeyword(null);
            }
        });
    }

    private void registerBroadcast() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(MyActions.PROFILE_SEARCH_FINISH)) {
                    int tabIndex = intent.getIntExtra("tab_index", -1);
                    int count = intent.getIntExtra("count", -1);

                    if (tabIndex == MainUserInfoFragmentHelper.TAB_LEFT) {
                        if (count >= 0) {
                            mHelper.setMyFoodTabTitle(TITLES[0] + " " + count);
                        }
                    } else if (tabIndex == MainUserInfoFragmentHelper.TAB_RIGHT) {
                        if (count >= 0) {
                            mHelper.setWantEatTabTitle(TITLES[1] + " " + count);
                        }
                    }

                    //显示空页面
                    setEmptyView();

                    if (isSearch && mHelper.isHideKeyboard(ProfileSearchResultFragment.this, mEditSearch)) {
                        EmojiUtils.openKeyboard(getActivity(), mEditSearch);
                    }
                }
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver,
                new IntentFilter(MyActions.PROFILE_SEARCH_FINISH));
    }

    private void unregisterReceiver() {
        if (mBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
        }
    }

    private void search() {
        if (!FFUtils.checkNet()) {
            showToast(getString(R.string.lsq_network_connection_interruption));
            return;
        }

        isSearch = true;
        String keyWord = mEditSearch.getText().toString().trim();
        EmojiUtils.hideKeyboard(getActivity(), mEditSearch);
        mMyFoodSearchListFragment.setKeyword(keyWord);
        mMyFoodSearchListFragment.getData(true, false);

        mWantFoodSearchListFragment.setKeyword(keyWord);
        mWantFoodSearchListFragment.getData(true, false);
    }

    public void setShadowBg(boolean isAdd) {
        mHelper.setShadowBg(isAdd);
    }

    public int getSelectedTab() {
        return mHelper.getSelectedTab();
    }

    public void setEmptyView() {
        if (getSelectedTab() == MainUserInfoFragmentHelper.TAB_LEFT
                && mMyFoodSearchListFragment != null
                && mMyFoodSearchListFragment.mAdapter != null
                && mMyFoodSearchListFragment.mAdapter.getItemCount() <= 0) {
            mEmptyView.setImageResource(R.drawable.food_list_search_empty);
            mEmptyView.setVisibility(View.VISIBLE);
        } else if (getSelectedTab() == MainUserInfoFragmentHelper.TAB_RIGHT
                && mWantFoodSearchListFragment != null
                && mWantFoodSearchListFragment.mAdapter != null
                && mWantFoodSearchListFragment.mAdapter.getItemCount() <= 0) {
            mEmptyView.setImageResource(R.drawable.want_eat_list_search_empty);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    private String getKeyWord() {
        if (getActivity() != null && getActivity().getIntent() != null) {
            Bundle bundle = getActivity().getIntent().getExtras();
            if (bundle != null && bundle.containsKey("keyWord")) {
                return bundle.getString("keyWord");
            }
        }
        return null;
    }

    public static final class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        BaseFragment[] mFragList = new BaseFragment[TITLES.length];

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragList[MainUserInfoFragmentHelper.TAB_LEFT] = MyFoodSearchListFragment.newInstance();
            mFragList[MainUserInfoFragmentHelper.TAB_RIGHT] = WantFoodSearchListFragment.newInstance();
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
