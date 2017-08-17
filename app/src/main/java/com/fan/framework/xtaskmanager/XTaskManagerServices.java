package com.fan.framework.xtaskmanager;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class XTaskManagerServices extends Service {
    public XTaskManagerServices() {
    }

    public class XLocalTaskBind extends Binder{
        public XTaskManagerServices getServices(){
            return XTaskManagerServices.this;
        }
    }

    private final IBinder myBinder = new XLocalTaskBind();

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("XTaskManagerServices", "onBind: onBind");
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("XTaskManagerServices", "onCreate: onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("XTaskManagerServices", "onDestroy: onDestory");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d("XTaskManagerServices", "onStart: onStart");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("XTaskManagerServices", "onStartCommand: onStartCommdnd");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("XTaskManagerServices", "onUnbind: onUnbind");
        return super.onUnbind(intent);
    }
}
