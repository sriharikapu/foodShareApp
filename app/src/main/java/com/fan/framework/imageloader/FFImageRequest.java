package com.fan.framework.imageloader;

import android.widget.ImageView;

import com.fan.framework.base.FFContext;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;

/**
 * 创建日期2012-12-26下午12:36:52<br/>
 * 作用描述：图片请求封装类
 */
public class FFImageRequest {
    /**
     * @param doCache
     */
    public FFImageRequest(Object imageUrl, ImageView imageView, boolean doCache, int width, int height, FFImageCallBack callBack, FFContext activity, int type, int defaultResId) {
        if (width <= 0) {
            width = FFUtils.getDisWidth();
        }
        if (height <= 0) {
            height = FFUtils.getDisHight();
        }
        if(imageUrl instanceof  String) {
//            imageUrl = NativeAndNetImageMapManager.getUrl((String) imageUrl, width, height);
//            if (imageUrl != null && (((String) imageUrl).startsWith(Constants.shareConstants().publishEnvironmentHeaderOriginal) || ((String) imageUrl).startsWith(Constants.shareConstants().testEnvironmentHeaderOriginal))) {
//                imageUrl = imageUrl + "?x-oss-process=image/resize,m_mfit,h_" + height + ",w_" + width + ",limit_1/format,jpg/auto-orient,1/quality,Q_80";
//            }
        }
        this.setCallBack(callBack);
        this.setImageUrl(imageUrl);
        this.setHeight(height);
        this.setWidth(width);
        this.setImageView(imageView);
        if (imageView != null && (imageView.getTag(R.id.ff_tag_imageLoader) == null || !imageView.getTag(R.id.ff_tag_imageLoader).equals(imageUrl))) {
            setNeedRefresh(true);
            imageView.setTag(R.id.ff_tag_imageLoader, imageUrl);
        }
        if(callBack != null){
            callBack.setUrl(imageUrl);
        }
        this.setActivity(activity);
        this.setDoCache(doCache);
        this.setType(type);
        this.setDefaultResId(defaultResId);
    }

    public FFImageRequest() {
    }

    private Object imageUrl;
    private FFImageCallBack callBack;
    private int width;
    private int height;
    private ImageView imageView;
    private boolean doCache = true;
    private FFContext activity;

    //    private boolean isRound;
    private int type;
    private int defaultResId = 0;
    private boolean needRefresh = false;
    private int deletedResId = 0;
    private int status = 0;
    private int failedResId = 0;

    public int getType() {
        return type;
    }

    public FFImageRequest setType(int type) {
        this.type = type;
        return this;
    }

    public String getCacheKey() {
        return new StringBuilder(getImageUrl()+"").append("?h=").append(getHeight()).append("&w=").append(getWidth()).append("&type=").append(type).toString();
    }

    public FFImageRequest setDeletedResId(int deletedResId) {
        this.deletedResId = deletedResId;
        return this;
    }

    public boolean isNeedRefresh() {
        return needRefresh;
    }

    public FFImageRequest setNeedRefresh(boolean needRefresh) {
        this.needRefresh = needRefresh;
        return this;
    }

    public int getDefaultResId() {
        return defaultResId;
    }

    public FFImageRequest setDefaultResId(int defaultResId) {
        this.defaultResId = defaultResId;
        return this;
    }

    public boolean isRound() {
        return (type & FFImageLoader.TYPE_ROUND) == type;
    }

    public FFContext getActivity() {
        return activity;
    }

    public FFImageRequest setActivity(FFContext activity) {
        this.activity = activity;
        return this;
    }

    public boolean isDoCache() {
        return doCache;
    }

    public FFImageRequest setDoCache(boolean doCache) {
        this.doCache = doCache;
        return this;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public FFImageRequest setImageView(ImageView imageView) {
        this.imageView = imageView;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public FFImageRequest setHeight(int height) {
        if (height <= 0) {
            height = FFUtils.getDisHight();
        }
        this.height = height;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public FFImageRequest setWidth(int width) {
        if (width <= 0) {
            width = FFUtils.getDisWidth();
        }
        this.width = width;
        return this;
    }

    public FFImageCallBack getCallBack() {
        return callBack;
    }

    public FFImageRequest setCallBack(FFImageCallBack callBack) {
        this.callBack = callBack;
        return this;
    }

    public Object getImageUrl() {
        return imageUrl;
    }

    public FFImageRequest setImageUrl(Object imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFailedResId(int status) {
        if (status == 404) {
            if (deletedResId != 0) {
                return deletedResId;
            }
        }
        if (failedResId != 0) {
            return failedResId;
        }
        if (deletedResId != 0) {
            return deletedResId;
        }
        return defaultResId;
    }

    public FFImageRequest setFailedResId(int failedResId) {
        this.failedResId = failedResId;
        return this;
    }
}