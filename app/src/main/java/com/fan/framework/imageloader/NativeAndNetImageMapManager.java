package com.fan.framework.imageloader;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.LruCache;

import com.fan.framework.base.FFApplication;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.utils.FileUitl;
import com.fengnian.smallyellowo.foodie.bean.publish.SerializeUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2016-11-17.
 */
public class NativeAndNetImageMapManager {

    private static final LruCache<String, String> map = new LruCache(100) {
        @Override
        protected int sizeOf(Object key, Object value) {
            return 1;
        }

        @Override
        protected void entryRemoved(boolean evicted, Object key, Object oldValue, Object newValue) {
            super.entryRemoved(evicted, key, oldValue, newValue);
            if (oldValue.toString().startsWith(FileUitl.getCacheFileDir())) {
                new File(oldValue.toString()).delete();
            }
        }
    };

    public static void clear() {
        map.trimToSize(0);
    }


    static {
        SharedPreferences sp = getSP();
        String str = sp.getString("mainMap", null);
        if (str != null) {
            HashMap<String, String> m = SerializeUtil.readObject(str);
            Iterator<Map.Entry<String, String>> iterator = m.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                map.put(entry.getKey(), entry.getValue());
            }
        }


    }

    private static SharedPreferences getSP() {
        return FFApplication.app.getSharedPreferences("FFImageLoader", Context.MODE_PRIVATE);
    }


    public static void onImageUploadSuccessed(String path, String url) {
        if (FFUtils.isStringEmpty(path) || FFUtils.isStringEmpty(url)) {
            return;
        }
        synchronized (NativeAndNetImageMapManager.class) {
            map.put(url, path);
        }
        saveMap();
    }

    private static void saveMap() {
        new Thread() {
            @Override
            public void run() {
                synchronized (NativeAndNetImageMapManager.class) {
                    getSP().edit().putString("mainMap", SerializeUtil.serializeObject(map.snapshot())).commit();
                }
            }
        }.start();

    }

    public static String getUrl(String source, int width, int height) {
        if (FFUtils.isStringEmpty(source)) {
            return "";
        }

        if (source.startsWith("http")) {
            String path;
            synchronized (NativeAndNetImageMapManager.class) {
                path = map.get(source);
            }
            if (path == null || !new File(path).exists()) {
                return source;
            } else {
                return path;
            }
        } else {
            return source;
        }

    }


}
