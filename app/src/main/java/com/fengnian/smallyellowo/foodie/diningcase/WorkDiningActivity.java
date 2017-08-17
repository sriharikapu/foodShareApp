package com.fengnian.smallyellowo.foodie.diningcase;

import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.fan.framework.config.Tool;
import com.fan.framework.config.Value;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.widgets.PullToRefreshLayout;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.RestSearchResultActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.CityPref;
import com.fengnian.smallyellowo.foodie.appbase.PreferJsonData;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFindMerchantInfo;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.bean.results.SYShopLocationResult;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.personal.MyActions;
import com.fengnian.smallyellowo.foodie.scoreshop.OnFinishListener;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.LocationHelper;

import java.util.HashMap;
import java.util.List;

import static com.fan.framework.widgets.PullToRefreshLayout.supportPull;

/**
 * Created by chenglin on 2017-6-7.
 */

public class WorkDiningActivity extends BaseActivity<IntentData> implements View.OnClickListener {
    private final int REQUEST_LOCATION = 1001;
    private WorkDiningHelper mHelper;
    public ListView mListView;
    public PullToRefreshLayout prl;
    public WorkDiningHeaderViewPager mViewPager;
    private BroadcastReceiver mBroadcastReceiver;
    private ValueAnimator mHeaderAnimator;
    public LinearLayout mHeaderView;
    public WorkDiningAdapter mAdapter;
    public LinearLayout mListTitle;
    private View title2;
    private View s_status_bar;
    private LocationHelper mLocationHelper;
    private DiningNearbyHelper mNearbyHelper;
    private int mHeaderHeight = -1;
    private int mDistance = -1;
    private int mTitleHeight = -1;
    private String mShopId = "0";
    public boolean isRequestedNet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_dining_layout);
        setNotitle(true);
        registerBroadcast();

        mHelper = new WorkDiningHelper(this);
        mLocationHelper = new LocationHelper(this);
        mListView = findView(R.id.list_view);
        mListTitle = findView(R.id.list_title);
        s_status_bar = findView(R.id.s_status_bar);

        mListTitle.setAlpha(0f);
        mListTitle.findViewById(R.id.title_2).setVisibility(View.GONE);
        mListTitle.findViewById(R.id.my_back_view).setOnClickListener(this);
        mListTitle.findViewById(R.id.dining_add).setOnClickListener(this);
        mListTitle.findViewById(R.id.dining_get_item).setOnClickListener(this);
        mListTitle.findViewById(R.id.dining_share).setOnClickListener(this);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) s_status_bar.getLayoutParams();
        params.height = FFUtils.getStatusbarHight(this);
        s_status_bar.setLayoutParams(params);
        s_status_bar.setBackgroundColor(Color.BLACK);

        mHeaderView = (LinearLayout) View.inflate(this, R.layout.work_dining_header, null);
        mHeaderView.findViewById(R.id.list_title).findViewById(R.id.title_1).setVisibility(View.GONE);
        mHeaderView.findViewById(R.id.line2).setVisibility(View.GONE);
        View headerLine = mHeaderView.findViewById(R.id.list_title).findViewById(R.id.s_status_bar);
        headerLine.getLayoutParams().height = DisplayUtil.dip2px(5f);

        mNearbyHelper = new DiningNearbyHelper(this, mHeaderView);
        mViewPager = (WorkDiningHeaderViewPager) mHeaderView.findViewById(R.id.view_pager);
        mListView.addHeaderView(mHeaderView);
        mViewPager.init();

        mTitleHeight = getResources().getDimensionPixelSize(R.dimen.ff_actionbarHight)
                + getResources().getDimensionPixelSize(R.dimen.dining_share_title2_height)
                + FFUtils.getStatusbarHight(this);

        mHeaderView.findViewById(R.id.iv_back_2).setOnClickListener(this);
        mHeaderView.findViewById(R.id.list_title).findViewById(R.id.dining_add).setOnClickListener(this);
        mHeaderView.findViewById(R.id.list_title).findViewById(R.id.dining_get_item).setOnClickListener(this);
        mHeaderView.findViewById(R.id.list_title).findViewById(R.id.dining_share).setOnClickListener(this);

        mAdapter = new WorkDiningAdapter(this);
        mListView.setAdapter(mAdapter);
        setScrollListener();
        requestLocationPermission();

        prl = supportPull(mListView, new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                getMyDiningList(true);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                getMyDiningList(false);
            }
        });
        prl.setDoPullUp(true);
        prl.setDoPullDown(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mNearbyHelper.isShowingLocationView() && LocationHelper.hasLocationPermission(this)) {
            mNearbyHelper.removeRequestLocationImage();
            startLocation();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
        mLocationHelper.stopLocation();
    }

    private void getMyDiningList(final boolean isRefresh) {
        getMyDiningList(isRefresh, true);
    }

    private void getMyDiningList(final boolean isRefresh, boolean isShowProgress) {
        String showText = null;
        if (isShowProgress) {
            showText = "";
        }

        if (isRefresh) {
            mShopId = "0";
        }

        //获取经纬度
        String longitude = Value.mLongitude + "";
        String latitude = Value.mLatitude + "";
        if (mNearbyHelper.getGDPoiModel() != null) {
            if (!TextUtils.isEmpty(mNearbyHelper.getGDPoiModel().location)) {
                String[] loc = mNearbyHelper.getGDPoiModel().location.split(",");
                if (loc.length == 2) {
                    longitude = loc[0];
                    latitude = loc[1];
                }
            }
        }

//        post(Constants.shareConstants().getNetHeaderAdress() + "/weekdayLunch/getUserWeekdayLunchList.do",
        post(IUrlUtils.WorkDining.getUserWeekdayLunchList,
                showText, null, new FFNetWorkCallBack<MyDiningList>() {
                    @Override
                    public void onSuccess(MyDiningList response, FFExtraParams extra) {
                        isRequestedNet = true;
                        if (response != null && response.data != null) {
                            if (isRefresh) {
                                mAdapter.setDataList(response.data);
                                prl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            } else {
                                mAdapter.appendDataList(response.data);
                            }

                            if (response.data != null && response.data.size() > 0) {
                                mShopId = response.data.get(response.data.size() - 1).getMerchantUid();
                            }

                            if (response.data.size() > 0) {
                                prl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                            } else {
                                prl.loadmoreFinish(PullToRefreshLayout.NO_DATA_SUCCEED);
                            }

                            mHelper.setMyDiningListPageState();
                            mHelper.setAllEmptyView();
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        prl.loadmoreFinish(PullToRefreshLayout.FAIL);
                        prl.refreshFinish(PullToRefreshLayout.FAIL);
                        return false;
                    }
                }, "openGPS", "0"
                , "shopId", mShopId
                , "pageSize", 20
                , "longitude", longitude
                , "latitude", latitude);
    }

    public void addToMyDiningList(final String shopId) {
        addToMyDiningList(shopId, null);
    }

    public void addToMyDiningList(final String shopId, final OnFinishListener listener) {
//        post(Constants.shareConstants().getNetHeaderAdress() + "/weekdayLunch/addShopToWeekdayLunch.do",
        post(IUrlUtils.WorkDining.addShopToWeekdayLunch,
                "", null, new FFNetWorkCallBack<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult response, FFExtraParams extra) {
                        if (response != null && response.getErrorCode() == 0) {
                            if (listener != null) {
                                listener.onFinish(null);
                            }
                            showToast("添加成功");
                            getMyDiningList(true, true);
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "shopId", shopId);
    }

    private void setScrollListener() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mHeaderHeight <= 0) {
                    mHeaderHeight = mHeaderView.getHeight();
                }

                if (firstVisibleItem == 0) {
                    if (mHeaderHeight > 0 && mHeaderView.getHeight() > 100) {
                        if (mDistance <= 0) {
                            mDistance = mHeaderHeight - mTitleHeight;
                        }

                        int offset = 0;
                        View firstItem = mListView.getChildAt(0);
                        if (firstItem != null) {
                            offset = 0 - firstItem.getTop();
                        }

                        float percent = (offset * 1f) / (mDistance * 1f);
                        if (percent >= 0 && percent <= 1) {
                            mListTitle.setAlpha(percent);
                            mListTitle.findViewById(R.id.title_2).setVisibility(View.GONE);
                        } else if (percent >= 1) {
                            mListTitle.findViewById(R.id.title_2).setVisibility(View.VISIBLE);
                        }
                    }
                }

                if (firstVisibleItem >= 1) {
                    if (mAdapter.isToTop()) {
                        return;
                    } else if (mHeaderAnimator != null && mHeaderAnimator.isRunning()) {
                        return;
                    }
                    hideListViewHeader();
                }

            }
        });
    }

    public void hideListViewHeader() {
        if (mListView.getPaddingTop() <= 0) {
            mListView.setPadding(0, mTitleHeight, 0, 0);
            mAdapter.setToTop(true);
            RelativeLayout headerRoot = (RelativeLayout) mHeaderView.findViewById(R.id.head_view);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) headerRoot.getLayoutParams();
            params.height = 0;
            headerRoot.setLayoutParams(params);
        }
    }

    public void showListViewHeader(int position) {
        mHelper.stopListViewScroll(mListView);
        mListView.setSelection(position);

        mListTitle.findViewById(R.id.title_2).setVisibility(View.GONE);
        mListTitle.setAlpha(0f);
        mListView.setPadding(0, 0, 0, 0);

        mHeaderAnimator = ValueAnimator.ofInt(0, mHeaderHeight);
        mHeaderAnimator.setDuration(300);
        mHeaderAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                RelativeLayout headerRoot = (RelativeLayout) mHeaderView.findViewById(R.id.head_view);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) headerRoot.getLayoutParams();
                params.height = value;

                if (value == mHeaderHeight) {
                    mAdapter.setToTop(false);
                    params.height = -1;
                }
                headerRoot.setLayoutParams(params);
            }
        });

        mHeaderAnimator.start();
    }


    public void setShareStateStart() {
        if (mAdapter.getCount() <= 1) {
            return;
        }
        if (title2 == null) {
            mListTitle.setAlpha(1f);
            mListTitle.findViewById(R.id.title_1).setVisibility(View.GONE);
            mListTitle.findViewById(R.id.title_2).setVisibility(View.GONE);
            title2 = View.inflate(this, R.layout.work_dining_title_2, null);
            mListTitle.addView(title2);
            mAdapter.setShareState(true);
            hideListViewHeader();

            FFUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mListView.setSelection(0);
                }
            }, 100);

            //取消
            title2.findViewById(R.id.my_back_view_2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAdapter.isShareState()) {
                        setShareStateEnd();
                    }
                }
            });

            //全选--取消全选
            final TextView selectAll = (TextView) title2.findViewById(R.id.tv_select_all);
            selectAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectAll.getText().toString().equals(getString(R.string.select_all))) {
                        mAdapter.setSelectAll(true);
                        setSelectAllText(R.string.un_select_all);
                    } else if (selectAll.getText().toString().equals(getString(R.string.un_select_all))) {
                        mAdapter.setSelectAll(false);
                        setSelectAllText(R.string.select_all);
                    }
                }
            });

            //分享
            TextView shareTv = (TextView) title2.findViewById(R.id.my_share);
            shareTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAdapter.getCheckedList().size() <= 1) {
                        showToast("至少选择两个哦~");
                    } else {
                        mHelper.getVoteUrl();
                    }
                }
            });
        }
    }

    public void setShareStateEnd() {
        if (title2 != null) {
            mAdapter.setShareState(false);
            mAdapter.setSelectAll(false);
            mListTitle.findViewById(R.id.title_1).setVisibility(View.VISIBLE);
            mListTitle.findViewById(R.id.title_2).setVisibility(View.VISIBLE);
            mListTitle.removeView(title2);
            title2 = null;
        }
    }

    public void setSelectAllText(int res_id) {
        if (title2 != null) {
            TextView selectAll = (TextView) title2.findViewById(R.id.tv_select_all);
            selectAll.setText(res_id);
        }
    }

    public void setAllEmptyView() {
        mHelper.setAllEmptyView();
    }

    public void setMyDiningListPageState() {
        mHelper.setMyDiningListPageState();
    }

    private void startLocation() {
        mNearbyHelper.removeRequestLocationImage();
        mHeaderView.findViewById(R.id.current_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNearbyHelper.setGDPoiModel(null);
                PreferJsonData.setDiningLocation("");
                launchBDLocation(null);
            }
        });

        //产品Simon要求工作日午餐的定位特殊处理，如果用户手动选择过就不再定位。2017年7月5日
        String json = PreferJsonData.getDiningLocation();
        if (!TextUtils.isEmpty(json)) {
            getCacheLocation();
            getMyDiningList(true);
        } else {
            launchBDLocation(new OnFinishListener() {
                @Override
                public void onFinish(Object object) {
                    getMyDiningList(true);
                }
            });
        }
    }

    /**
     * 用户选择的过的位置
     */
    private void getCacheLocation() {
        String json = PreferJsonData.getDiningLocation();
        GDPoiModel gdPoiModel = JSON.parseObject(json, GDPoiModel.class);
        if (gdPoiModel != null) {
            mNearbyHelper.setGDPoiModel(gdPoiModel);
            mNearbyHelper.setAddress(gdPoiModel.name, gdPoiModel.isCurrentLocation);
        }
    }

    /**
     * 启动百度定位
     */
    private void launchBDLocation(final OnFinishListener listener) {
        if (mLocationHelper.isStarted()) {
            return;
        }
        mNearbyHelper.setAddress(getString(R.string.location_ing), false);
        mLocationHelper.startLocation(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if (isFinishing() || getDestroyed()){
                    mLocationHelper.stopLocation();
                    return;
                }
                if (location == null) {
                    return;
                }
                Value.mLatitude = location.getLatitude();
                Value.mLongitude = location.getLongitude();
                mLocationHelper.stopLocation();
                mNearbyHelper.setAddress(location);
                if (listener != null) {
                    listener.onFinish(null);
                }
            }
        });
    }

    private void requestLocationPermission() {
        if (!LocationHelper.hasLocationPermission(this)) {
            ActivityCompat.requestPermissions(WorkDiningActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
            mNearbyHelper.addRequestLocationImage(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FFUtils.openAppDetailSetting(WorkDiningActivity.this);
                }
            });

        } else {
            startLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {// 允许了
                    startLocation();
                } else {
                    mNearbyHelper.addRequestLocationImage(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FFUtils.openAppDetailSetting(WorkDiningActivity.this);
                        }
                    });
                    showToast("未获得定位权限，请打开系统设置并允许定位", null);
                }
                return;
            }
        }
    }


    private void registerBroadcast() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(MyActions.ACTION_NEARBY_BUSINESS_ITEM)) {
                    SYShopLocationResult.SYShopLocationModel item = (SYShopLocationResult.SYShopLocationModel) intent.getSerializableExtra("item");
                    if (item != null && !TextUtils.isEmpty(item.getId())) {
                        addToMyDiningList(item.getId());
                    }
                } else if (intent.getAction().equals(MyActions.ACTION_GD_POI)) {
                    GDPoiModel gDPoiModel = (GDPoiModel) intent.getSerializableExtra("item");
                    mNearbyHelper.setGDPoiModel(gDPoiModel);
                    mNearbyHelper.setAddress(gDPoiModel.name, gDPoiModel.isCurrentLocation);
                    PreferJsonData.setDiningLocation(JSON.toJSONString(gDPoiModel));
                    mViewPager.getRecommendList(gDPoiModel);
                    getMyDiningList(true);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyActions.ACTION_NEARBY_BUSINESS_ITEM);
        intentFilter.addAction(MyActions.ACTION_GD_POI);
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void unregisterReceiver() {
        if (mBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        }
    }

    public int getRecommendListCount() {
        if (mViewPager.getAdapter() != null && mViewPager.getAdapter().getCount() > 0) {
            return mViewPager.getAdapter().getCount();
        } else {
            return 0;
        }
    }

    public void removeViewPagerItem(SYFindMerchantInfo model){
        mViewPager.removeViewPagerItem(model);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.my_back_view) {
            if (mAdapter.isToTop()) {
                showListViewHeader(0);
            } else {
                finish();
            }
        } else if (v.getId() == R.id.iv_back_2) {
            finish();
        } else if (v.getId() == R.id.dining_add) {
            if (!FFUtils.checkNet()) {
                showToast(getString(R.string.lsq_network_connection_interruption));
                return;
            }

            pushEvent("Yellow_157", null);
            NearbyRestListActivity.start(WorkDiningActivity.this, Value.mLongitude, Value.mLatitude);
        } else if (v.getId() == R.id.dining_get_item) {

            pushEvent("Yellow_156", null);
            mHelper.getMyDiningListItem();
        } else if (v.getId() == R.id.dining_share) {
            pushEvent("Yellow_155", null);
            setShareStateStart();
        }
    }

    @Override
    public void onBackPressed() {
        if (mAdapter.isShareState()) {
            setShareStateEnd();
            return;
        } else if (mAdapter.isToTop()) {
            showListViewHeader(0);
            return;
        }

        super.onBackPressed();
    }

    private static final class MyDiningList extends BaseResult {
        public List<SYFindMerchantInfo> data;
    }

    public void pushEvent158(String shopIds){
        HashMap<String, String> event = new HashMap<String, String>();
        event.put("shop_id", shopIds);

        pushEvent("Yellow_158", event);
    }

    public void pushEvent154(String position, SYFindMerchantInfo info){
        /*HashMap<String, String> event = new HashMap<String, String>();
        event.put("shop_name", info.getMerchantName());
        event.put("shop_id", info.getMerchantUid());
        event.put("score", info.getStartLevel()+"");
        event.put("range", info.getMerchantDistance());
        event.put("price", info.getMerchantPrice()+"");
        event.put("position", position);
        event.put("from", this.getClass().getName());
        pushEvent("Yellow_154", event);*/

        pushEventDetail("Yellow_154", position,info);
    }

    public void pushEvent107(String position, SYFindMerchantInfo info){
        /*HashMap<String, String> event = new HashMap<String, String>();
        event.put("shop_name", info.getMerchantName());
        event.put("shop_id", info.getMerchantUid());
        event.put("score", info.getStartLevel()+"");
        event.put("position", position);
        event.put("range", info.getMerchantDistance());
        event.put("price", info.getMerchantPrice()+"");
        event.put("from", this.getClass().getName());*/

        pushEventDetail("Yellow_107", position,info);
    }

    private void pushEventDetail(String eventId, String position, SYFindMerchantInfo info){
        HashMap<String, String> event = new HashMap<String, String>();
        event.put("shop_name", info.getMerchantName());
        event.put("shop_id", info.getMerchantUid());
        event.put("score", info.getStartLevel()+"");
        event.put("position", position);
        event.put("range", info.getMerchantDistance());
        event.put("price", info.getMerchantPrice()+"");
        event.put("from", this.getClass().getName());

        pushEvent(eventId, event);
    }

    private void pushEvent(String eventId, HashMap<String, String> eventMap){
        if (eventMap == null){
            eventMap = new HashMap<>();
        }
        eventMap.put("account", SP.getUid());
        eventMap.put("channel", Tool.getChannelName(this));
        eventMap.put("city", CityPref.getSelectedCity().getAreaName());
        pushEventAction(eventId, eventMap);
    }
}
