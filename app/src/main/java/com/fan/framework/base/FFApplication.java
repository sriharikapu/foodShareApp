package com.fan.framework.base;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fan.framework.config.FFConfig;
import com.fan.framework.http.FFBaseBean;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWork;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFLogUtil;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.WelcomActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;

import java.lang.Thread.UncaughtExceptionHandler;


public abstract class FFApplication extends MultiDexApplication implements UncaughtExceptionHandler {
    /**
     * 保存一个当前app的静态引用
     */
    public static FFApplication app;
    /**
     * ui线程
     */
    private static Thread mUiThread;
    /**
     * 网络请求对象
     */
    private FFNetWork mNetWork;

    @Override
    public Object getSystemService(String name) {
        if (name.equals(Context.LAYOUT_INFLATER_SERVICE)) {
            LayoutInflater inflater = (LayoutInflater) super.getSystemService(name);
            if (inflater.getFactory() == null)
                inflater.setFactory(new FFLayoutInflaterFactory());
            return inflater;
        }
        return super.getSystemService(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        getDisplayMetrics();
        mNetWork = new FFNetWork(null);
        mUiThread = Thread.currentThread();
        // 捕获全局异常
        Thread.setDefaultUncaughtExceptionHandler(this);
        // 监听网络状态变化
        registerReceiver(new FFNetBroadcastReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public static void showToast(int resId, CharSequence text) {
        Toast toast = new Toast(app);
        View view = LayoutInflater.from(app).inflate(R.layout.ff_toast, null, false);
        ((TextView) view.findViewById(R.id.tv_msg)).setText(text);
        ((ImageView) view.findViewById(R.id.iv_icon)).setImageResource(resId);
        toast.setView(view);
        toast.show();
    }


    /**
     * post请求<br/>
     * 不会弹出进度条
     *
     * @param url      请求网址
     * @param extra    附加参数 可以为null
     * @param callBack 回调方法
     * @param params   请求参数
     */
    public static <T extends FFBaseBean> void post(String url, FFExtraParams extra, FFNetWorkCallBack<T> callBack, Object... params) {
        app.mNetWork.post(url, null, new FFExtraParams(extra).setQuiet(extra != null ? extra.isQuiet() : true), callBack, params);
    }

    /**
     * get请求<br/>
     * 不会弹出进度条
     *
     * @param url      请求网址
     * @param clazz    请求返回数据的数据类型
     * @param extra    附加参数 可以为null
     * @param callBack 回调方法
     * @param params   请求参数
     */
    public static <T extends FFBaseBean> void get(String url, Class<T> clazz, FFExtraParams extra, FFNetWorkCallBack<T> callBack, Object... params) {
        app.mNetWork.get(url, null, extra, callBack, clazz, params);
    }

    /**
     * 显示toast 自动切换到主线程
     *
     * @param msg
     * @param debugMsg
     */
    public static void showToast(final CharSequence msg, final CharSequence debugMsg) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            FFLogUtil.e("FFApplication", "主线程");
            mShowToast(msg, debugMsg);
        } else {
            FFLogUtil.e("FFApplication", "非主线程");
            FFUtils.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    mShowToast(msg, debugMsg);
                }
            });
        }
    }

    private static void mShowToast(CharSequence msg, CharSequence debugMsg) {
        if (FFConfig.SHOW_DEBUG_TOAST && debugMsg != null) {
            SpannableString ss = new SpannableString(debugMsg + "\n" + msg);
            ss.setSpan(new ForegroundColorSpan(0xffff8888), 0, debugMsg != null ? debugMsg.toString().length() : 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            Toast toast = Toast.makeText(app, ss, Toast.LENGTH_SHORT);
            int  id = Resources.getSystem().getIdentifier("message", "id", "android");
            TextView tv = (TextView)toast.getView().findViewById(id);
            if(tv != null){
                tv.setGravity(Gravity.CENTER);
            }
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            if (msg != null) {
                Toast toast = Toast.makeText(app, msg, Toast.LENGTH_SHORT);
                int  id = Resources.getSystem().getIdentifier("message", "id", "android");
                TextView tv = (TextView)toast.getView().findViewById(id);
                if(tv != null){
                    tv.setGravity(Gravity.CENTER);
                }
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
        }
    }

    /**
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        if (Thread.currentThread() == mUiThread) {
            FFLogUtil.e("FFApplication", "主线程");
            runnable.run();
        } else {
            FFUtils.getHandler().post(runnable);
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        FFLogUtil.i("uncaughtException", "Application uncaughtException。。。。。。");
        if (FFConfig.LOG2SD_ENABLE) {
            exception2file(ex, false);
        } else {
            ex.printStackTrace();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        Intent intent = new Intent(getApplicationContext(), WelcomActivity.class);
        intent.putExtra("hhh", "ggg");
        PendingIntent restartIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        //退出程序
        AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500,
                restartIntent); // 1秒钟后重启应用
        BaseActivity.finishActvities();
        android.os.Process.killProcess(android.os.Process.myPid());
//		System.exit(1);
    }

    private void exception2file(Throwable ex, boolean cause) {
        FFLogUtil.i("uncaughtException", cause ? "Cause by: " + ex.toString() : ex.toString());
        StackTraceElement[] ss = ex.getStackTrace();
        for (StackTraceElement s : ss) {
            String info = s.toString();
            if (info.startsWith("android.app.ActivityThread.access")) {
                FFLogUtil.i("uncaughtException", "...more");
                break;
            }
            FFLogUtil.i("uncaughtException", info);
        }
        Throwable c = ex.getCause();
        if (c != null) {
            exception2file(c, true);
        }
    }

    public abstract void onNetStatusChanged(boolean isConnect);

    //初始化得到屏幕宽高
    private void getDisplayMetrics() {
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayUtil.screenWidth = display.getWidth();
        DisplayUtil.screenHeight = display.getHeight();
    }
}
