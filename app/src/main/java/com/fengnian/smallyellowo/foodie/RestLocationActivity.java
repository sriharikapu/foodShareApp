package com.fengnian.smallyellowo.foodie;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.RestLocationIntent;

import java.net.URISyntaxException;

/**
 * 演示MapView的基本用法
 */
public class RestLocationActivity extends BaseActivity<RestLocationIntent> implements AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener, AMap.OnMarkerDragListener, AMap.OnMapLoadedListener,
        View.OnClickListener, AMap.InfoWindowAdapter {
    private AMap aMap;
    private MapView mapView;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.marker_button:
//                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_activity);
        setTitle(getIntentData().getName());
        mapView = (MapView) findViewById(R.id.map);
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
        }
    }

    private void setUpMap() {
        aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
        aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
        addMarkersToMap();// 往地图上添加marker
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
        Marker marker = aMap.addMarker(new MarkerOptions()
                .position(new LatLng(getIntentData().getLat(), getIntentData().getLng()))
                .title(getIntentData().getAddress())
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .draggable(true));
        LatLng marker1 = new LatLng(getIntentData().getLat(), getIntentData().getLng());
        //设置中心点和缩放比例
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
        marker.showInfoWindow();// 设置默认显示一个infowinfow
    }

    /**
     * 对marker标注点点击响应事件
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
        } else {
            marker.showInfoWindow();
        }
        aMap.invalidate();// 刷新地图
        return false;
    }

    private String[] items = new String[]{"百度地图", "高德地图"};
    private String[] packages = new String[]{"com.baidu.BaiduMap",
            "com.autonavi.minimap"};

    private boolean isBaiduMapInstalled() {
        try {
            return getPackageManager().getPackageInfo("com.baidu.BaiduMap", 0) != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected Drawable getIcon(int position) {
        try {
            return getPackageManager().getPackageInfo(packages[position], 0).applicationInfo
                    .loadIcon(getPackageManager());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isGaodeMapInstalled() {
        try {
            return getPackageManager()
                    .getPackageInfo("com.autonavi.minimap", 0) != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 监听点击infowindow窗口事件回调
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
//        ToastUtil.show(this, "你点击了infoWindow窗口" + marker.getTitle());
    }

    /**
     * 监听拖动marker时事件回调
     */
    @Override
    public void onMarkerDrag(Marker marker) {
//        String curDes = marker.getTitle() + "拖动时当前位置:(lat,lng)\n("
//                + marker.getPosition().latitude + ","
//                + marker.getPosition().longitude + ")";
    }

    /**
     * 监听拖动marker结束事件回调
     */
    @Override
    public void onMarkerDragEnd(Marker marker) {
//        markerText.setText(marker.getTitle() + "停止拖动");
    }

    /**
     * 监听开始拖动marker事件回调
     */
    @Override
    public void onMarkerDragStart(Marker marker) {
//        markerText.setText(marker.getTitle() + "开始拖动");
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

    /**
     * 监听自定义infowindow窗口的infocontents事件回调
     */
    @Override
    public View getInfoContents(Marker marker) {
        View infoContent = getLayoutInflater().inflate(
                R.layout.custom_info_contents, null);
        render(marker, infoContent);
        return infoContent;
    }

    /**
     * 监听自定义infowindow窗口的infowindow事件回调
     */
    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = getLayoutInflater().inflate(
                R.layout.custom_info_window, null);

        render(marker, infoWindow);
        return infoWindow;
    }

    /**
     * 自定义infowinfow窗口
     */
    public void render(Marker marker, View view) {
        TextView imageView = (TextView) view.findViewById(R.id.badge);
        imageView.setOnClickListener(new View.OnClickListener() {

                                         private void skipToGaode() {
                                             Intent intent = new Intent(
                                                     "android.intent.action.VIEW",
                                                     android.net.Uri
                                                             .parse("androidamap://viewMap?sourceApplication=小黄圈&poiname="
                                                                     + getIntentData().getName()
                                                                     + "&lat="
                                                                     + getIntentData().getLat()
                                                                     + "&lon="
                                                                     + getIntentData().getLng()
                                                                     + "&dev=0"));
                                             intent.addCategory("android.intent.category.DEFAULT");
                                             intent.setPackage("com.autonavi.minimap");
                                             startActivity(intent);
                                         }

                                         private void skipToBaidu() {
                                             double[] d = bd_encrypt(getIntentData().getLat(), getIntentData().getLng());
                                             Intent intent;
                                             try {
                                                 intent = Intent
                                                         .parseUri(
                                                                 "intent://map/direction?"
                                                                         + "origin=latlng:"
                                                                         + getIntentData().getLat()
                                                                         + ","
                                                                         + getIntentData().getLng()
                                                                         + "|"
                                                                         + "name:我的位置&destination=latlng:"
                                                                         + d[0]
                                                                         + ","
                                                                         + d[1]
                                                                         + "|name:"
                                                                         + getIntentData().getName()
                                                                         + "&mode=driving&region=北京&src=小黄圈美食俱乐部|"
                                                                         + "小黄圈#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end",
                                                                 Intent.URI_INTENT_SCHEME);
                                                 startActivity(intent);
                                             } catch (URISyntaxException e) {
                                                 e.printStackTrace();
                                             }
                                         }

                                         @Override
                                         public void onClick(View view) {

                                             boolean gaodeMapInstalled = isGaodeMapInstalled();
                                             boolean baiduMapInstalled = isBaiduMapInstalled();
                                             if (baiduMapInstalled && !gaodeMapInstalled) {
                                                 skipToBaidu();
                                             } else if (!baiduMapInstalled && gaodeMapInstalled) {
                                                 skipToGaode();
                                             } else if (baiduMapInstalled && gaodeMapInstalled) {
                                                 AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
                                                         context());
                                                 // 设置对话框的图标
                                                 builder.setIcon(R.mipmap.ic_launcher);
                                                 // 设置对话框的标题
                                                 builder.setTitle("选择要使用的地图");
                                                 BaseAdapter adapter = new BaseAdapter() {

                                                     @Override
                                                     public View getView(int position, View convertView,
                                                                         ViewGroup parent) {
                                                         View[] views;
                                                         if (convertView == null) {
                                                             views = new View[2];
                                                             convertView = getLayoutInflater().inflate(
                                                                     R.layout.item_elsenavi, null);
                                                             views[0] = convertView
                                                                     .findViewById(R.id.imageView1);
                                                             views[1] = convertView
                                                                     .findViewById(R.id.textView1);
                                                             convertView.setTag(views);
                                                         } else {
                                                             views = (View[]) convertView.getTag();
                                                         }
                                                         ((ImageView) views[0])
                                                                 .setImageDrawable(getIcon(position));
                                                         ((TextView) views[1]).setText(items[position]);
                                                         return convertView;
                                                     }

                                                     @Override
                                                     public long getItemId(int position) {
                                                         return 0;
                                                     }

                                                     @Override
                                                     public Object getItem(int position) {
                                                         return null;
                                                     }

                                                     @Override
                                                     public int getCount() {
                                                         return items.length;
                                                     }
                                                 };
                                                 DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                                                     @Override
                                                     public void onClick(
                                                             DialogInterface dialogInterface, int which) {
                                                         switch (which) {
                                                             case 0:
                                                                 skipToBaidu();
                                                                 break;
                                                             case 1:
                                                                 skipToGaode();
                                                                 break;

                                                             default:
                                                                 break;
                                                         }
                                                     }
                                                 };
                                                 builder.setAdapter(adapter, listener);
                                                 builder.create().show();
                                             } else {
                                                 showToast("您未安装地图软件");
                                             }
                                         }
                                     }
        );
        String title = marker.getTitle();
        TextView titleUi = ((TextView) view.findViewById(R.id.title));
        titleUi.setText(title);
    }


    /**
     * 高德地图
     *
     * @param gg_lat
     * @param gg_lon
     */
    double[] bd_encrypt(double gg_lat, double gg_lon) {
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * Math.PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * Math.PI);
        double[] result = new double[2];
        result[1] = z * Math.cos(theta) + 0.0065;
        result[0] = z * Math.sin(theta) + 0.006;
        return result;
    }

}
