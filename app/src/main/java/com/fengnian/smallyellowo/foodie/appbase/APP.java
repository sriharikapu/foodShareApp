package com.fengnian.smallyellowo.foodie.appbase;

import android.os.Looper;

import com.alibaba.fastjson.parser.ParserConfig;
import com.baidu.mapapi.SDKInitializer;
import com.fan.framework.base.FFApplication;
import com.fengnian.smallyellowo.foodie.MainActivity;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.publics.SYObjectDeserializer;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.seles.tusdk.FilterManager;
import org.lasque.tusdk.core.utils.TLog;

import static com.fan.framework.config.FFConfig.IS_OFFICIAL;


public class APP extends FFApplication {
    public UPushHelper mUPushHelper;

    private static boolean mTuSdkInited = false;

    @Override
    public void onNetStatusChanged(boolean isConnect) {
        MainActivity.onNetStatusChanged(isConnect);
    }

    @Override
    public void onCreate() {
        TLog.d("app oncreate");
        mUPushHelper = new UPushHelper(this);
        mUPushHelper.init();
        TuSdk.enableDebugLog(true);


        if (AppHelper.isAppMainProcess(this)) {
            super.onCreate();
//            //耗时操作，一定要放到异步线程里
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    QbSdk.initX5Environment(app, null);
//                    TuSdk.enableDebugLog(true);
//                    TuSdk.init(app, "1faa2d433fef2f60-03-glpcq1");
                    Looper.loop();
                }
            }.start();
            LeakCanary.install(this);
            SDKInitializer.initialize(app);
            MobclickAgent.setDebugMode(!IS_OFFICIAL);
            ParserConfig.getGlobalInstance().putDeserializer(SYUser.class, new SYObjectDeserializer(ParserConfig.getGlobalInstance(), SYUser.class));
            ParserConfig.getGlobalInstance().putDeserializer(SYFeed.class, new SYObjectDeserializer(ParserConfig.getGlobalInstance(), SYFeed.class));
        }
    }

    /** 滤镜管理器委托 */
    private FilterManager.FilterManagerDelegate mFilterManagerDelegate = new FilterManager.FilterManagerDelegate()
    {
        @Override
        public void onFilterManagerInited(FilterManager manager)
        {
            TLog.i("init success");
        }
    };
}