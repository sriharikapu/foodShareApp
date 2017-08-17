package com.fengnian.smallyellowo.foodie;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;

/**
 * Created by Administrator on 2016-11-23.
 */

public abstract class PermissionActivity extends BaseActivity {

    boolean needCamera = false;
    boolean needRecord = false;
    boolean needLocation = false;
    boolean needState = true;
    boolean needSd = true;


    private static String[] PERMISSIONS_STORAGE = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public boolean verifyStoragePermissions() {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);
            return false;
        }
        return true;
    }

    @SuppressLint("InlinedApi")
    public boolean verifyStatePermissions() {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        int permission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 4);
            return false;
        }
        return true;
    }

    public boolean verifyLocationPermissions() {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        int permission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
            return false;
        }
        return true;
    }

    public boolean verifyPermissions(String permission, int code) {

        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        int p = ActivityCompat.checkSelfPermission(this, permission);

        if (p != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, code);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {// 允许了
                    requestcamera();
                } else {// 拒绝了
                    showToast("未获得sd卡权限，请打开系统设置并允许读写sd卡", null);
                }
                return;
            }
            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {// 允许了
                    requestLocation();
                } else {// 拒绝了
                    showToast("未获得使用相机权限，请打开系统设置并允许使用相机", null);
                }
                return;
            }
            case 3: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {// 允许了
                    requestState();
                } else {// 拒绝了
                    showToast("未获得定位权限，请打开系统设置并允许定位", null);
                }
                return;
            }
            case 4: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {// 允许了
                    requestRecord();
                } else {// 拒绝了
                    showToast("未获得读取手机唯一识别码权限，请打开系统设置并允许读取手机状态", null);
                }
                return;
            }
            case 5: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {// 允许了
                    onPermissionsAllowed();
                } else {// 拒绝了
                    showToast("未获得录音权限，请打开系统设置并允许录音", null);
                }
                return;
            }
        }
    }

    public boolean start() {
        return requestStorage();
    }

    private boolean requestStorage() {// 1
        if (!needSd) {//如果不需要权限
            return requestcamera();
        }
        if (verifyStoragePermissions()) {
            return requestcamera();
        }
        return false;
    }

    private boolean requestcamera() {// 2
        if (!needCamera) {//如果不需要权限
            return requestLocation();
        }

        if (verifyPermissions(android.Manifest.permission.CAMERA, 2)) {
            return requestLocation();
        }
        return false;
    }

    private boolean requestLocation() {// 3
        if (!needLocation) {//如果不需要权限
            return requestState();
        }

        if (verifyLocationPermissions()) {
            return requestState();
        }
        return false;
    }

    private boolean requestState() {// 4
        if (!needState) {//如果不需要权限
            return requestRecord();
        }

        if (verifyStatePermissions()) {
            return requestRecord();
        }
        return false;
    }

    private boolean requestRecord() {// 5
        if (!needRecord) {//如果不需要权限
            onPermissionsAllowed();
            return true;
        }

        if (verifyPermissions(android.Manifest.permission.RECORD_AUDIO, 5)) {//权限已获取
            onPermissionsAllowed();
            return true;
        }

        return false;
    }

    abstract void onPermissionsAllowed();

}
