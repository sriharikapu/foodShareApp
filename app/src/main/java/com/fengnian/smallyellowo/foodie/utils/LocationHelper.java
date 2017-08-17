package com.fengnian.smallyellowo.foodie.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by chenglin on 2017-4-5.
 */

public class LocationHelper {
    public static final String permission = Manifest.permission.ACCESS_FINE_LOCATION;
    private Context mContext;
    private LocationClient locationClient = null;

    public LocationHelper(Context context) {
        mContext = context;
        initLocation();
    }

    private LocationHelper() {
    }

    public void startLocation(BDLocationListener listener) {
        locationClient.start();  //调用此方法开始定位
        locationClient.registerLocationListener(listener);
    }

    public void stopLocation() {
        //退出时销毁定位
        if (locationClient != null) {
            locationClient.stop();
        }
    }

    public boolean isStarted() {
        if (locationClient != null) {
            return locationClient.isStarted();
        }
        return false;
    }

    private void initLocation() {
        locationClient = new LocationClient(mContext);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(false);      //是否打开GPS
        option.setCoorType("bd09ll");  //设置返回值的坐标类型。
        option.setPriority(LocationClientOption.NetWorkFirst);  //设置定位优先级
        option.setProdName("LocationDemo"); //设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
//        option.setScanSpan(UPDATE_TIME);  //设置定时定位的时间间隔。单位毫秒
        option.setIsNeedAddress(true);  //返回的定位结果包含地址信息
        locationClient.setLocOption(option);
    }


    public static boolean hasLocationPermission(Activity activity) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        int pCode = ActivityCompat.checkSelfPermission(activity, permission);
        if (pCode != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }
}
