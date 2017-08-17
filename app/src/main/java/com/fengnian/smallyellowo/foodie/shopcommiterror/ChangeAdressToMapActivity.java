package com.fengnian.smallyellowo.foodie.shopcommiterror;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.intentdatas.ShopErrorInfoIntent;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

/**
 * Created by Administrator on 2017-3-29.
 */

public class ChangeAdressToMapActivity extends BaseActivity<ShopErrorInfoIntent> implements AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener, AMap.OnMarkerDragListener, AMap.OnMapLoadedListener,
        View.OnClickListener, AMap.InfoWindowAdapter, AMap.OnCameraChangeListener,GeocodeSearch.OnGeocodeSearchListener{
    private AMap aMap;
    private MapView mapView;
    private TextView tv_address,tv_button;
    private UiSettings mUiSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMenuContainer().removeAllViews();
        setContentView(R.layout.activity_change_adress_to_map);
        mapView = (MapView) findViewById(R.id.map);

        tv_address=findView(R.id.tv_address);
        tv_address.setText(getIntentData().getAddress());
        tv_button=findView(R.id.tv_button);
        tv_button.setOnClickListener(this);
        tv_button.setClickable(false);
        mapView.onCreate(savedInstanceState); // 此方法必须重写
        init();
    }
    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
            mUiSettings = aMap.getUiSettings();
            mUiSettings.setZoomControlsEnabled(false);
        }


    }

    /**
     * 隐藏放大缩小的图标
     */
    void   ZoomLogo(){
    }

    private void setUpMap() {
        aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
        aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
        aMap.setOnCameraChangeListener(this);
        addMarkersToMap();// 往地图上添加marker
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap() {
        drawMarkers();
    }

    /**
     * 绘制系统默认的1种marker背景图片
     */
    public void drawMarkers() {

        View  v= View.inflate(ChangeAdressToMapActivity.this,R.layout.address_to_chang_icon,null);
        BitmapDescriptor d;
//        if(v!=null){
//             d=  BitmapDescriptorFactory.fromView(v);
//        }else{
//            BitmapDescriptor d=BitmapDescriptorFactory.fromResource(R.mipmap.err_map_icon);
//        }
        Marker marker = aMap.addMarker(new MarkerOptions()
                .position(new LatLng(getIntentData().getLat(), getIntentData().getLng()))
//                .title(getIntentData().getAddress())
                .icon(BitmapDescriptorFactory.fromView(v))
 )
                ;
//                .draggable(true))
        //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

        LatLng marker1 = new LatLng(getIntentData().getLat(), getIntentData().getLng());
        //设置中心点和缩放比例
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
        marker.setPositionByPixels(FFUtils.getDisWidth()/2,FFUtils.getDisHight()/2+FFUtils.getPx(56));//
    }


    @Override
    public void onClick(View v) {
     switch (v.getId()){
         case R.id.tv_button:
             String  detail;
             if(camera_Position!=null)
                 detail =camera_Position.target.longitude+";"+camera_Position.target.latitude+";"+addressName;
              else{
                 detail=getIntentData().getAddress();
             }
             commitbusinesserror_info(getIntentData().getShopid(),getIntentData().getError_type()+"",detail);
         break;
     }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }
    /**
     * 监听amap地图加载成功事件回调
     */
    @Override
    public void onMapLoaded() {
        // 设置所有maker显示在当前可视区域地图中
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(new LatLng(getIntentData().getLat(), getIntentData().getLng())).build();
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    /**
     * 监听拖动marker时事件回调
     */
    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
   int flag=0;
    CameraPosition camera_Position;
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
         camera_Position=cameraPosition;
         TargetToAdress(cameraPosition.target);
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {


    }
    private GeocodeSearch geocoderSearch;
    void   TargetToAdress(LatLng target){
        LatLonPoint point=new LatLonPoint(target.latitude,target.longitude);
        RegeocodeQuery query = new RegeocodeQuery(point, 0,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求

    }
String addressName;
    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                addressName = result.getRegeocodeAddress().getFormatAddress();
                if (flag > 3)
                    tv_address.setText(addressName);
                if (flag > 3) {
                    tv_button.setClickable(true);
                    tv_button.setBackgroundResource(R.drawable.login_normal);
                }
                flag++;
            } else {
                showToast("对不起，没有搜索到相关数据！");
            }
        } else {
            showToast("对不起，没有搜索到相关数据！");
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * 提交商户报错的错误信息
     */
    void  commitbusinesserror_info(String shopid,String type,String details) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/shop/shopErrorFeedback.do", "", extra, new FFNetWorkCallBack<BaseResult>() {
        post(IUrlUtils.Search.shopErrorFeedback, "", extra, new FFNetWorkCallBack<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response, FFExtraParams extra) {
                if (response.judge()) {
                    showToast("提交成功!");
                    finish();
                } else {
                    showToast(response.getServerMsg());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {

                return false;
            }
        }, "shopId", shopid, "type", type, "details", details);
    }
}
