package com.fengnian.smallyellowo.foodie.diningcase;

import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.fan.framework.config.Value;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;

/**
 * Created by chenglin on 2017-6-9.
 */

public class DiningNearbyHelper {
    private BaseActivity mActivity;
    private View mLocationView;
    private View mHeaderView;
    private TextView mTvAddress;
    private String mAddressStr;
    private GDPoiModel mGDPoiModel;

    public DiningNearbyHelper(BaseActivity activity, View headerView) {
        mActivity = activity;
        mHeaderView = headerView;
        mTvAddress = (TextView) mHeaderView.findViewById(R.id.tv_address);

        mHeaderView.findViewById(R.id.dining_nearby).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FFUtils.checkNet()) {
                    mActivity.showToast(mActivity.getString(R.string.lsq_network_connection_interruption));
                    return;
                }

                if (mGDPoiModel != null) {
                    NearbyGdDiningRoomActivity.start(mActivity, mGDPoiModel);
                } else {
                    NearbyGdDiningRoomActivity.start(mActivity);
                }

            }
        });
    }

    public GDPoiModel getGDPoiModel() {
        return mGDPoiModel;
    }

    public void setGDPoiModel(GDPoiModel model) {
        mGDPoiModel = model;
    }

    public void addRequestLocationImage(View.OnClickListener clickListener) {
        if (mLocationView == null) {
            mLocationView = View.inflate(mActivity, R.layout.dining_request_location_layout, null);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
            mActivity.getContainer().addView(mLocationView, params);

            View btn = mLocationView.findViewById(R.id.btn);
            btn.setOnClickListener(clickListener);

            View s_status_bar = mLocationView.findViewById(R.id.s_status_bar);
            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) s_status_bar.getLayoutParams();
            params2.height = FFUtils.getStatusbarHight(mActivity);
            s_status_bar.setLayoutParams(params2);

            TextView tv_title = (TextView) mLocationView.findViewById(R.id.tv_title);
            if (mActivity instanceof WorkDiningActivity) {
                tv_title.setText(R.string.dining_work_title);
            } else if (mActivity instanceof TogetherDiningActivity) {
                tv_title.setText(R.string.together_dining_title);
            } else if (mActivity instanceof MyselfDiningActivity) {
                tv_title.setText(R.string.myself_dining_title);
            }

            mLocationView.findViewById(R.id.back_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.finish();
                }
            });
        }
    }

    public void removeRequestLocationImage() {
        if (mLocationView != null) {
            mActivity.getContainer().removeView(mLocationView);
            mLocationView = null;
        }
    }

    public void setAddress(BDLocation location) {
        mAddressStr = location.getAddrStr();
        if (TextUtils.isEmpty(mAddressStr)) {
            mAddressStr = mActivity.getString(R.string.location_fail);
            mTvAddress.setText(mAddressStr);
            return;
        }
        getNearbyData();
    }

    public void setAddress(String addressStr, boolean isCurrent) {
        mAddressStr = addressStr;
        if (isCurrent) {
            final String currentStr = "[当前] ";
            SpannableString spannableString = new SpannableString(currentStr + addressStr);
            int color = mActivity.getResources().getColor(R.color.search_color);
            spannableString.setSpan(new ForegroundColorSpan(color), 0, currentStr.length(), 0);
            mTvAddress.setText(spannableString);
        } else {
            mTvAddress.setText(addressStr);
        }
    }

    public String getAddress() {
        return mAddressStr;
    }

    public void getNearbyData() {
        mActivity.get(Value.GD_AROUND, "", null, new FFNetWorkCallBack<NearbyGdDiningRoomActivity.GDPoiList>() {
                    @Override
                    public void onSuccess(NearbyGdDiningRoomActivity.GDPoiList response, FFExtraParams extra) {
                        if (response != null && response.pois != null && response.pois.size() > 0) {
                            GDPoiModel model = response.pois.get(0);

                            if (model != null && !TextUtils.isEmpty(model.name)) {
                                setAddress(model.name, true);
                            } else {
                                mTvAddress.setText(R.string.location_fail);
                            }
                        } else {
                            mTvAddress.setText(R.string.location_fail);
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        mTvAddress.setText(R.string.location_fail);
                        return false;
                    }

                }, "key", Value.GDkey
                , "location", Value.mLongitude + "," + Value.mLatitude
                , "offset", 1
                , "page", 1
                , "radius", Value.GD_AROUND_RADIUS
                , "sortrule", "distance");
    }


    public boolean isShowingLocationView() {
        if (mLocationView == null) {
            return false;
        } else {
            return true;
        }
    }

}
