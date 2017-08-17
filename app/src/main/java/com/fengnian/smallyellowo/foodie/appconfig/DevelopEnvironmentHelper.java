package com.fengnian.smallyellowo.foodie.appconfig;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by chenglin on 2017-8-2.
 */

public class DevelopEnvironmentHelper {
    private Context mContext;
    private int clickCount = 0;
    private static int TIME_LONG = 2 * 1000;
    private long mLastTime = 0;

    private DevelopEnvironmentHelper() {
    }

    public DevelopEnvironmentHelper(Context context) {
        mContext = context;
    }

    public void changeEnvironment() {
        //如果是正式环境，并且SD卡下面有 fflog.log 文件，那么才能切换环境
        if (Constants.shareConstants().getAppEnvironment() == Constants.ENV_RELEASE) {
            String logFilePath = Environment.getExternalStorageDirectory() + "/fflog.log";
            File file = new File(logFilePath);
            if (file.exists()) {
                showDialog();
            }
        } else {
            showDialog();
        }
    }

    //切换环境：2秒钟之内连续点击十次，进入开发者模式
    private void showDialog() {
        long exitTime = System.currentTimeMillis();
        if (exitTime - mLastTime < TIME_LONG) {
            clickCount = clickCount + 1;
        } else {
            mLastTime = exitTime;
            clickCount = 0;
        }
        if (clickCount == 10) {
            clickCount = 0;
            DevelopEnvironmentDialog dialog = new DevelopEnvironmentDialog(mContext);
            dialog.show();
        }
    }
}
