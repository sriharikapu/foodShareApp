package com.fengnian.smallyellowo.foodie;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.CityPref;
import com.fengnian.smallyellowo.foodie.appbase.PullToRefreshFragment;
import com.fengnian.smallyellowo.foodie.bean.CityListBean;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFindMerchantInfo;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.bean.results.RestListResult;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.RestInfoIntent;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 演示覆盖物的用法
 */
public class CustomCircleActivity extends BaseActivity<IntentData> {

    @Bind(R.id.tv_circle_name)
    TextView tvCircleName;
    @Bind(R.id.tv_shop_num)
    TextView tvShopNum;
    @Bind(R.id.fl_listView_container)
    FrameLayout flListViewContainer;
    @Bind(R.id.shop_list_container)
    LinearLayout shopListContainer;
    /**
     * MapView 是地图主控件
     */
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private List<CustomCircleBean> relationships;
    private MyPullToRefreshFragment fragment;

    @Override
    public void onBackPressed() {
        if (shopListContainer.getVisibility() == View.VISIBLE) {
            shopListContainer.setVisibility(View.GONE);
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
                fragment = null;
            }
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_circle);
        ButterKnife.bind(this);
        setTitle("商圈地图");
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        CityListBean.CityBean selectedCity = CityPref.getSelectedCity();
        LatLng cenpt = new LatLng(selectedCity.getLatitude(), selectedCity.getLongitude());
        MapStatus mMapStatus = new MapStatus.Builder()
                //要移动的点
                .target(cenpt)
                //放大地图到20倍
                .zoom(12)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (relationships != null) {
                    for (int i = 0; i < relationships.size(); i++) {
                        final List<LatLng> pts = new ArrayList<>();
                        for (Location location : relationships.get(i).coordinates) {
                            pts.add(new LatLng(location.latitude, location.longitude));
                        }
                        boolean polygon = SpatialRelationUtil.isPolygonContainsPoint(pts, latLng);
                        if (polygon) {
                            onCircleClick(relationships.get(i));
                            break;
                        }
                    }
                }
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                if (relationships != null) {
                    for (int i = 0; i < relationships.size(); i++) {
                        final List<LatLng> pts = new ArrayList<>();
                        for (Location location : relationships.get(i).coordinates) {
                            pts.add(new LatLng(location.latitude, location.longitude));
                        }
                        boolean polygon = SpatialRelationUtil.isPolygonContainsPoint(pts, mapPoi.getPosition());
                        if (polygon) {
                            onCircleClick(relationships.get(i));
                            return true;
                        }
                    }
                }
                return false;
            }
        });


        post(IUrlUtils.Discover.CUSTOM_CIRCLE, "", null, new FFNetWorkCallBack<CustomCircleResult>() {
            @Override
            public void onSuccess(CustomCircleResult response, FFExtraParams extra) {
                relationships = response.relationships;
                for (CustomCircleBean circle : response.relationships) {
                    test(circle);
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        });

    }

    public static class CustomCircleResult extends BaseResult {
        public List<CustomCircleBean> relationships;
    }

    public static class Location implements Parcelable {
        public double longitude;
        public double latitude;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(this.longitude);
            dest.writeDouble(this.latitude);
        }

        public Location() {
        }

        protected Location(Parcel in) {
            this.longitude = in.readDouble();
            this.latitude = in.readDouble();
        }

        public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
            @Override
            public Location createFromParcel(Parcel source) {
                return new Location(source);
            }

            @Override
            public Location[] newArray(int size) {
                return new Location[size];
            }
        };
    }

    public static class CustomCircleBean implements Parcelable {
        public String areaId;
        public String areaName;
        public String shopCount;
        public String coatingColour;
        public String frameColour;
        public Location center;
        public List<Location> coordinates;

        public int borderColor() {
            try {
                return Color.parseColor(coatingColour);
            } catch (Exception e) {
            }
            return 0x883232cd;
        }

        public int solidColor() {
            try {
                return Color.parseColor(frameColour);
            } catch (Exception e) {
            }
            return 0x884d4dff;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.areaId);
            dest.writeString(this.areaName);
            dest.writeString(this.shopCount);
            dest.writeString(this.coatingColour);
            dest.writeString(this.frameColour);
            dest.writeParcelable(this.center, flags);
            dest.writeTypedList(this.coordinates);
        }

        public CustomCircleBean() {
        }

        protected CustomCircleBean(Parcel in) {
            this.areaId = in.readString();
            this.areaName = in.readString();
            this.shopCount = in.readString();
            this.coatingColour = in.readString();
            this.frameColour = in.readString();
            this.center = in.readParcelable(Location.class.getClassLoader());
            this.coordinates = in.createTypedArrayList(Location.CREATOR);
        }

        public static final Parcelable.Creator<CustomCircleBean> CREATOR = new Parcelable.Creator<CustomCircleBean>() {
            @Override
            public CustomCircleBean createFromParcel(Parcel source) {
                return new CustomCircleBean(source);
            }

            @Override
            public CustomCircleBean[] newArray(int size) {
                return new CustomCircleBean[size];
            }
        };
    }


    public static class Holder {
        public ImageView iv_img;
        public TextView tv_name;
        public TextView tv_distance;
        public TextView tv_class_and_per_people;
        public RatingBar ratingBar;
        public TextView tv_score;
    }

    public static class MyPullToRefreshFragment extends PullToRefreshFragment<Holder, SYFindMerchantInfo, RestListResult> {

        @Override
        protected Object[] getParams(int currentPage) {
            return new Object[]{"areaId", ((CustomCircleBean) getArguments().getParcelable("bean")).areaId,
                    "openGPS", 1,
                    "longitude", 0,
                    "latitude", 0,
                    "pageNum", currentPage + 1,
                    "pageSize", 8};
        }

        @Override
        protected String getUrl() {
            return IUrlUtils.Discover.CUSTOM_CIRCLE_RESTS;
        }

        String getSubFloat(double f) {
            DecimalFormat fnum = new DecimalFormat("##0.0");
            String string = fnum.format(f);
            return string.equals("-0") ? "0" : string;
        }

        @Override
        protected void refreshItem(View convertView, Holder holder, int position, final SYFindMerchantInfo item) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RestInfoIntent intent = new RestInfoIntent();
                    intent.setInfo(item);
                    startActivity(RestInfoActivity.class, intent);
                }
            });
            FFImageLoader.loadSmallImage(context(), item.getMerchantImage().getThumbUrl(), holder.iv_img);
            if (FFUtils.getSubFloat(item.getMerchantPrice()).equals("0")) {
                String jj = item.getMerchantKind();
                if (FFUtils.isStringEmpty(jj)) {
                    holder.tv_class_and_per_people.setVisibility(View.GONE);
                } else {
                    holder.tv_class_and_per_people.setVisibility(View.VISIBLE);
                    holder.tv_class_and_per_people.setText(jj);
                }
            } else {
                holder.tv_class_and_per_people.setVisibility(View.VISIBLE);
                String jj = item.getMerchantKind() + "  |  ";
                if (item.getMerchantKind() == null || item.getMerchantKind().length() == 0) {
                    jj = "";
                }
                holder.tv_class_and_per_people.setText(jj + "￥" + FFUtils.getSubFloat(item.getMerchantPrice()) + "/人");
            }
            holder.tv_distance.setText(item.getMerchantDistance());
            holder.tv_name.setText(item.getMerchantName());
            if (item.getStartLevel() <= 0) {
                holder.ratingBar.setVisibility(View.GONE);
                holder.tv_score.setVisibility(View.GONE);
            } else {
                holder.ratingBar.setVisibility(View.VISIBLE);
                holder.tv_score.setVisibility(View.VISIBLE);
                holder.ratingBar.setRating(item.getStartLevel());
                holder.tv_score.setText(getSubFloat(item.getStartLevel()));
            }
        }

        @Override
        protected void onGetView(Holder holder) {

        }

        @Override
        protected int getItemViewId(int position) {
            return R.layout.item_rest_custom_map;
        }
    }

    private void test(CustomCircleBean circle) {
        if (circle.coordinates == null || circle.coordinates.size() < 3) {
            return;
        }
        //定义多边形的五个顶点
        final List<LatLng> pts = new ArrayList<>();
        for (Location location : circle.coordinates) {
            pts.add(new LatLng(location.latitude, location.longitude));
        }
//构建用户绘制多边形的Option对象
        OverlayOptions polygonOption = new PolygonOptions()
                .points(pts)
                .stroke(new Stroke(5, circle.borderColor()))
                .fillColor(circle.solidColor());
//在地图上添加多边形Option，用于显示
        mBaiduMap.addOverlay(polygonOption);
    }

    private void onCircleClick(CustomCircleBean customCircleBean) {
        shopListContainer.setVisibility(View.VISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragment = new MyPullToRefreshFragment();
        tvCircleName.setText(customCircleBean.areaName);
        tvShopNum.setText(customCircleBean.shopCount);
        Bundle args = new Bundle();
        args.putParcelable("bean", customCircleBean);
        fragment.setDividerHeight(1);
        fragment.setArguments(args);
        ft.add(R.id.fl_listView_container, fragment);
        ft.commit();
    }

    /**
     * 清除所有Overlay
     *
     * @param view
     */
    public void clearOverlay(View view) {
        mBaiduMap.clear();
    }

    /**
     * 重新添加Overlay
     *
     * @param view
     */
    public void resetOverlay(View view) {
        clearOverlay(null);
    }

    @Override
    protected void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        mMapView.onDestroy();
        super.onDestroy();
        // 回收 bitmap 资源
    }

}
