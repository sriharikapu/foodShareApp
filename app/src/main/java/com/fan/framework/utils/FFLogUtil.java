package com.fan.framework.utils;

import android.util.Log;

import com.fan.framework.config.FFConfig;

public class FFLogUtil {
    public static void i(String tag, String message) {
        if (FFConfig.LOG_ENABLE) {
            Log.i(tag, message);
            if (FFConfig.LOG2SD_ENABLE) {
                FFLoger.i("[" + tag + "] " + message);
            }

        }
    }

    public static void e(String tag, String message) {
        if (FFConfig.LOG_ENABLE) {
            Log.e(tag, message);
            if (FFConfig.LOG2SD_ENABLE) {
                FFLoger.i("[" + tag + "] " + message);
            }
        }
    }

    public static void e(String tag, Throwable ex) {
        e(tag, ex, false);
    }

    private static void e(String tag, Throwable ex, boolean cause) {
        if (FFConfig.LOG_ENABLE) {

            FFLogUtil.i(tag, cause ? "Cause by: " + ex.toString() : ex.toString());
            StackTraceElement[] ss = ex.getStackTrace();
            for (StackTraceElement s : ss) {
                String info = s.toString();
                if (info.startsWith("android.app.ActivityThread.access")) {
                    FFLogUtil.i(tag, "...more");
                    break;
                }
                FFLogUtil.i(tag, info);
            }
            Throwable c = ex.getCause();
            if (c != null) {
                e(tag, c, true);
            }
        }
    }

    public static void d(String tag, String message) {
        if (FFConfig.LOG_ENABLE) {
            Log.d(tag, message);
            if (FFConfig.LOG2SD_ENABLE) {
                FFLoger.i("[" + tag + "] " + message);
            }
        }
    }

    public static synchronized void cleanAppLog() {
        FFLoger.clean();
    }
}
