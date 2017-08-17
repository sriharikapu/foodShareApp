package com.fengnian.smallyellowo.foodie.diningcase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.fan.framework.config.Value;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.CityPref;
import com.fengnian.smallyellowo.foodie.appbase.PreferJsonData;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.personal.MyActions;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.LocationHelper;
import com.fengnian.smallyellowo.foodie.widgets.MyScrollView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chenglin on 2017-6-13.
 */

public class TogetherDiningActivity extends BaseActivity<IntentData> implements View.OnClickListener {
    private final int REQUEST_LOCATION = 1001;
    private LocationHelper mLocationHelper;
    private DiningNearbyHelper mNearbyHelper;
    private MyScrollView mScrollView;
    private View mTopTitle;
    private DiningGridView mGridView1, mGridView2;
    private int mHeaderImageHeight = 0;
    private RecomRequestModel mParams = new RecomRequestModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerBroadcast();
        setContentView(R.layout.together_dining_layout);
        setNotitle(true);

        mLocationHelper = new LocationHelper(this);
        mNearbyHelper = new DiningNearbyHelper(this, findView(R.id.dining_nearby));
        mScrollView = findView(R.id.base_id);
        mGridView1 = findView(R.id.grid_view_1);
        mGridView2 = findView(R.id.grid_view_2);
        findViewById(R.id.iv_back_2).setOnClickListener(this);
        findViewById(R.id.start_recommend).setOnClickListener(this);
        findViewById(R.id.dining_nearby).findViewById(R.id.current_location).setOnClickListener(this);

        mGridView1.init(DiningGridView.TYPE_SINGLE);
        mGridView2.init(DiningGridView.TYPE_MULTIPLE);
        mGridView1.setChild(mGridView2);
        mHeaderImageHeight = getResources().getDrawable(R.drawable.together_dining_header).getIntrinsicHeight();

        TextView textView = (TextView) findViewById(R.id.child_title_1).findViewById(R.id.title_1);
        textView.setText(R.string.dining_child_title_1);
        textView.getPaint().setFakeBoldText(true);

        textView = (TextView) findViewById(R.id.child_title_1).findViewById(R.id.title_2);
        textView.setText(R.string.dining_child_title_2);

        textView = (TextView) findViewById(R.id.child_title_2).findViewById(R.id.title_1);
        textView.setText(R.string.dining_child_title_3);
        textView.getPaint().setFakeBoldText(true);

        textView = (TextView) findViewById(R.id.child_title_2).findViewById(R.id.title_2);
        textView.setText(R.string.dining_child_title_4);

        setScrollListener();
        requestLocationPermission();
        addStatusBar();
        getData();
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

    private void getData() {
        String showText = "";
        String result = PreferJsonData.getTogetherTypeJson();
        if (!TextUtils.isEmpty(result) && result.length() > 10) {
            DiningTypeList diningList = JSON.parseObject(result, DiningTypeList.class);
            setGridData(diningList);
            showText = null;
        }

//        post(Constants.shareConstants().getNetHeaderAdress() + "/discover/multipleMeals/queryFoodKindNumberRanges.do",
        post(IUrlUtils.WorkDining.queryFoodKindNumberRanges,
                showText, null, new FFNetWorkCallBack<DiningTypeList>() {
                    @Override
                    public void onSuccess(DiningTypeList response, FFExtraParams extra) {
                        if (response != null) {
                            PreferJsonData.setTogetherTypeJson(JSON.toJSONString(response));
                            setGridData(response);
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                });

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
        if (indexList.size() > 0) {
            mParams.peopleTypeName = indexList.get(0).name;
        }

        indexList = mGridView2.getAdapter().getSelectedList();
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

        pushEvent160(mParams.peopleTypeName,mParams.ptypes);

//        post(Constants.shareConstants().getNetHeaderAdress() + "/discover/multipleMeals/multipleMealsRecommendedList.do",
        post(IUrlUtils.WorkDining.multipleMealsRecommendedList,
                "", null, new FFNetWorkCallBack<RecomResultModel>() {
                    @Override
                    public void onSuccess(RecomResultModel response, FFExtraParams extra) {
                        if (response != null && response.syFindMerchantInfos != null) {
                            if (response.syFindMerchantInfos.size() > 0) {
                                mParams.shopId = response.syFindMerchantInfos.get(response.syFindMerchantInfos.size() - 1).getMerchantUid();
                                mParams.address = mNearbyHelper.getAddress();
                                DiningRecommendActivity.startFromMultiple(TogetherDiningActivity.this, response.syFindMerchantInfos, mParams);
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
                }, "pageSize", 20
                , "shopId", mParams.shopId
                , "longitude", longitude
                , "latitude", latitude
                , "peopleTypeName", mParams.peopleTypeName
                , "ptypes", mParams.ptypes
                , "city", "北京"
                , "openGPS", "0"
        );

    }

    private void setGridData(DiningTypeList response) {
        if (response != null && response.friendDineInfo != null && response.friendDineInfo.size() > 0) {
            List<DiningTypeItem> list = new ArrayList<>();
            for (int i = 0; i < response.friendDineInfo.size(); i++) {
                SYFindPeopleNumForFoodTypeModel parentModel = response.friendDineInfo.get(i);

                DiningTypeItem item = new DiningTypeItem();
                item.name = parentModel.peopleTypeName;

                List<DiningTypeItem> list2 = new ArrayList<>();
                for (int j = 0; j < parentModel.foodTypes.size(); j++) {
                    DiningTypeItem item2 = new DiningTypeItem();
                    item2.name = parentModel.foodTypes.get(j).foodTypeName;
                    list2.add(item2);
                }
                item.foodTypeList = list2;

                list.add(item);
            }
            mGridView1.getAdapter().setDataList(list);

            if (list.size() > 2) {
                DiningTypeItem selectedItem = list.get(1);
                mGridView2.getAdapter().setDataList(selectedItem.foodTypeList);
                mGridView1.setSelectedItem(selectedItem);
            }
        }

    }

    private void requestLocationPermission() {
        if (!LocationHelper.hasLocationPermission(this)) {
            ActivityCompat.requestPermissions(TogetherDiningActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
            mNearbyHelper.addRequestLocationImage(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FFUtils.openAppDetailSetting(TogetherDiningActivity.this);
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
                            FFUtils.openAppDetailSetting(TogetherDiningActivity.this);
                        }
                    });
                    showToast("未获得定位权限，请打开系统设置并允许定位", null);
                }
                return;
            }
        }
    }

    private BroadcastReceiver mBroadcastReceiver;

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
            titleView.setText(R.string.together_dining_title);
            getContainer().addView(mTopTitle, new RelativeLayout.LayoutParams(-1, -2));
            mTopTitle.setAlpha(0f);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.my_back_view || v.getId() == R.id.iv_back_2) {
            finish();
        } else if (v.getId() == R.id.start_recommend || v.getId() == R.id.start_recommend_2) {
            if (mGridView1.getAdapter().getSelectedList().size() <= 0
                    || mGridView2.getAdapter().getSelectedList().size() <= 0) {
                showToast("请至少选择一个哦");
                return;
            }
            if (mLocationHelper.isStarted()){
                showToast("正在定位，请稍等");
                return;
            }
            getRecommendList();
        } else if (v.getId() == R.id.current_location) {
            mNearbyHelper.setGDPoiModel(null);
            startLocation();
        }
    }

    private static final class DiningTypeList extends BaseResult implements Serializable {
        public List<SYFindPeopleNumForFoodTypeModel> friendDineInfo;
    }

    public void pushEvent160(String persons, String type){
        HashMap<String, String> event = new HashMap<String, String>();
        event.put("account", SP.getUid());
        event.put("city", CityPref.getSelectedCity().getAreaName());
        event.put("person_count", persons);
        event.put("food_type", type);
        pushEventAction("Yellow_160", event);
    }
}
