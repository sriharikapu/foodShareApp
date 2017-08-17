package com.fan.framework.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.SystemClock;

public class FFHttpCache {
    private static final SharedPreferences sp = FFApplication.app.getSharedPreferences("httpCache", Context.MODE_PRIVATE);
    private static final String assembleWord = "#validTime";

    private FFHttpCache() {
    }

    public static void storeCache(String key, Object value) {
        Editor edit = sp.edit();
        if (value == null) {
            edit.remove(key).remove(key + assembleWord);
            edit.commit();
            return;
        }
        if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            edit.putFloat(key, (Float) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            edit.putLong(key, (Long) value);
        } else if (value instanceof String) {
            edit.putString(key, (String) value);
        } else {
            edit.putString(key, value.toString());
        }
        edit.putLong(key + assembleWord, SystemClock.elapsedRealtime());
        edit.commit();
    }

    public static void clear() {
        sp.edit().clear().commit();
    }

    public static String getString(String key, String defaultValue, long validTime) {
        long storeTime = sp.getLong(key + assembleWord, 0);
        if (storeTime == 0) {
            return defaultValue;
        }
        if (validTime == 0) {
            return sp.getString(key, defaultValue);
        }
        if (storeTime < SystemClock.elapsedRealtime() - validTime) {
            sp.edit().remove(key).remove(key + assembleWord).commit();
            return defaultValue;
        }
        return sp.getString(key, defaultValue);
    }
}
