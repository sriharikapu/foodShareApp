package com.fengnian.smallyellowo.foodie.diningcase;

import android.Manifest;
import android.animation.Animator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.fan.framework.config.Value;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.CityPref;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.SiftBean;
import com.fengnian.smallyellowo.foodie.bean.results.ConfigResult;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.personal.MyActions;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.LocationHelper;
import com.fengnian.smallyellowo.foodie.widgets.MyScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chenglin on 2017-6-13.
 */

public class MyselfDiningActivity extends BaseActivity<IntentData> implements View.OnClickListener {
    private final int REQUEST_LOCATION = 1001;
    private LocationHelper mLocationHelper;
    private DiningNearbyHelper mNearbyHelper;
    private MyScrollView mScrollView;
    private View mTopTitle;
    private DiningGridView mGridView1;
    private int mHeaderImageHeight = 0;
    private RelativeLayout mFragFrame;
    private BroadcastReceiver mBroadcastReceiver;
    private RecomRequestModel mParams = new RecomRequestModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerBroadcast();
        setContentView(R.layout.myself_dining_layout);
        setNotitle(true);

        mLocationHelper = new LocationHelper(this);
        mNearbyHelper = new DiningNearbyHelper(this, findView(R.id.dining_nearby));
        mScrollView = findView(R.id.base_id);
        mGridView1 = findView(R.id.grid_view_1);
        findViewById(R.id.iv_back_2).setOnClickListener(this);
        findViewById(R.id.start_recommend).setOnClickListener(this);
        findViewById(R.id.dining_nearby).findViewById(R.id.current_location).setOnClickListener(this);

        mGridView1.init(DiningGridView.TYPE_MULTIPLE);
        mHeaderImageHeight = getResources().getDrawable(R.drawable.together_dining_header).getIntrinsicHeight();

        TextView textView = (TextView) findViewById(R.id.child_title_1).findViewById(R.id.title_1);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.font_size_10));
        textView.setText(R.string.dining_child_title_5);
        textView.getPaint().setFakeBoldText(true);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
        params.topMargin = DisplayUtil.dip2px(20f);
        textView.setLayoutParams(params);

        textView = (TextView) findViewById(R.id.child_title_1).findViewById(R.id.title_2);
        textView.setText(R.string.dining_child_title_6);

        setScrollListener();
        requestLocationPermission();
        addStatusBar();
        setGridData();

        //对用餐清单fragment的处理
        mFragFrame = new RelativeLayout(this);
        mFragFrame.setClickable(true);
        mFragFrame.setId(R.id.frag_layout);
        RelativeLayout.LayoutParams fragParams = new RelativeLayout.LayoutParams(-1, -1);
        getContainer().addView(mFragFrame, fragParams);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MyselfDiningListFragment fragment = new MyselfDiningListFragment();
        transaction.add(R.id.frag_layout, fragment);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mNearbyHelper.isShowingLocationView() && LocationHelper.hasLocationPermission(this)) {
            startLocation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
        mLocationHelper.stopLocation();
    }

    private void setGridData() {
        ConfigResult configResult = SP.getConfig();
        List<SiftBean.FoodKindListBean.FoodKindBean> list = configResult.getConfig().getSift().getFoodKind().getFoodKind();

        List<DiningTypeItem> typeList = new ArrayList<>();
        for (SiftBean.FoodKindListBean.FoodKindBean bean : list) {
            DiningTypeItem item = new DiningTypeItem();
            item.name = bean.getContent();
            typeList.add(item);
        }
        mGridView1.getAdapter().setDataList(typeList);
        if (typeList.size() > 0) {
            mGridView1.setSelectedItem(typeList.get(0));
        }
    }

    /**
     * 是否有用餐选择清单
     */
    public void onHasDiningList(boolean hasList) {
        if (!hasList) {
            getContainer().removeView(mFragFrame);
            startLocation();
        }
    }

    /**
     * 新建用餐清单
     */
    public void onNewBuild() {
        Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                getContainer().removeView(mFragFrame);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };

        mFragFrame.animate().setDuration(300).translationX(-mFragFrame.getWidth()).setListener(animatorListener).start();
        if (mNearbyHelper.isShowingLocationView() && LocationHelper.hasLocationPermission(this)) {
            startLocation();
        }
    }

    private void setScrollListener() {
        mScrollView.setOnScrollChangedListener(new MyScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int top, int oldTop) {
                float percent = (top * 1f) / (mHeaderImageHeight * 1f);
                if (percent >= 0 && percent <= 1) {
                    mTopTitle.setAlpha(percent);
                }
            }
        });
    }

    private void startLocation() {
        mNearbyHelper.removeRequestLocationImage();
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
            }
        });
    }

    private boolean requestLocationPermission() {
        if (!LocationHelper.hasLocationPermission(this)) {
            ActivityCompat.requestPermissions(MyselfDiningActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
            mNearbyHelper.addRequestLocationImage(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FFUtils.openAppDetailSetting(MyselfDiningActivity.this);
                }
            });
            return false;
        } else {
            startLocation();
        }
        return true;
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
                            FFUtils.openAppDetailSetting(MyselfDiningActivity.this);
                        }
                    });
                    showToast("未获得定位权限，请打开系统设置并允许定位", null);
                }
                return;
            }
        }
    }

    private void addStatusBar() {
        if (mTopTitle == null) {
            mTopTitle = View.inflate(this, R.layout.together_dining_title, null);
            View mBar = mTopTitle.findViewById(R.id.s_status_bar);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mBar.getLayoutParams();
            params.height = FFUtils.getStatusbarHight(this);
            mBar.setLayoutParams(params);
            TextView titleView = (TextView) mTopTitle.findViewById(R.id.tv_actionbar_title);
            mTopTitle.findViewById(R.id.my_back_view).setOnClickListener(this);
            mTopTitle.findViewById(R.id.start_recommend_2).setOnClickListener(this);
            titleView.setText(R.string.myself_dining_title);
            getContainer().addView(mTopTitle, new RelativeLayout.LayoutParams(-1, -2));
            mTopTitle.setAlpha(0f);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.my_back_view || v.getId() == R.id.iv_back_2) {
            finish();
        } else if (v.getId() == R.id.start_recommend || v.getId() == R.id.start_recommend_2) {
            if (mGridView1.getAdapter().getSelectedList().size() <= 0) {
                showToast("请至少选择一个哦");
                return;
            }
            if (mLocationHelper.isStarted()) {
                showToast("正在定位，请稍等");
                return;
            }
            getRecommendList();
        } else if (v.getId() == R.id.current_location) {
            mNearbyHelper.setGDPoiModel(null);
            startLocation();
        }
    }

    /**
     * 得到推荐列表数据
     */
    private void getRecommendList() {
        if (!FFUtils.checkNet()) {
            showToast(getString(R.string.lsq_network_connection_interruption));
            return;
        }

        mParams.shopId = "0";

        List<DiningTypeItem> indexList = mGridView1.getAdapter().getSelectedList();
        StringBuilder builder = new StringBuilder();
        if (indexList.size() > 0) {
            for (DiningTypeItem item : indexList) {
                if (builder.length() <= 0) {
                    builder.append(item.name);
                } else {
                    builder.append("," + item.name);
                }
            }
            mParams.ptypes = builder.toString();
        } else {
            mParams.ptypes = null;
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

        pushEvent182(mParams.ptypes);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/discover/onePersonFood/onePersonFoodList.do",
        post(IUrlUtils.WorkDining.onePersonFoodList,
                "", null, new FFNetWorkCallBack<RecomResultModel>() {
                    @Override
                    public void onSuccess(RecomResultModel response, FFExtraParams extra) {
                        if (response != null && response.syFindMerchantInfos != null) {
                            if (response.syFindMerchantInfos.size() > 0) {
                                mParams.shopId = response.syFindMerchantInfos.get(response.syFindMerchantInfos.size() - 1).getMerchantUid();
                                DiningRecommendActivity.startFromSingle(context(), response.syFindMerchantInfos, mParams);
                            } else {
                                new EnsureDialog.Builder(context()).setMessage(R.string.dining_recommend_result_empty)
                                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                            }
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "pageSize", 15
                , "shopId", mParams.shopId
                , "longitude", longitude
                , "latitude", latitude
                , "ptypes", mParams.ptypes
                , "city", "北京"
                , "openGPS", "0"
                , "address", mNearbyHelper.getAddress()
        );

    }


    private void registerBroadcast() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(MyActions.ACTION_GD_POI)) {
                    GDPoiModel gDPoiModel = (GDPoiModel) intent.getSerializableExtra("item");
                    mNearbyHelper.setGDPoiModel(gDPoiModel);
                    mNearbyHelper.setAddress(gDPoiModel.name, false);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyActions.ACTION_GD_POI);
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void unregisterReceiver() {
        if (mBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        }
    }

    public void pushEvent182(String type){
        HashMap<String, String> event = new HashMap<String, String>();
        event.put("account", SP.getUid());
        event.put("city", CityPref.getSelectedCity().getAreaName());
        event.put("food_type", type);
        pushEventAction("Yellow_182", event);
    }
}
