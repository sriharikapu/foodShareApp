/*
 * @author http://blog.csdn.net/singwhatiwanna
 */
package com.fengnian.smallyellowo.foodie.homepage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.fan.framework.config.Value;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.NoticeActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.SelectCityActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appbase.CityPref;
import com.fengnian.smallyellowo.foodie.appbase.TitleIndicator;
import com.fengnian.smallyellowo.foodie.datamanager.SYDataManager;
import com.fengnian.smallyellowo.foodie.fragments.HomeChildDynamicFrag;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.personal.MyActions;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.LocationHelper;
import com.fengnian.smallyellowo.foodie.utils.ReplaceViewHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainMyHomeFragment extends BaseFragment implements OnPageChangeListener {
    public static final int TAB_0_SELECT = 0;//精选
    public static final int TAB_1_FOLLOW = 1;//关注
    public static final int TAB_2_MEET = 2;//活动
    public static final int TAB_3_NEARBY = 3;//附近

    private final String TAB_0_TEXT = "  精选  ";
    private final String TAB_1_TEXT = "  关注  ";
    private final String TAB_2_TEXT = "  活动  ";
    private final String TAB_3_TEXT = "  附近  ";

    private final int REQUEST_LOCATION = 1001;

    private List<String> TAB_TITLES = new ArrayList<>();
    private int mCurrentTab = TAB_0_SELECT;
    private ArrayList<TabInfo> mTabs = new ArrayList<>();
    private MyFragmentAdapter mFragmentAdapter = null;
    private ViewPager mPager;
    private TitleIndicator mIndicator;
    private BroadcastReceiver mBroadcastReceiver;
    private LocationHelper mLocationHelper;
    private TextView mMsgNumber;
    private TextView tv_city;
    private View mNoNetNoticeView;
    private View mSanlitunTabView;
    private TabInfo mSanlitunTabInfo;
    private boolean isSwitchedCity = false;

    public HomeChildSelectedFrag mHomeChildSelectedFrag;
    public HomeChildDynamicFrag mHomeChildDynamicFrag;
    public HomeChildMeetFrag mHomeChildMeetFrag;
    public HomeChildNearbyFrag mHomeChildNearbyFrag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBroadcast();
        mLocationHelper = new LocationHelper(getActivity());
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_home2, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isHidden()) {
            if (FFUtils.checkNet()) {
                mNoNetNoticeView.setVisibility(View.GONE);
            } else {
                mNoNetNoticeView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
        mLocationHelper.stopLocation();
    }

    public void onFindView() {
        mIndicator = getView(R.id.pagerindicator);
        initTabs();
        mPager = getView(R.id.pager);
        mNoNetNoticeView = findViewById(R.id.tv_fail);
        tv_city = (TextView) findViewById(R.id.tv_city);
        mFragmentAdapter = new MyFragmentAdapter(context(), getChildFragmentManager(), mTabs);
        mPager.setAdapter(mFragmentAdapter);
        mPager.setOnPageChangeListener(this);
        mPager.setOffscreenPageLimit(mTabs.size());

        mHomeChildSelectedFrag = (HomeChildSelectedFrag) mFragmentAdapter.getItem(TAB_0_SELECT);
        mHomeChildDynamicFrag = (HomeChildDynamicFrag) mFragmentAdapter.getItem(TAB_1_FOLLOW);
        mHomeChildMeetFrag = (HomeChildMeetFrag) mFragmentAdapter.getItem(TAB_2_MEET);

        mMsgNumber = getView(R.id.msg_num);
        mPager.setCurrentItem(mCurrentTab);
        mMsgNumber.setVisibility(View.GONE);

        initOnClickListener();
        rebuildAdapter();

        if (!LocationHelper.hasLocationPermission(getActivity())) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
        } else {
            startLocation();
        }
    }

    /**
     * 重新构建fragment Adapter
     */
    public void rebuildAdapter() {
        tv_city.setText(CityPref.getSelectedCity().getAreaName());
        if (isShowSanlitunTab()) {
            //容错，此方法在未知的某种快速操作下会调用多次：所以移除最后一个三里屯tab再进行add
            if (TAB_TITLES.size() > 3) {
                Iterator<String> iter = TAB_TITLES.iterator();
                while (iter.hasNext()) {
                    String value = iter.next();
                    if (value.equals(TAB_3_TEXT)) {
                        iter.remove();
                    }
                }
            }
            if (mTabs.size() > 3) {
                Iterator<TabInfo> iter = mTabs.iterator();
                while (iter.hasNext()) {
                    TabInfo value = iter.next();
                    if (mSanlitunTabInfo != null && value == mSanlitunTabInfo) {
                        iter.remove();
                    }
                }
            }

            TAB_TITLES.add(TAB_3_TEXT);
            if (mSanlitunTabInfo == null) {
                mSanlitunTabInfo = new TabInfo(TAB_3_NEARBY, TAB_TITLES.get(TAB_3_NEARBY), HomeChildNearbyFrag.class);
            }
            mTabs.add(mSanlitunTabInfo);
            mIndicator.init(mCurrentTab, mTabs, mPager);
            mFragmentAdapter.notifyDataSetChanged();
            mHomeChildNearbyFrag = (HomeChildNearbyFrag) mFragmentAdapter.getItem(TAB_3_NEARBY);
            mSanlitunTabView = mIndicator.getTabViewByIndex(TAB_3_NEARBY);
            showSanlitunIcon();
        } else {
            TAB_TITLES.remove(TAB_3_TEXT);
            if (mSanlitunTabInfo != null) {
                mTabs.remove(mSanlitunTabInfo);
            }
            mIndicator.init(mCurrentTab, mTabs, mPager);
            if (isSwitchedCity) {
                mFragmentAdapter.notifyDataSetChanged();
            }
        }

        //导航条的点击切换事件
        mIndicator.setCurrentTagCheckedListener(new TitleIndicator.OnCurrentTagCheckedListener() {
            @Override
            public void onCheck(int position, boolean showProgressDialog) {
                if (position == TAB_0_SELECT) {
                    mHomeChildSelectedFrag.setAutoRefresh();
                    mHomeChildSelectedFrag.setDislikeIconHide();
                } else if (position == TAB_1_FOLLOW) {
                    mHomeChildDynamicFrag.setAutoRefresh();
                } else if (position == TAB_2_MEET) {
                    mHomeChildMeetFrag.setAutoRefresh();
                } else if (position == TAB_3_NEARBY) {
                    if (mHomeChildNearbyFrag != null) {
                        mHomeChildNearbyFrag.setAutoRefresh();
                    }
                }
            }
        });
    }

    private void initOnClickListener() {
        //消息盒子
        findViewById(R.id.msg_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FFUtils.checkNet()) {
                    showToast(getString(R.string.lsq_network_connection_interruption));
                    return;
                }
                startActivity(NoticeActivity.class, new IntentData());
            }
        });

        tv_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentData intentData = new IntentData();
                startActivity(SelectCityActivity.class, intentData);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {// 允许了
                    startLocation();
                } else {
                    showToast("未获得定位权限，请打开系统设置并允许定位", null);
                }
                return;
            }
        }
    }


    private void startLocation() {
        mLocationHelper.startLocation(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if (getDestroyed() || isDetached() || getActivity().isFinishing()) {
                    mLocationHelper.stopLocation();
                    return;
                }
                if (location == null) {
                    return;
                }

                Value.mLatitude = location.getLatitude();
                Value.mLongitude = location.getLongitude();
                mLocationHelper.stopLocation();
                if (mHomeChildNearbyFrag != null) {
                    mHomeChildNearbyFrag.getData(true);
                }
            }
        });
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mIndicator.onScrolled((mPager.getWidth() + mPager.getPageMargin()) * position + positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        mIndicator.onSwitched(position);
        mCurrentTab = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {

        }
    }

    /**
     * 跳转到任意选项卡
     */
    public void setCurrentTab(int tabId) {
        for (int index = 0, count = mTabs.size(); index < count; index++) {
            if (mTabs.get(index).getId() == tabId) {
                mPager.setCurrentItem(index);
            }
        }
    }


    /**
     * 在这里提供要显示的选项卡数据
     */
    protected void initTabs() {
        TAB_TITLES.clear();
        TAB_TITLES.add(TAB_0_TEXT);
        TAB_TITLES.add(TAB_1_TEXT);
        TAB_TITLES.add(TAB_2_TEXT);

        mTabs.clear();
        mTabs.add(new TabInfo(TAB_0_SELECT, TAB_TITLES.get(TAB_0_SELECT), HomeChildSelectedFrag.class));
        mTabs.add(new TabInfo(TAB_1_FOLLOW, TAB_TITLES.get(TAB_1_FOLLOW), HomeChildDynamicFrag.class));
        mTabs.add(new TabInfo(TAB_2_MEET, TAB_TITLES.get(TAB_2_MEET), HomeChildMeetFrag.class));
    }

    //处理最后一个tab显示为三里屯图标
    private void showSanlitunIcon() {
        if (isShowSanlitunTab() && mSanlitunTabView != null) {
            TextView tabTitle = (TextView) mSanlitunTabView.findViewById(R.id.tab_title1);
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(R.drawable.home_sanlitun_icon);
            ReplaceViewHelper replaceViewHelper = new ReplaceViewHelper(getActivity());
            replaceViewHelper.toReplaceView(tabTitle, imageView);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
            params.bottomMargin = DisplayUtil.dip2px(7f);
            imageView.setLayoutParams(params);
        }
    }

    private boolean isShowSanlitunTab() {
        return CityPref.isBeiJing();
    }

    private void registerBroadcast() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(MyActions.HomeFragAction.ACTION_HOME_FRAG)) {
                    if ("type_dynamic_num".equals(intent.getStringExtra("type"))) {

                        int dynamicNum = intent.getIntExtra(MyActions.HomeFragAction.KEY_DYNAMIC_NUM, -1);
                        int messageNum = intent.getIntExtra(MyActions.HomeFragAction.KEY_MSG_NUM, -1);

                        boolean isShowTips = dynamicNum > 0 || messageNum > 0 || !SYDataManager.shareDataManager().getAllFailDynamic().isEmpty();
                        if (mIndicator != null) {
                            mIndicator.updateChildTips(1, isShowTips);
                        }
                        mHomeChildDynamicFrag.setMessageNum(dynamicNum);
                    } else if ("type_notice_num".equals(intent.getStringExtra("type"))) {
                        int noticeNum = intent.getIntExtra(MyActions.HomeFragAction.KEY_NOTICE_NUM, -1);
                        if (noticeNum > 0) {
                            mMsgNumber.setVisibility(View.VISIBLE);
                            if (noticeNum < 10) {
                                mMsgNumber.setText(String.valueOf(noticeNum));
                            } else if (noticeNum >= 10) {
                                mMsgNumber.setText("…");
                            }
                        } else {
                            mMsgNumber.setVisibility(View.GONE);
                        }
                    } else if ("type_to_dynamic_top".equals(intent.getStringExtra("type"))) {
                        mPager.setCurrentItem(TAB_1_FOLLOW, false);
                        mHomeChildDynamicFrag.toTop();
                    }
                } else if (intent.getAction().equals(MyActions.ACTION_NO_NET)) {
                    if ("type_net".equals(intent.getStringExtra("type"))) {
                        boolean isConnect = intent.getBooleanExtra("isConnect", false);
                        if (mNoNetNoticeView != null) {
                            if (isConnect) {
                                mNoNetNoticeView.setVisibility(View.GONE);
                            } else {
                                mNoNetNoticeView.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else if (intent.getAction().equals(MyActions.ACTION_TAB_SELECTED)) {
                    int index = intent.getIntExtra("tab_index", -1);
                    if (index != 0) {
                        mHomeChildSelectedFrag.setDislikeIconHide();
                    }
                } else if (intent.getAction().equals(MyActions.ACTION_HOME_RECOMMEND)) {
                    mHomeChildSelectedFrag.recommendFinished();
                }
                if (intent.getAction().equals(IUrlUtils.Constans.ACTIION_SELECT_CITY)) {
                    isSwitchedCity = true;
                    rebuildAdapter();
                    mHomeChildSelectedFrag.getData(true);
                    mHomeChildDynamicFrag.refresh(true);
                    mHomeChildMeetFrag.getData(true);
                    if (mHomeChildNearbyFrag != null && isShowSanlitunTab()) {
                        mHomeChildNearbyFrag.getData(true);
                    }
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyActions.HomeFragAction.ACTION_HOME_FRAG);
        intentFilter.addAction(MyActions.ACTION_NO_NET);
        intentFilter.addAction(MyActions.ACTION_TAB_SELECTED);
        intentFilter.addAction(MyActions.ACTION_HOME_RECOMMEND);
        intentFilter.addAction(IUrlUtils.Constans.ACTIION_SELECT_CITY);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void unregisterReceiver() {
        if (mBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
        }
    }

    public static class MyFragmentAdapter extends FragmentPagerAdapter {
        ArrayList<TabInfo> tabs = null;
        Context context = null;

        public MyFragmentAdapter(Context context, FragmentManager fm, ArrayList<TabInfo> tabs) {
            super(fm);
            this.tabs = tabs;
            this.context = context;
        }

        @Override
        public Fragment getItem(int pos) {
            Fragment fragment = null;
            if (tabs != null && pos < tabs.size()) {
                TabInfo tab = tabs.get(pos);
                if (tab == null)
                    return null;
                fragment = tab.createFragment();
            }
            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            if (tabs != null && tabs.size() > 0)
                return tabs.size();
            return 0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabInfo tab = tabs.get(position);
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            tab.fragment = fragment;
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }

}
