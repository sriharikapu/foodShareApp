package com.fan.framework.imageloader;

import android.graphics.Bitmap;

import com.fan.framework.base.FFApplication;

/**
 * 图片回调接口
 */
public abstract class FFImageCallBack {
    public abstract void imageLoaded(Bitmap bitmap, String imageUrl);

    private Object url;

    public void onProgress(final int downloaded, final int contentLength) {
        FFApplication.runOnUiThread(new Runnable() {
            public void run() {
                onDownLoadProgress(downloaded, contentLength);
            }
        });
    }

    public abstract void onDownLoadProgress(int downloaded, int contentLength);

    public Object getUrl() {
        return url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }
}