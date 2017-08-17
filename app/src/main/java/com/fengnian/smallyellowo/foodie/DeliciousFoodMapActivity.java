package com.fengnian.smallyellowo.foodie;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.fan.framework.base.MyBaseAdapter;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFLogUtil;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.Adapter.MyPageAdapter;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.CityPref;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.CityListBean;
import com.fengnian.smallyellowo.foodie.bean.SiftBean;
import com.fengnian.smallyellowo.foodie.bean.publics.DotInfo;
import com.fengnian.smallyellowo.foodie.bean.publics.SYPoi;
import com.fengnian.smallyellowo.foodie.bean.results.ConfigResult;
import com.fengnian.smallyellowo.foodie.bean.results.DeliciousFoodListResult;
import com.fengnian.smallyellowo.foodie.clusterutil.MarkerManager;
import com.fengnian.smallyellowo.foodie.clusterutil.clustering.Cluster;
import com.fengnian.smallyellowo.foodie.clusterutil.clustering.ClusterManager;
import com.fengnian.smallyellowo.foodie.clusterutil.clustering.view.DefaultClusterRenderer;
import com.fengnian.smallyellowo.foodie.contact.AddFriendsActivty;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.RestInfoIntent;
import com.fengnian.smallyellowo.foodie.utils.GPSUtil;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.MyOrientationListener;
import com.fengnian.smallyellowo.foodie.widgets.CustomRatingBar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.fan.framework.imageloader.FFImageLoader.TYPE_ROUND;


/**
 * Created by Administrator on 2016-12-28.
 */

public class DeliciousFoodMapActivity extends BaseActivity<IntentData> implements BaiduMap.OnMapLoadedCallback, View.OnClickListener {
    private List<DotInfo> dotList = new ArrayList<>();  //地图列表信息
    private List<DotInfo> two_dotList = new ArrayList<>();  //地图列表信息
    private List<DotInfo> mlist = new ArrayList<>(); //
    MapView mMapView;
    BaiduMap mBaiduMap;
    MapStatus ms;
    private ClusterManager<DotInfo> mClusterManager;

    public static int SetMarkIconStytle = 1;//  1是默认 2是   加星

    BitmapDescriptor mCurrentMarker;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;

    //    private BMapManager mBMapManager;
    private LocationClient locationClient = null;

    private static final int UPDATE_TIME = 5000;

    private static int LOCATION_COUTNS = 0;

    private LatLng latLng;

    private String city;

    private MarkerManager markerManager;

    private String Merchantlds;
    private ViewPager view_page;

    private LinearLayout enty_cir;

    private FrameLayout rl_pinlei;

    private TextView tv_class;

    private LinearLayout lin_shaixuan;

    private MyOrientationListener mOrientationListener;
    private float mCurrentX;//方向值
    private TextView tv_pick1;
    private RelativeLayout rl_2, rl_viewpage;
    float ss;

    private ImageView iv_guiwei, iv_jiajing;

    private ListView lv_view0, lv_view1;
    List<SiftBean.FoodKindListBean.FoodKindBean> list_zhonglei = new ArrayList<>();
    List<SiftBean.FilterListBean.FilterBean> list__shuaixuan = new ArrayList<>();
    int hight;

    private ImageView btn_ok, btn_reset;

    private String zhonglei_content = "全部美食", xgd_code = "";

    BDLocation L_location;

    String head_url;

    boolean isFirstLoc = true; // 是否首次定位

    private int pageSize = 100;//分页请求的数据
    private CityListBean.CityBean currentCity; // 用户选择的城市

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delicious_food);
        head_url = SP.getUser().getHeadImage().getUrl();
        rl_pinlei = findView(R.id.rl_pinlei);
        rl_pinlei.setOnClickListener(this);
        tv_class = findView(R.id.tv_class);
        lin_shaixuan = findView(R.id.lin_shaixuan);
        lin_shaixuan.setOnClickListener(this);
        tv_pick1 = findView(R.id.tv_pick1);
        rl_2 = findView(R.id.rl_2);
        rl_2.setOnClickListener(this);
        lv_view0 = findView(R.id.lv_view0);
        lv_view1 = findView(R.id.lv_view1);

        mMapView = (MapView) findViewById(R.id.bmapView);
        rl_viewpage = findView(R.id.rl_viewpage);
        rl_viewpage.setOnClickListener(this);
        view_page = findView(R.id.view_page);
        enty_cir = findView(R.id.enty_cir);

        iv_guiwei = findView(R.id.iv_guiwei);
        iv_guiwei.setOnClickListener(this);
        iv_jiajing = findView(R.id.iv_jiajing);
        iv_jiajing.setOnClickListener(this);
        initjuhe();

        ConfigResult res = SP.getConfig();
        currentCity = CityPref.getSelectedCity();
        SiftBean bean = res.getConfig().getSift();
        list_zhonglei.addAll(bean.getFoodKind().getFoodKind());
        list__shuaixuan.addAll(bean.getFilter().getList());

        for (int x = 0; x < list__shuaixuan.size(); x++) {
            SiftBean.FilterListBean.FilterBean b = list__shuaixuan.get(x);
            if (x == 0)
                b.setCode("x");
            if (x == 1)
                b.setCode("g");
            if (x == 2)
                b.setCode("d");
        }

        hight = Math.min(FFUtils.getDisHight() - FFUtils.getPx(25 + 48 + 49 + 48 + 20), list_zhonglei.size() * (FFUtils.getPx(48) + 1));
        if (android.os.Build.VERSION.SDK_INT >= 23 && !hasPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION))
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, AddFriendsActivty.MY_PERMISSIONS_REQUEST_READ_CONTACTS_ADD);
        else {
            initLoation();
        }
    }

    private void initjuhe() {
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMapLoadedCallback(this);
        markerManager = new MarkerManager(mBaiduMap);
        // 定义点聚合管理类ClusterManager
        mClusterManager = new ClusterManager<>(this, mBaiduMap, markerManager);
//        // 添加Marker点
//        addMarkers();
        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
    }

    MyLocationData locData;

    private void initLoation() {
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        locationClient = new LocationClient(this);

        //设置定位条件
        LocationClientOption option = new LocationClientOption();

        option.setOpenGps(true);        //是否打开GPS

        option.setCoorType("bd09ll");       //设置返回值的坐标类型。

        option.setPriority(LocationClientOption.NetWorkFirst);  //设置定位优先级

        option.setProdName("LocationDemo"); //设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。

//        option.setScanSpan(UPDATE_TIME);    //设置定时定位的时间间隔。单位毫秒

        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息


        locationClient.setLocOption(option);

        locationClient.start();  // 调用此方法开始定位

        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if (isFinishing() || getDestroyed()){
                    locationClient.stop();
                    return;
                }
                if (location == null) {
                    DeliciousFoodMapActivity.this.showToast("定位失败");
                    return;
                }
                L_location = location;

                latLng = new LatLng(location.getLatitude(), location.getLongitude());

                locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(mCurrentX). //方向传值
                        latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                // 设置定位数据
                mBaiduMap.setMyLocationData(locData);

                seticon();
                mBaiduMap
                        .setMyLocationConfigeration(new MyLocationConfiguration(
                                null, true, mCurrentMarker));//普通模式 //MyLocationConfiguration.LocationMode.NORMAL
                if (isFirstLoc) {
                    isFirstLoc = false;

                    initLocationAndCenter(18.0f);

                }
                if (L_location != null && mOrientationListener != null)
                    mOrientationListener.start();

//                String city = location.getCity();
                String city = currentCity.getAreaName();
                if (city == null) {
                    city = "北京";
                }
                if (city.contains("市")) {
                    city =city.replace("市", "");
                }
                getfoodlocation(city);
                locationClient.stop();//取消定位


            }
        });

        mOrientationListener = new MyOrientationListener(DeliciousFoodMapActivity.this);

        mOrientationListener.start();
        mOrientationListener
                .setmOnOrientationListener(new MyOrientationListener.OnOrientationListener() {

                    @Override
                    public void onOrientationChanged(float x) {
                        mCurrentX = x;

                        if (is_add_sucess == 1) {
                            seticon();
                            is_add_sucess = 0;
                            mBaiduMap
                                    .setMyLocationConfigeration(new MyLocationConfiguration(
                                            MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker));
                        }
                        if (L_location != null) {
                            locData = new MyLocationData.Builder()
                                    .accuracy(ss)
                                    // 此处设置开发者获取到的方向信息，顺时针0-360
                                    .direction(mCurrentX) //方向传值
                                    .latitude(L_location.getLatitude())
                                    .longitude(L_location.getLongitude())
                                    .build();
                            // 设置定位数据
                            mBaiduMap.setMyLocationData(locData);
                        }
                    }
                });


        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                return mClusterManager.onMarkerClick(marker);
            }
        });

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<DotInfo>() {
            @Override
            public boolean onClusterClick(Cluster<DotInfo> cluster) {
                float zoom = mBaiduMap.getMapStatus().zoom;
//                FFLogUtil.e("food", "zoom = " + zoom);
                // 21:代表地图标尺为   1:5
                if (cluster.getSize() > 6 && zoom < 21) {
//                    showToast("请双击点击");
                    setCenter(cluster.getPosition(), zoom + 1);
                    return true;//不对地图做处理
                } else {

                    setCenter(cluster.getPosition(), zoom);
                    StringBuffer bu = new StringBuffer();
                    int length = cluster.getSize();
                    List<DotInfo> ll = new ArrayList<DotInfo>();
                    ll.addAll(cluster.getItems());
                    for (int i = 0; i < length; i++) {
                        String id = ll.get(i).getPoi().getId();
                        if (i == (length - 1))
                            bu.append(id);
                        else
                            bu.append(id + ",");
                    }
                    getMarkInfo(bu.toString());
                }


                return false;
            }
        });
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<DotInfo>() {
            @Override
            public boolean onClusterItemClick(DotInfo item) {
                String id = item.getPoi().getId();
                LatLng ll = new LatLng(item.getPoi().getLatitude(), item.getPoi().getLongitude());
                setCenter(ll, mBaiduMap.getMapStatus().zoom);
                getMarkInfo(id);
                return false;
            }
        });

    }

    private boolean isSameCity = false;

    private void initLocationAndCenter(float zoom){
        LatLng ll;
        if (L_location != null){
            String current = L_location.getCity();
            String city = currentCity.getAreaName();
            if (city.contains("市")){
                city = city.replace("市", "");
            }

            if (!TextUtils.isEmpty(current)){
                isSameCity = current.contains(city);
            }

        }
        FFLogUtil.e("DeliciousFoodMapActivity", "isSameCity = " + isSameCity);
        if (isSameCity && L_location != null){
            ll = new LatLng(L_location.getLatitude(), L_location.getLongitude());
        } else {
            ll = new LatLng(currentCity.getLatitude(),currentCity.getLongitude());
        }

        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(zoom);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }


    private void setCenter(LatLng lalo, float zoom) {
        LatLng ll = new LatLng(lalo.latitude,lalo.longitude);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(zoom);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    private int is_add_sucess = 0;

    private void seticon() {
        final View v = View.inflate(DeliciousFoodMapActivity.this, R.layout.view_map_icon, null);
        ImageView iv_head = (ImageView) v.findViewById(R.id.iv_head);
        FFImageLoader.load_base(DeliciousFoodMapActivity.this, head_url, iv_head, true, Constants.AvatarImage, Constants.AvatarImage, R.mipmap.moren_head_img, TYPE_ROUND, new FFImageCallBack() {
            @Override
            public void imageLoaded(Bitmap bitmap, String imageUrl) {
                mCurrentMarker = BitmapDescriptorFactory.fromView(v);
                is_add_sucess = 1;
            }

            @Override
            public void onDownLoadProgress(int downloaded, int contentLength) {

            }
        });
        mCurrentMarker = BitmapDescriptorFactory.fromView(v);
    }

    /**
     * 向地图添加Marker点
     *
     * @param list
     */
    public void addMarkers(List<DotInfo> list) {
        // 添加Marker点
        for (int i = 0; i < list.size(); i++) {
            DotInfo info = list.get(i);
//            SYPoi po=info.getPoi();
            mClusterManager.addItem(info);
        }

        mClusterManager.cluster();//强制聚合
    }

    @Override
    protected void onPause() {
//        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 关闭方向传感器
        if (mOrientationListener != null)
            mOrientationListener.stop();
    }

    @Override
    protected void onResume() {
//        mMapView.onResume();
        super.onResume();
        //开启方向传感器
        if (mOrientationListener != null)
            mOrientationListener.start();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        //退出时销毁定位
        if (locationClient != null) {
            locationClient.stop();
        }
        SetMarkIconStytle = 1;
        super.onDestroy();
    }


    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
    private List<View> ig = new ArrayList<>();//

    private void getMarkInfo(final String merchantlds) {

        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
        post(IUrlUtils.Search.queryShopInfoV250, "", extra, new FFNetWorkCallBack<DeliciousFoodListResult>() {
            @Override
            public void onSuccess(DeliciousFoodListResult response, FFExtraParams extra) {

                if (mlist != null) mlist.clear();
                mlist.addAll(response.getList());

                int len = mlist.size();
                if (len > 0) {
                    rl_viewpage.setVisibility(View.VISIBLE);
                    view_page.setVisibility(View.VISIBLE);
                    enty_cir.removeAllViews();
                    if (len > 1)
                        enty_cir.setVisibility(View.VISIBLE);
                    else enty_cir.setVisibility(View.GONE);
                    set_view_page_adapter(mlist);
                }

            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "merchantIds", merchantlds);
    }

    private void set_view_page_adapter(final List<DotInfo> list) {


        params.leftMargin = 10;
        params.rightMargin = 10;
        params.gravity = Gravity.CENTER;
        ig.clear();
        for (int i = 0; i < list.size(); i++) {
//         ImageView view = new ImageView(this);
            ImageView view1 = new ImageView(this);

            View view = View.inflate(DeliciousFoodMapActivity.this, R.layout.item_view_page, null);
            RelativeLayout rl_1 = (RelativeLayout) view.findViewById(R.id.rl_1);

            //评分
            LinearLayout lin_1_1 = (LinearLayout) view.findViewById(R.id.lin_1_1);
            CustomRatingBar rating_bar = (CustomRatingBar) view.findViewById(R.id.rating_bar);
            TextView tv_stervl = (TextView) view.findViewById(R.id.tv_stervl);


            ImageView iv_xiang = (ImageView) view.findViewById(R.id.iv_xiang);
            ImageView iv_guan = (ImageView) view.findViewById(R.id.iv_guan);
            ImageView iv_da = (ImageView) view.findViewById(R.id.iv_da);

            ImageView iv_img = (ImageView) view.findViewById(R.id.iv_img);

            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);

            TextView tv_food_type = (TextView) view.findViewById(R.id.tv_food_type);
            TextView tv_avarge_money = (TextView) view.findViewById(R.id.tv_avarge_money);
            TextView tv_number_good_friends_share = (TextView) view.findViewById(R.id.tv_number_good_friends_share);
            View view_xian_1 = view.findViewById(R.id.view_xian1);
            View view_xian_2 = view.findViewById(R.id.view_xian2);


            TextView tv_address = (TextView) view.findViewById(R.id.tv_address);
            final DotInfo info = list.get(i);

            double level = info.getStarLevel();

            if (level > 0) {
                lin_1_1.setVisibility(View.VISIBLE);
                rating_bar.setLevel((int) level);

                DecimalFormat df = new DecimalFormat("###.0");
                tv_stervl.setText(df.format(level) + "");
            } else {
                lin_1_1.setVisibility(View.GONE);
            }


            FFImageLoader.loadBigImage(this, info.getImg().getUrl(), iv_img);
            SYPoi poi = info.getPoi();
            if (poi != null) {
                tv_title.setText(poi.getTitle());
                tv_address.setText(poi.getAddress());
            }
            String xgd = info.getXgd();
            if (xgd.contains("x")) {
                iv_xiang.setImageResource(R.mipmap.xiang);
            }
            if (xgd.contains("g")) {
                iv_guan.setImageResource(R.mipmap.guan);
            }
            if (xgd.contains("d")) {
                iv_da.setImageResource(R.mipmap.da);
            }


            if (info.getPtype() != null && info.getPtype().length() > 0) {
                tv_food_type.setVisibility(View.VISIBLE);
                tv_food_type.setText(info.getPtype());
            } else {
                tv_food_type.setVisibility(View.GONE);
            }

            if (info.getPrice() > 0) {
                tv_avarge_money.setVisibility(View.VISIBLE);
                tv_avarge_money.setText(info.getPrice() + "/人");
            } else {
                tv_avarge_money.setVisibility(View.GONE);
            }
            if (info.getShareNum() > 0) {
                tv_number_good_friends_share.setVisibility(View.VISIBLE);
                tv_number_good_friends_share.setText(info.getShareNum() + "位好友分享过");
            } else {
                tv_number_good_friends_share.setVisibility(View.GONE);
            }
            int gone = View.GONE;
            if (tv_food_type.getVisibility() == gone && tv_avarge_money.getVisibility() == gone && tv_number_good_friends_share.getVisibility() == gone) {
                view_xian_1.setVisibility(View.GONE);
                view_xian_2.setVisibility(View.GONE);
            } else if (tv_food_type.getVisibility() == gone && tv_avarge_money.getVisibility() != gone && tv_number_good_friends_share.getVisibility() != gone) {
                view_xian_1.setVisibility(View.GONE);
                view_xian_2.setVisibility(View.VISIBLE);
            } else if (tv_food_type.getVisibility() == gone && tv_avarge_money.getVisibility() == gone && tv_number_good_friends_share.getVisibility() != gone) {
                view_xian_1.setVisibility(View.GONE);
                view_xian_2.setVisibility(View.GONE);
            } else if (tv_food_type.getVisibility() != gone && tv_avarge_money.getVisibility() == gone && tv_number_good_friends_share.getVisibility() == gone) {
                view_xian_1.setVisibility(View.GONE);
                view_xian_2.setVisibility(View.GONE);
            } else if (tv_food_type.getVisibility() != gone && tv_avarge_money.getVisibility() != gone && tv_number_good_friends_share.getVisibility() == gone) {
                view_xian_1.setVisibility(View.VISIBLE);
                view_xian_2.setVisibility(View.GONE);
            } else if (tv_food_type.getVisibility() != gone && tv_avarge_money.getVisibility() == gone && tv_number_good_friends_share.getVisibility() != gone) {
                view_xian_1.setVisibility(View.VISIBLE);
                view_xian_2.setVisibility(View.GONE);
            }

            view.setTag(i);
            final DotInfo dot = list.get(i);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //// TODO: 2017-1-3

                    RestInfoIntent intent = new RestInfoIntent();
                    intent.setId(dot.getPoi().getId());
                    intent.setStar(dot.getStarLevel());
                    startActivity(RestInfoActivity.class, intent);


                }
            });
            ig.add(view);
            if (i == 0 && ig.size() != 0)
                view1.setImageResource(R.drawable.dot_normal);
            else if (i != 0)
                view1.setImageResource(R.drawable.dot_focused);

            view1.setLayoutParams(params);
            enty_cir.addView(view1);
        }
        MyPageAdapter pageAdapter = new MyPageAdapter(ig, list, DeliciousFoodMapActivity.this);
        view_page.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.com_sina_weibo_sdk_loginview_padding_bottom));
        view_page.setAdapter(pageAdapter);

        view_page.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                enty_cir.removeAllViews();
                for (int x = 0; x < ig.size(); x++) {
                    ImageView view2 = new ImageView(DeliciousFoodMapActivity.this);
                    if (position % ig.size() != x)
                        view2.setImageResource(R.drawable.dot_focused);
                    else
                        view2.setImageResource(R.drawable.dot_normal);

                    view2.setLayoutParams(params);

                    enty_cir.addView(view2);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private String lastId = "0";

    private void getfoodlocation(final String city) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/shop/queryShopByCityV270.do", "0".equals(lastId) ? "" : null, extra, new FFNetWorkCallBack<DeliciousFoodListResult>() {
        post(IUrlUtils.Search.queryShopByCityV270, "0".equals(lastId) ? "" : null, extra, new FFNetWorkCallBack<DeliciousFoodListResult>() {
            @Override
            public void onSuccess(DeliciousFoodListResult response, FFExtraParams extra) {
                if (response.judge()) {
                    for (int i = 0; i < response.getList().size(); i++) {
                        DotInfo info = response.getList().get(i);
                        //坐标转换
                        SYPoi po = info.getPoi();
                        double[] x = GPSUtil.gcj02_To_Bd09(po.getLatitude(), po.getLongitude());
                        po.setLatitude(x[0]);
                        po.setLongitude(x[1]);
                        info.setPoi(po);
                    }
                    dotList.addAll(response.getList());
                    two_dotList.addAll(response.getList());
                    addMarkers(response.getList());
                    if (response.getHasMore() != 0) {
                        SYPoi poi = two_dotList.get(two_dotList.size() - 1).getPoi();
//                        String lastid="";
                        if (poi != null) {
                            lastId = poi.getId();
                        }

                        getfoodlocation(city);
                    }


                } else {
                    showToast(response.getServerMsg());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "city", city, "pageSize", "300", "lastShopId", lastId);
    }


    @Override
    public void onMapLoaded() {

    }

    private void selectTv(TextView tv) {
        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.rest_result_arrow_up, 0);
        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    private void unselectTv(TextView tv, boolean isReal) {
        boolean fl1 = false;
        boolean fl2 = false;
        boolean fl3 = false;
        int sum = 0;
        for (int i = 0; i < list__shuaixuan.size(); i++) {
            if (i == 0) {
                boolean checked = isReal ? list__shuaixuan.get(i).isRealyChecked() : list__shuaixuan.get(i).isChecked();
                if (checked) {
                    fl1 = true;
                    sum++;
                }

            }
            if (i == 1) {
                boolean checked = isReal ? list__shuaixuan.get(i).isRealyChecked() : list__shuaixuan.get(i).isChecked();
                if (checked) {
                    fl2 = true;
                    sum++;
                }
            }
            if (i == 2) {
                boolean checked = isReal ? list__shuaixuan.get(i).isRealyChecked() : list__shuaixuan.get(i).isChecked();
                if (checked) {
                    fl3 = true;
                    sum++;
                }
            }
        }
        if ((fl1 || fl2 || fl3) && tv == tv_pick1) {
            tv.setTextColor(getResources().getColor(R.color.colorPrimary));
            if (sum > 0) {
                tv.setText("筛选" + sum);
            } else {
                tv.setText("筛选");
            }
        } else {
            if (tv == tv_pick1) {
                tv.setText("筛选");
            }

            tv.setTextColor(getResources().getColor(R.color.ff_text_black));
        }
        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.rest_result_arrow_down, 0);
    }

    private int pinlei_flag = 1, shaixuan_flag = 1;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_pinlei: //品类
                lv_view0.getLayoutParams().height = hight + 45;
                if (pinlei_flag == 1) {
                    rl_2.setVisibility(View.VISIBLE);
                    selectTv(tv_class);
                    pinlei_flag = 2;
                    lv_view0.setVisibility(View.VISIBLE);
                    lv_view1.setVisibility(View.GONE);
                    setlv_list0();
                    iv_guiwei.setVisibility(View.GONE);
                    iv_jiajing.setVisibility(View.GONE);
                } else {
                    //关闭品类
                    unselectTv(tv_class, true);
                    rl_2.setVisibility(View.GONE);
                    pinlei_flag = 1;
                    lv_view0.setVisibility(View.GONE);
                    lv_view1.setVisibility(View.GONE);
                    iv_guiwei.setVisibility(View.VISIBLE);
                    iv_jiajing.setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < list__shuaixuan.size(); i++) {
                    list__shuaixuan.get(i).setChecked(list__shuaixuan.get(i).isRealyChecked());
                }
                unselectTv(tv_pick1, true);
                shaixuan_flag = 1;
                break;
            case R.id.lin_shaixuan://筛选

                unselectTv(tv_class, true);
                if (shaixuan_flag == 1) {
                    rl_2.setVisibility(View.VISIBLE);

                    selectTv(tv_pick1);
                    shaixuan_flag = 2;
                    lv_view1.setVisibility(View.VISIBLE);
                    lv_view0.setVisibility(View.GONE);
                    setlv_list1();
                    iv_guiwei.setVisibility(View.GONE);
                    iv_jiajing.setVisibility(View.GONE);
                } else {
                    //关闭筛选
                    rl_2.setVisibility(View.GONE);
                    lv_view0.setVisibility(View.GONE);
                    lv_view1.setVisibility(View.GONE);
                    unselectTv(tv_pick1, true);

                    for (int i = 0; i < list__shuaixuan.size(); i++) {
                        list__shuaixuan.get(i).setChecked(list__shuaixuan.get(i).isRealyChecked());
                    }
                    shaixuan_flag = 1;
                    iv_guiwei.setVisibility(View.VISIBLE);
                    iv_jiajing.setVisibility(View.VISIBLE);

                    resetjuhe(shaixuan_conditions(zhonglei_content, xgd_code, SetMarkIconStytle));

                }
                unselectTv(tv_class, false);
                pinlei_flag = 1;
                break;

            case R.id.rl_2:
                pinlei_flag = 1;
                shaixuan_flag = 1;
                unselectTv(tv_pick1, true);
                unselectTv(tv_class, true);
                for (int i = 0; i < list__shuaixuan.size(); i++) {
                    list__shuaixuan.get(i).setChecked(list__shuaixuan.get(i).isRealyChecked());
                }
                rl_2.setVisibility(View.GONE);
                iv_guiwei.setVisibility(View.VISIBLE);
                iv_jiajing.setVisibility(View.VISIBLE);
                break;
            case R.id.rl_viewpage:
                view_page.setVisibility(View.GONE);
                enty_cir.setVisibility(View.GONE);
                rl_viewpage.setVisibility(View.GONE);
                break;

            case R.id.btn_ok:
                iv_guiwei.setVisibility(View.VISIBLE);
                iv_jiajing.setVisibility(View.VISIBLE);
                rl_2.setVisibility(View.GONE);
                pinlei_flag = 1;
                shaixuan_flag = 1;
                unselectTv(tv_pick1, false);
                for (int i = 0; i < list__shuaixuan.size(); i++) {
                    list__shuaixuan.get(i).setRealyChecked(list__shuaixuan.get(i).isChecked());
                }

                resetjuhe(shaixuan_conditions(zhonglei_content, xgd_code, SetMarkIconStytle));
                break;
            case R.id.btn_reset:
                resetPick(); //数据重置
                xgd_code = "";
//                resetjuhe(shaixuan_conditions(zhonglei_content,xgd_code));
                unselectTv(tv_pick1, false);
                break;

            case R.id.iv_guiwei:

                if (L_location != null) {
                    LatLng ll;
                    if (isSameCity){
                        ll = new LatLng(L_location.getLatitude(),L_location.getLongitude());
                    } else {
                        ll = new LatLng(currentCity.getLatitude(),currentCity.getLongitude());
                    }

                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(18.0f);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                }

                break;
            case R.id.iv_jiajing:
                if (SetMarkIconStytle == 1) {
                    SetMarkIconStytle = 2;

                    iv_jiajing.setImageResource(R.mipmap.jingxun_map_selector);
                } else {
                    SetMarkIconStytle = 1;
                    iv_jiajing.setImageResource(R.mipmap.jingxun_map_normal);
                }

                mClusterManager.setRenderer(new DefaultClusterRenderer<>(this, mBaiduMap, mClusterManager));

                resetjuhe(shaixuan_conditions(zhonglei_content, xgd_code, SetMarkIconStytle));
                break;
        }
    }

    MyBaseAdapter<SiftHolder, SiftBean.FoodKindListBean.FoodKindBean> pinlei_adapter;

    private void setlv_list0() {
        if (pinlei_adapter == null) {
            pinlei_adapter = new MyBaseAdapter<SiftHolder, SiftBean.FoodKindListBean.FoodKindBean>(SiftHolder.class, DeliciousFoodMapActivity.this, R.layout.item_rest_sift, list_zhonglei) {
                @Override
                public void initView(View convertView, SiftHolder holder, int position, final SiftBean.FoodKindListBean.FoodKindBean item) {
                    holder.textView.setText(item.getContent());
                    holder.textView.setTextColor(getResources().getColor(R.color.ff_text_black));
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tv_class.setText(item.getContent());
                            unselectTv(tv_class, true);
//                                    lv_view0.setVisibility(View.GONE);
                            rl_2.setVisibility(View.GONE);
                            lv_view0.setVisibility(View.GONE);
                            pinlei_flag = 1;
                            //// TODO: 2017-1-3   循环取 flag数据
                            zhonglei_content = item.getContent();
                            resetjuhe(shaixuan_conditions(zhonglei_content, xgd_code, SetMarkIconStytle));
                        }
                    });
                }

            };
            lv_view0.setAdapter(pinlei_adapter);
        }
    }

    MyBaseAdapter<SiftHolder, SiftBean.FilterListBean.FilterBean> shai_adapter;

    private void setlv_list1() {
        if (shai_adapter == null) {
            shai_adapter = new MyBaseAdapter<SiftHolder, SiftBean.FilterListBean.FilterBean>(SiftHolder.class, DeliciousFoodMapActivity.this, R.layout.item_rest_sift, list__shuaixuan) {
                @Override
                public void initView(View convertView, final SiftHolder holder, int position, final SiftBean.FilterListBean.FilterBean item) {
                    holder.textView.setText(item.getContent());
                    if (item.isChecked()) {
                        holder.textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                        holder.imageView.setVisibility(View.VISIBLE);
                        holder.imageView.setImageResource(R.mipmap.restrestult_selected);
                    } else {
                        holder.textView.setTextColor(getResources().getColor(R.color.ff_text_black));
                        holder.imageView.setVisibility(View.GONE);
                    }
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            item.setChecked(!item.isChecked());
                            holder.textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                            holder.imageView.setVisibility(View.VISIBLE);
                            notifyDataSetChanged();
                            //// TODO: 2017-1-3   循环取 flag数据
                            xgd_code = "";//先清空一次
                            for (int i = 0; i < list__shuaixuan.size(); i++) {
                                boolean bena = list__shuaixuan.get(i).isChecked();
                                if (bena)
                                    xgd_code += list__shuaixuan.get(i).getCode();
                            }
                            if (xgd_code.length() > 0) {
                                btn_reset.setEnabled(true);
                            } else {
                                btn_reset.setEnabled(false);
                            }
                        }
                    });
                }
            };

            View view = View.inflate(DeliciousFoodMapActivity.this, R.layout.view_resent, null);
//           rl_pick_sort= (RelativeLayout) view.findViewById(rl_pick_sort);
            btn_ok = (ImageView) view.findViewById(R.id.btn_ok);
            btn_ok.setOnClickListener(this);
            btn_reset = (ImageView) view.findViewById(R.id.btn_reset);
            btn_reset.setOnClickListener(this);
            btn_reset.setEnabled(false);
            lv_view1.addFooterView(view);
        }

        lv_view1.setAdapter(shai_adapter);
    }

    public static class SiftHolder {
        TextView textView;
        ImageView imageView;
    }

    private void resetPick() {
        for (SiftBean.FilterListBean.FilterBean bean : list__shuaixuan) {
            bean.setChecked(false);
            bean.setRealyChecked(false);
        }
//                tvPick1.setText("");
//                tvPickw.setTextColor(getResources().getColor(R.color.ff_text_black));
        unselectTv(tv_pick1, true);
        btn_reset.setEnabled(false);
        shai_adapter.notifyDataSetChanged();
    }

    private boolean check_is_have_xgd(String xdg_code) {
        if ("".equals(xdg_code)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean check_is_p_flag(String p_flag) {
        if ("".equals(p_flag) || "全部美食".equals(p_flag) || "品类".equals(p_flag)) {
            return false;
        }
        return true;
    }

    /**
     * @param p_flag      品类的ptytype，
     * @param xdg_code    xgd code值
     * @param jiajingflag 1默认  2是加精
     * @return
     */
    private List<DotInfo> shaixuan_conditions(String p_flag, String xdg_code, int jiajingflag) {
        List<DotInfo> list = new ArrayList<>();
        int len = two_dotList.size();
        boolean is_p_flag = check_is_p_flag(p_flag);//是否有品类筛选

        boolean is_xdg_code_flag = check_is_have_xgd(xdg_code);

        /**
         * 没有选品类、也没有选中筛选项，当然也没有加精
         */
        if (!is_p_flag && !is_xdg_code_flag && jiajingflag == 1) {

            for (int i = 0; i < two_dotList.size(); i++) {
                DotInfo ii = two_dotList.get(i);
                ii.setBitmapDescriptor(1);
            }
            return two_dotList;
        }

        for (int i = 0; i < len; i++) {
            DotInfo info = dotList.get(i);
            String content = info.getPtype();
            String xgd = info.getXgd();

            double levle = info.getStarLevel();

            if (!is_p_flag && is_xdg_code_flag && jiajingflag == 1) {
                //没有选品类、但选中筛选项、没有加精
                if (xgd.contains(xdg_code)) {
                    info.setBitmapDescriptor(1);
                    list.add(info);
                }
            } else if (!is_p_flag && !is_xdg_code_flag && jiajingflag == 2) {
                //没有选品类、也没有选中筛选项、但有加精
                if (levle >= 3) {
                    info.setBitmapDescriptor(2);
                    list.add(info);
                }
            } else if (is_p_flag && !is_xdg_code_flag && jiajingflag == 1) {
                //有选品类、没有选中筛选项、但没有加精
                if (p_flag.equals(content)) {
                    info.setBitmapDescriptor(1);
                    list.add(info);
                }

            } else if (is_p_flag && is_xdg_code_flag && jiajingflag == 1) {
                //有选品类、也有选筛选项、但没有加精
                if (p_flag.equals(content) && xgd.contains(xdg_code)) {
                    info.setBitmapDescriptor(1);
                    list.add(info);
                }
            } else if (!is_p_flag && is_xdg_code_flag && jiajingflag == 2) {
                //没有选品类、但选了筛选项，并且也加精了
                if (xgd.contains(xdg_code) && levle >= 3) {
                    info.setBitmapDescriptor(2);
                    list.add(info);
                }
            } else if (is_p_flag && !is_xdg_code_flag && jiajingflag == 2) {
                //选了品类、没有筛选项、但加精了
                if (p_flag.equals(content) && levle >= 3) {
                    info.setBitmapDescriptor(2);
                    list.add(info);
                }
            } else if (is_p_flag && is_xdg_code_flag && jiajingflag == 2) {
                //选了品类、也选了筛选项、也加精了
                if (p_flag.equals(content) && xgd.contains(xdg_code) && levle >= 3) {
                    info.setBitmapDescriptor(2);
                    list.add(info);
                }
            }
//                    else if(!check_is_p_flag(p_flag)&&check_is_have_xgd(xdg_code)){
//                        if (p_flag.equals(content))
//                            list.add(info);
//                    }
//                    else if(check_is_p_flag(p_flag)&&check_is_have_xgd(xdg_code)){
//                        if ( xdg_code.equals(xgd))
//                            list.add(info);
//                    }
        }

        return list;
    }

    /**
     * 重新聚合
     *
     * @param list
     */
    private void resetjuhe(List<DotInfo> list) {
        mClusterManager.clearItems();

//                for(int i=0;i<list.size();i++){
//                    mClusterManager.removeItem(list.get(i));  //把原来的点全部清除 重新加载
//                }
        addMarkers(list);
    }

    /**
     * 动态申请  权限后用用的选择
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case AddFriendsActivty.MY_PERMISSIONS_REQUEST_READ_CONTACTS_ADD: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //用户授权
                    initLoation();
                } else {
                    showToast("未获得访问权限，不能查看！！");
                }
                return;
            }

        }
    }

    @Override
    public void onBackPressed(View v) {

        if (rl_viewpage.getVisibility() == View.VISIBLE) {
            rl_viewpage.setVisibility(View.GONE);
        } else {
            onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (rl_viewpage.getVisibility() == View.VISIBLE) {
                rl_viewpage.setVisibility(View.GONE);
            } else {
                finish();
            }


        }

        return false;

    }


}
